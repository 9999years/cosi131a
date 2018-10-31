package cs131.pa2.CarsTunnels;

import cs131.pa2.Abstract.Direction;
import cs131.pa2.Abstract.Tunnel;
import cs131.pa2.Abstract.Vehicle;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Each tunnel has only one lane, so at any given time all vehicles must
 * be traveling in the same direction.
 * • Only three cars may be inside a tunnel at any given time.
 * • Only one sled may be inside a tunnel at any given time.
 * • Cars and sleds cannot share a tunnel.
 */
public class  	BasicTunnel extends Tunnel {
	// absolute maximum vehicle capacity of the tunnel
	public static final int MAX_CAPACITY = 3;

	// Direction vehicles in tunnel currently travelling
	private Direction direction = Direction.NORTH;
	// Type of vehicle currently using the tunnel
	private VehicleType vehicleType = VehicleType.Car;
	// the vehicles currently in the tunnel; this represents a horrible little
	// fixed-size array-set
	private Vehicle[] vehicles = new Vehicle[MAX_CAPACITY];
	private int vehiclesInTunnel = 0;

	public BasicTunnel(String name) {
		super(name);
	}

	@Override
	public synchronized boolean tryToEnterInner(Vehicle vehicle) {
		if (!canEnter(vehicle)) {
			return false;
		}
		// should never return false, but just to be safe
		return add(vehicle);
	}

	@Override
	public synchronized void exitTunnelInner(Vehicle vehicle) {
		if (!remove(vehicle)) {
			// something has gone horribly wrong (or the caller used a bad
			// vehicle...?)
			throw new IllegalStateException("Attempted to remove unknown vehicle from tunnel");
		}
	}

	/**
	 * adds a vehicle to the tunnel, performing NO checks for correctness
	 * @param vehicle the vehicle to add
	 * @return whether or not a vehicle was added
	 */
	private boolean add(Vehicle vehicle) {
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
	 * @return true if a vehicle was removed
	 */
	private boolean remove(Vehicle vehicle) {
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

	/**
	 * the number of vehicles of this tunnel's current type (vehicleType)
	 * that can enter this tunnel; e.g. if there are two cars in this
	 * tunnel, returns 2. If there is 1 sled, returns 0, etc. If there's
	 * no vehicles in the tunnel, returns at least 1
	 */
	private int remainingSlots() {
		return vehicleType.maxInTunnel - vehiclesInTunnel;
	}

	private boolean canEnter(Vehicle vehicle) {
		if (vehiclesInTunnel == 0) {
			direction = vehicle.getDirection();
			vehicleType = vehicleType.from(vehicle);
			return true;
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

}
