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

import cs131.pa2.Abstract.Direction;
import cs131.pa2.Abstract.Vehicle;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * these tests BREAK other tests due to the weird logging framework. watch out!
 */
public class BasicTunnelTest {

	private BasicTunnel tunnel(boolean preemptive, Vehicle[] vehicles) {
		BasicTunnel tunnel = new BasicTunnel("test-tunnel");
		if (preemptive) {
			tunnel.isPreemptive();
		}
		for (Vehicle vehicle : vehicles) {
			vehicle.addTunnel(tunnel);
		}
		return tunnel;
	}

	private Vehicle canEnter(boolean preemptive,
							 Vehicle[] vehicles) {
		BasicTunnel tunnel = new BasicTunnel("test-tunnel");
		if (preemptive) {
			tunnel.isPreemptive();
		}
		for (Vehicle vehicle : vehicles) {
			if (!tunnel.canEnter(vehicle)) {
				return vehicle;
			}
			vehicle.addTunnel(tunnel);
			new Thread(vehicle).start();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private void allCanEnter(boolean preemptive, Vehicle[] vehicles) {
		assertNull("Checking that all vehicles in "
						+ Arrays.toString(vehicles) + " can enter a "
						+ (preemptive ? "preemptive" : "") + " tunnel",
				canEnter(preemptive, vehicles));
	}

	private boolean containsAmbulance(boolean preemptive, Vehicle[] vehicles) {
		BasicTunnel tunnel = tunnel(preemptive, vehicles);
		return tunnel.containsAmbulance();
	}

	// buggy...
	public void containsAmbulance() {
		assertTrue(containsAmbulance(true, new Vehicle[] {
				new Ambulance("oiasdjgoi", Direction.SOUTH)
		}));

		assertFalse(containsAmbulance(true, new Vehicle[] {
				new Car("car", Direction.SOUTH),
				new Car("car2", Direction.SOUTH)
		}));

		assertTrue(containsAmbulance(false, new Vehicle[] {
				new Car("car", Direction.SOUTH),
				new Car("car2", Direction.SOUTH),
				new Ambulance("ambu", Direction.SOUTH)
		}));
	}

	public void canEnter() {
		assertEquals("ambu cant enter tunnel w/ ambu",
				new Ambulance("ambu2", Direction.NORTH),
				canEnter(true, new Vehicle[]{
						new Car("xxx", Direction.SOUTH),
						new Ambulance("ambu1", Direction.NORTH),
						new Ambulance("ambu2", Direction.NORTH),
		}));
	}
}
