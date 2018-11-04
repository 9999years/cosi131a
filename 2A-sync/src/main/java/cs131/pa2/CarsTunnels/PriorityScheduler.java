package cs131.pa2.CarsTunnels;

import cs131.pa2.Abstract.Log.Log;
import cs131.pa2.Abstract.Tunnel;
import cs131.pa2.Abstract.Vehicle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * When a Vehicle calls tryToEnter(vehicle) on a PriorityScheduler instance, the
 * vehicle must wait if:
 * • the vehicle's priority is not the highest
 * • there are no tunnels the vehicle can enter
 * <p>
 * Otherwise, the vehicle successfully enters one of the tunnels.
 * <p>
 * To “exit” a vehicle, make sure to call tunnel.exitTunnel(v)
 * • After a vehicle has successfully exited a tunnel, the waiting vehicles should
 * be signaled to retry to enter a tunnel. Note that the vehicles with highest
 * priority should be allowed to enter.
 * • Use condition variables to avoid busy waiting when the car cannot find a
 * tunnel to enter. Make sure the use of the condition variables is safe.
 */
public class PriorityScheduler extends Tunnel {
	private List<Tunnel> tunnels = new ArrayList<>();
	private PriorityVehicles vehicles = new PriorityVehicles();

	public PriorityScheduler(String name) {
		this(name, Collections.emptyList());
	}

	public PriorityScheduler(String name, Collection<? extends Tunnel> c) {
		this(name, c, Tunnel.DEFAULT_LOG);
	}

	public PriorityScheduler(String name, Collection<? extends Tunnel> c,
							 Log log) {
		super(name, log);
		tunnels.addAll(c);
	}

	@Override
	public boolean tryToEnterInner(Vehicle vehicle) {
		// check priority
		if (vehicles.isHighestPriority(vehicle)) {
			for (Tunnel tunnel : tunnels) {
				if (tunnel.tryToEnter(vehicle)) {
					// remove vehicle from waiting queue and associate with a tunnel
					vehicles.tunnelWaiting(vehicle, tunnel);
					return true;
				}
			}
		}
		// no tunnel could take it OR its priority wasn't high enough; now it's
		// waiting
		vehicles.addWaiting(vehicle);
		return false;
	}

	@Override
	public void exitTunnelInner(Vehicle vehicle) {
		vehicles.exitTunnel(vehicle);
	}

}
