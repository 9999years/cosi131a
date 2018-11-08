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

import cs131.pa2.Abstract.Tunnel;
import cs131.pa2.Abstract.Vehicle;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * a queue of vehicles ordered by priority
 */
public class PriorityVehicles {
	private PriorityQueue<Vehicle> waiting =
			new PriorityQueue<>(Comparator.comparingInt(Vehicle::getPriority));
	private Map<Vehicle, Tunnel> inTunnels = new HashMap<>();

	public PriorityVehicles() {
	}

	public int waitingSize() {
		return waiting.size();
	}

	public int inTunnelsSize() {
		return inTunnels.size();
	}

	public void addWaiting(Vehicle vehicle) {
		waiting.add(vehicle);
	}

	public void addTunneled(Vehicle vehicle, Tunnel tunnel) {
		inTunnels.put(vehicle, tunnel);
	}

	public void tunnelWaiting(Vehicle vehicle, Tunnel tunnel) {
		// sometimes the vehicle never got into the waiting set. that's OK!
		waiting.remove(vehicle);
		inTunnels.put(vehicle, tunnel);
	}

	public void exitTunnel(Vehicle vehicle) {
		inTunnels.get(vehicle).exitTunnel(vehicle);
		inTunnels.remove(vehicle);
	}

	public boolean isHighestPriority(Vehicle vehicle) {
		return waiting.isEmpty()
				|| vehicle.getPriority() >= waiting.element().getPriority();
	}
}
