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

import cs131.pa2.Abstract.Vehicle;

import java.util.Arrays;

public enum VehicleType {
	Car(3, new Class[]{Car.class, Ambulance.class}),
	Sled(1, new Class[]{Sled.class});

	public final int maxInTunnel;
	public final Class[] classes;

	VehicleType(int maxInTunnel, Class[] classes) {
		this.maxInTunnel = maxInTunnel;
		this.classes = classes;
	}

	public boolean isInstance(Object o) {
		return Arrays.stream(classes).anyMatch(c -> c.isInstance(o));
	}

	public static VehicleType from(Vehicle vehicle) {
		return Arrays.stream(VehicleType.values())
				.filter(vt -> vt.isInstance(vehicle))
				.findFirst()
				.orElse(VehicleType.Car);
	}
}
