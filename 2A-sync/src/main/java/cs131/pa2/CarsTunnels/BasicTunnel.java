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
public class BasicTunnel extends Tunnel {
	// absolute maximum vehicle capacity of the tunnel
	public static final int MAX_CAPACITY = 3;

	// Direction vehicles in tunnel currently travelling
	private Direction direction = Direction.NORTH;
	// Type of vehicle currently using the tunnel
	private VehicleType vehicleType = VehicleType.Car;
	// the vehicles currently in the tunnel
	private Vehicles vehicles;

	public BasicTunnel(String name) {
		super(name);
	}

	@Override
	public synchronized boolean tryToEnterInner(Vehicle vehicle) {
		if (!canEnter(vehicle)) {
			return false;
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

	private boolean canEnter(Vehicle vehicle) {
		if (vehicles.size() == 0) {
			direction = vehicle.getDirection();
			vehicleType = VehicleType.from(vehicle);
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
