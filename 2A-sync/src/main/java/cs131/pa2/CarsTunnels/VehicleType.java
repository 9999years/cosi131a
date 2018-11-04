/*
 * Copyright 2018 Rebecca Turner (rebeccaturner@brandeis.edu)
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

public enum VehicleType {
	Car(3, Car.class),
	Sled(1, Sled.class);

	public final int maxInTunnel;
	public final Class clazz;

	VehicleType(int maxInTunnel, Class clazz) {
		this.maxInTunnel = maxInTunnel;
		this.clazz = clazz;
	}

	public boolean isInstance(Object o) {
		return this.clazz.isInstance(o);
	}

	public static VehicleType from(Vehicle vehicle) {
		for (VehicleType vt : VehicleType.values()) {
			if (vt.clazz.isInstance(vehicle)) {
				return vt;
			}
		}
		// unspecified vehicles are cars :)
		// (this line is a failsafe because Ambulance doesn't inherit from Car)
		return VehicleType.Car;
	}
}
