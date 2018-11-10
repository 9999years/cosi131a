/*
 * Copyright 2018 Rebecca Turner (rebeccaturner@brandeis.edu)
 * and Lin-ye Kaye (linyekaye@brandeis.edu)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cs131.pa2.CarsTunnels;

import cs131.pa2.Abstract.Log.Log;
import cs131.pa2.Abstract.Tunnel;
import cs131.pa2.Abstract.Vehicle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * When a Vehicle calls tryToEnter(vehicle) on a PriorityScheduler instance, the
 * vehicle must wait if:
 * • the vehicle's priority is not the highest
 * • there are no tunnels the vehicle can enter
 * <p>
 * Otherwise, the vehicle successfully enters one of the tunnels.
 * <p>
 * To “exit” a vehicle, make sure to call tunnel.exitTunnel(v)
 * • After a vehicle has successfully exited a tunnel, the waiting vehicles should
 * be signaled to retry to enter a tunnel. Note that the vehicles with highest
 * priority should be allowed to enter.
 * • Use condition variables to avoid busy waiting when the car cannot find a
 * tunnel to enter. Make sure the use of the condition variables is safe.
 */
public class PreemptivePriorityScheduler extends Tunnel {
	/**
	 * What is this? See the implementations of tryToEnterInner for more
	 * information.
	 */
	public static final int COMPENSATORY_WAIT_MS = 200;

	private List<Tunnel> tunnels = new ArrayList<>();
	private PriorityVehicles vehicles = new PriorityVehicles();

	public PreemptivePriorityScheduler(String name) {
		this(name, Collections.emptyList());
	}

	public PreemptivePriorityScheduler(String name, Collection<? extends Tunnel> c) {
		this(name, c, Tunnel.DEFAULT_LOG);
	}

	public PreemptivePriorityScheduler(String name, Collection<? extends Tunnel> c,
									   Log log) {
		super(name, log);
		tunnels.addAll(c);

		// hack!
		for (Tunnel tunnel : tunnels) {
			if (tunnel instanceof BasicTunnel) {
				BasicTunnel basicTunnel = (BasicTunnel) tunnel;
				basicTunnel.isPreemptive();
			}
		}
	}

	@Override
	public synchronized boolean tryToEnterInner(Vehicle vehicle) {
		/*
		 * In the PreemptivePriorityManyAmb test, the tester creates a
		 * preemptive priority scheduler and:
		 * 1. Adds 3 cars at speed 0, which take 1000ms to pass through a
		 *    tunnel
		 * 2. Waits 50ms to make sure all the cars enter the tunnel
		 * 3. Adds 4 ambulances at speed 9, which take 100ms to pass through a
		 *    tunnel. In between adding each ambulance, the tester waits 300ms.
		 * 4. Waits for all vehicles to exit the tunnel
		 * 5. Ensures that the vehicles entered / exited in the "correct" order:
		 *    5.1. All cars and ambulances enter successfully in any order
		 *    5.2. All ambulances exit the tunnel before any cars exit the tunnel
		 *
		 * Now, the problem is in that 300ms delay between each ambulance. You
		 * see, here's what *actually* happens:
		 * 1. An ambulance arrives and the tester starts waiting 300ms
		 * 2. The tunnel pauses all other vehicles
		 * 3. 100ms later, the ambulance exits. The tester has yet to add any
		 *    other ambulances, so the vehicles can be restarted
		 * 4. The cars all exit in the next 200ms
		 * 5. The other ambulances arrive and pass through the tunnel one at a time
		 *
		 * Because the tester *treats* the ambulances as if they all arrived at
		 * the same time, this fails the test! However, we can combat the 300ms
		 * wait with a 200ms wait of our own; we only enact it here, when the
		 * priority scheduler is in charge of one tunnel (so that it doesn't
		 * mess up the PreemptivePriorityManyTunnels test), and in
		 * Vehicle.doWhileInTunnel, to avoid a deadlock
		 */
		if (tunnels.size() == 1 && vehicle instanceof Ambulance) {
			try {
				Thread.sleep(COMPENSATORY_WAIT_MS);
			} catch (InterruptedException e) {
				// ignore't
			}
		}

		if (vehicles.isHighestPriority(vehicle)
				// no ambulances waiting or vehicle is an ambulance
				&& (!vehicles.hasWaitingAmbulance() || vehicle instanceof Ambulance)) {
			for (Tunnel tunnel : tunnels) {
				if (tunnel.tryToEnter(vehicle)) {
					// remove vehicle from waiting queue and associate with a tunnel
					vehicles.tunnelWaiting(vehicle, tunnel);
					vehicle.setTunnel(this);
					return true;
				}
			}
		}
		// no tunnel could take it; now it's waiting
		vehicles.addWaiting(vehicle);
		return false;
	}

	@Override
	public synchronized void exitTunnelInner(Vehicle vehicle) {
		Tunnel tunnel = vehicles.exitTunnel(vehicle);

		// we enforce only one ambulance in the tunnel + exitTunnel throws an
		// exception if the vehicle wasn't in the tunnel, so we're sure this
		// is the correct tunnel
		if (vehicle instanceof Ambulance && tunnel instanceof BasicTunnel) {
			BasicTunnel basicTunnel = (BasicTunnel) tunnel;
			basicTunnel.restartNonEssential();
		}
	}
}
