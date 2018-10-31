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

import cs131.pa2.Abstract.Vehicle;

public class Vehicles {
	// the vehicles currently in the tunnel; this represents a horrible little
	// fixed-size array-set
	private Vehicle[] vehicles = new Vehicle[BasicTunnel.MAX_CAPACITY];
	private int vehiclesInTunnel = 0;

	/**
	 * adds a vehicle to the tunnel, performing NO checks for correctness
	 *
	 * @param vehicle the vehicle to add
	 * @return whether or not a vehicle was added
	 */
	public synchronized boolean add(Vehicle vehicle) {
		for (int i = 0; i < vehicles.length; i++) {
			if (vehicles[i] == null) {
				vehicles[i] = vehicle;
				vehiclesInTunnel++;
				return true;
			}
		}
		return false;
	}

	/**
	 * removes the specified vehicle from the tunnel; equality is determined
	 * with the identity operator
	 *
	 * @return true if a vehicle was removed
	 */
	public synchronized boolean remove(Vehicle vehicle) {
		for (int i = 0; i < vehicles.length; i++) {
			if (vehicles[i] == vehicle) {
				// nulling out the reference lets the JVM GC collect it
				vehicles[i] = null;
				vehiclesInTunnel--;
				return true;
			}
		}
		return false;
	}

	public int size() {
		return vehiclesInTunnel;
	}
}
