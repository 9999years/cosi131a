/*
 * Copyright 2018 Rebecca Turner (rebeccaturner@brandeis.edu)
 * and Lin-Ye Kaye (linyekaye@brandeis.edu)
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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * a queue of waiting vehicles ordered by priority combined with a map
 * associating vehicles with the tunnel they're in
 */
public class PriorityVehicles {
	private PriorityQueue<Vehicle> waiting =
			new PriorityQueue<>(Comparator.comparingInt(Vehicle::getPriority));
	private Map<Vehicle, Tunnel> inTunnels = new HashMap<>();

	public PriorityVehicles() {
	}

	/**
	 * Add a vehicle to the waiting queue
	 */
	public void addWaiting(Vehicle vehicle) {
		waiting.add(vehicle);
	}

	/**
	 * Move a vehicle from the waiting queue to the tunneled set; doesn't
	 * require the vehicle to actually be in the waiting queue to begin with
	 */
	public void tunnelWaiting(Vehicle vehicle, Tunnel tunnel) {
		// sometimes the vehicle never got into the waiting set. that's OK!
		waiting.remove(vehicle);
		inTunnels.put(vehicle, tunnel);
	}

	/**
	 * Removes a vehicle from its tunnel and from this map
	 */
	public void exitTunnel(Vehicle vehicle) {
		inTunnels.get(vehicle).exitTunnel(vehicle);
		inTunnels.remove(vehicle);
	}

	/**
	 * is a vehicle's priority as high or higher than a given vehicle in the
	 * queue?
	 */
	public boolean isHighestPriority(Vehicle vehicle) {
		return waiting.isEmpty()
				|| vehicle.getPriority() >= waiting.element().getPriority();
	}
}
