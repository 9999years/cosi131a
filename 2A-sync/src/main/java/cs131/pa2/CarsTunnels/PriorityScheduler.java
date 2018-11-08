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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

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
public class PriorityScheduler extends Tunnel {
	private List<Tunnel> tunnels = new ArrayList<>();
	private PriorityVehicles vehicles = new PriorityVehicles();

	public PriorityScheduler(String name) {
		this(name, Collections.emptyList());
	}

	public PriorityScheduler(String name, Collection<? extends Tunnel> c) {
		this(name, c, Tunnel.DEFAULT_LOG);
	}

	public PriorityScheduler(String name, Collection<? extends Tunnel> c,
							 Log log) {
		super(name, log);
		tunnels.addAll(c);
	}

	@Override
	public boolean tryToEnterInner(Vehicle vehicle) {
		if (vehicles.isHighestPriority(vehicle)) {
			for (Tunnel tunnel : tunnels) {
				if (tunnel.tryToEnter(vehicle)) {
					// remove vehicle from waiting queue and associate with a tunnel
					vehicles.tunnelWaiting(vehicle, tunnel);
					return true;
				}
			}
		}
		// no tunnel could take it; now it's waiting
		vehicles.addWaiting(vehicle);
		return false;
	}

	@Override
	public void exitTunnelInner(Vehicle vehicle) {
		vehicles.exitTunnel(vehicle);
	}

}
