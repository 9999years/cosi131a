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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO
 */
public class PreemptivePriorityScheduler extends Tunnel{
	// Synchronization variables
	private final Lock lock = new ReentrantLock();
	private final Condition shouldEnter = lock.newCondition();

	// State variables
	private int waitingAmbulances = 0;
	private int activeAmbulances = 0;

	private List<Tunnel> tunnels = new ArrayList<>();
	private PriorityVehicles vehicles = new PriorityVehicles();

	public PreemptivePriorityScheduler(String name) {
		this(name, Collections.emptyList());
	}

	public PreemptivePriorityScheduler(String name, Collection<? extends Tunnel> c) {
		this(name, c, Tunnel.DEFAULT_LOG);
	}

	public PreemptivePriorityScheduler(String name, Collection<? extends Tunnel> c, Log log) {
		super(name, log);
		tunnels.addAll(c);
	}

	@Override
	public boolean tryToEnterInner(Vehicle vehicle) {
		// check priority
		if (vehicles.isHighestPriority(vehicle)) {
			for (Tunnel tunnel : tunnels) {
				if (tunnel.tryToEnter(vehicle)) {
					if (vehicle instanceof Ambulance) {

					}
					// vehicle entered tunnel, remove it from the waiting queue
					vehicles.tunnelWaiting(vehicle, tunnel);
					return true;
				}
			}
		}
		// vehicle didn't enter a tunnel, add it to waiting queue
		vehicles.addWaiting(vehicle);
		return false;
	}

	@Override
	public void exitTunnelInner(Vehicle vehicle) {
		vehicles.exitTunnel(vehicle);
	}

	// returns true if a vehicle should not be able to access a tunnel
	public boolean noAccess() {
		return (activeAmbulances > 0);
	}

	public void startAmbulance() {
		lock.lock();
		try {
			waitingAmbulances++;
			while (noAccess()) {
				shouldEnter.await();
			}
			waitingAmbulances--;
			activeAmbulances++;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void stopAmbulance() {
		lock.lock();
		try {
			activeAmbulances--;
			assert(activeAmbulances == 0);
			shouldEnter.signal();
		} finally {
			lock.unlock();
		}
	}

}
