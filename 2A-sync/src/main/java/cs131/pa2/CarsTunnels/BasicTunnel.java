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
import cs131.pa2.Abstract.Tunnel;
import cs131.pa2.Abstract.Vehicle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Each tunnel has only one lane, so at any given time all vehicles must
 * be traveling in the same direction.
 * • Only three cars may be inside a tunnel at any given time.
 * • Only one sled may be inside a tunnel at any given time.
 * • Cars and sleds cannot share a tunnel.
 */
public class BasicTunnel extends Tunnel {
	// rough estimate of vehicle capacity of the tunnel
	public static final int MAX_CAPACITY = 4;

	// Direction vehicles in tunnel currently travelling
	private Direction direction = Direction.NORTH;
	// Type of vehicle currently using the tunnel
	private VehicleType vehicleType = VehicleType.Car;
	// the vehicles currently in the tunnel; this represents a horrible little
	// fixed-size array-set
	private Vehicles vehicles = new Vehicles();

	private boolean isPreemptive = false;
	private Collection<ReentrantLock> locks = new ArrayList<>(MAX_CAPACITY);

	public BasicTunnel(String name) {
		super(name);
	}

	@Override
	public synchronized boolean tryToEnterInner(Vehicle vehicle) {
		if (!canEnter(vehicle)) {
			return false;
		}
		if (isPreemptive && vehicle instanceof Ambulance && locks.isEmpty()) {
			interruptNonEssential();
		}
		// should never return false, but just to be safe
		return vehicles.add(vehicle);
	}

	@Override
	public synchronized void exitTunnelInner(Vehicle vehicle) {
		if (!vehicles.remove(vehicle)) {
			// something has gone horribly wrong (or the caller used a bad
			// vehicle...?)
			throw new IllegalStateException("Attempted to remove unknown vehicle from tunnel");
		}
	}

	/**
	 * the number of vehicles of this tunnel's current type (vehicleType)
	 * that can enter this tunnel; e.g. if there are two cars in this
	 * tunnel, returns 2. If there is 1 sled, returns 0, etc. If there's
	 * no vehicles in the tunnel, returns at least 1
	 */
	private int remainingSlots() {
		return vehicleType.maxInTunnel - vehicles.size();
	}

	boolean canEnter(Vehicle vehicle) {
		if (vehicles.size() == 0) {
			direction = vehicle.getDirection();
			vehicleType = VehicleType.from(vehicle);
			return true;
		} else if (isPreemptive && vehicle instanceof Ambulance && !containsAmbulance()) {
			return true;
		} else if (isPreemptive && containsAmbulance()) {
			return false;
		} else if (!vehicleType.isInstance(vehicle)) {
			return false;
		} else if (direction != vehicle.getDirection()) {
			return false;
		} else if (remainingSlots() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean contains(Vehicle v) {
		return vehicles.contains(v);
	}

	public boolean containsAmbulance() {
		return vehicles.stream().anyMatch(v -> v instanceof Ambulance);
	}

	private Stream<Vehicle> nonEssentialVehicles() {
		return vehicles.stream().filter(v -> !(v instanceof Ambulance));
	}

	public void interruptNonEssential() {
		locks.clear();
		locks.addAll(nonEssentialVehicles()
				.map(Vehicle::lockAndInterrupt)
				.collect(Collectors.toList()));
	}

	public void restartNonEssential() {
		locks.forEach(Lock::unlock);
		locks.clear();
	}

	protected void isPreemptive() {
		isPreemptive = true;
	}

	public int size() {
		return vehicles.size();
	}
}
