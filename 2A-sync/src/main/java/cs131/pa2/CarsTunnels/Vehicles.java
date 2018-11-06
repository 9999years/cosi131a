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

import java.util.ArrayList;
import java.util.Collection;

public class Vehicles extends ArrayList<Vehicle> {
	public Vehicles() {
		super(BasicTunnel.MAX_CAPACITY);
	}

	public Vehicles(Collection<? extends Vehicle> collection) {
		super(collection);
	}

	public boolean containsAmbulance() {
		return stream().anyMatch(v -> v instanceof Ambulance);
	}
}
