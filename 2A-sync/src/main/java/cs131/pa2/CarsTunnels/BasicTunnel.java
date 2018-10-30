package cs131.pa2.CarsTunnels;

import cs131.pa2.Abstract.Direction;
import cs131.pa2.Abstract.Tunnel;
import cs131.pa2.Abstract.Vehicle;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;

/**
 * Each tunnel has only one lane, so at any given time all vehicles must
 * be traveling in the same direction.
 * • Only three cars may be inside a tunnel at any given time.
 * • Only one sled may be inside a tunnel at any given time.
 * • Cars and sleds cannot share a tunnel.
 */
public class BasicTunnel extends Tunnel {
	// Direction vehicles in tunnel currently travelling
	private Direction direction = Direction.NORTH;
	// Type of vehicle currently using the tunnel
	private VehicleType vehicleType = VehicleType.Car;
	// max 3 vehicles in the tunnel at a time
	private Queue<Vehicle> vehicles = new ArrayDeque<>(3);

	public BasicTunnel(String name) {
		super(name);
	}

	@Override
	public boolean tryToEnterInner(Vehicle vehicle) {
		return false;
	}

	@Override
	public void exitTunnelInner(Vehicle vehicle) {

	}

	/**
	 * the number of vehicles of this tunnel's current type (vehicleType)
	 * that can enter this tunnel; e.g. if there are two cars in this
	 * tunnel, returns 2. If there is 1 sled, returns 0, etc. If there's
	 * no vehicles in the tunnel, returns at least 1
	 * @return
	 */
	private int remainingSlots() {
		return vehicleType.maxInTunnel - vehicles.size();
	}

	private boolean canEnter(Vehicle vehicle) {
		if (!vehicleType.isInstance(vehicle)) {
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
