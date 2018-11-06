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

package cs131.pa2.Test;

import cs131.pa2.Abstract.Direction;
import cs131.pa2.Abstract.Vehicle;
import cs131.pa2.CarsTunnels.Car;
import cs131.pa2.CarsTunnels.Vehicles;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VehiclesTest {

	@Test
	public void iterator() {
		Vehicles vehicles = new Vehicles();
		vehicles.add(new Car("1", Direction.NORTH));
		vehicles.add(new Car("2", Direction.SOUTH));
		vehicles.add(new Car("3", Direction.SOUTH));

		Iterator<Vehicle> itr = vehicles.iterator();
		assertEquals(new Car("1", Direction.NORTH), itr.next());
		assertEquals(new Car("2", Direction.SOUTH), itr.next());
		assertEquals(new Car("3", Direction.SOUTH), itr.next());
		assertFalse(itr.hasNext());

		assertTrue(vehicles.remove(new Car("2", Direction.SOUTH)));

		itr = vehicles.iterator();
		assertEquals(new Car("1", Direction.NORTH), itr.next());
		assertEquals(new Car("3", Direction.SOUTH), itr.next());
		assertFalse(itr.hasNext());
	}
}
