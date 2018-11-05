package cs131.pa2.CarsTunnels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import java.util.concurrent.locks.*;

import cs131.pa2.Abstract.Tunnel;
import cs131.pa2.Abstract.Vehicle;
import cs131.pa2.Abstract.Log.Log;

/**
 * TODO
 */
public class PreemptivePriorityScheduler extends Tunnel{
	// Synchronization variables
	private final Lock lock = new ReentrantLock();
	private final Condition shouldEnter = lock.newCondition();
	
	// State variables
	private int waitingAmbulances = 0;
	private int activeAmbulances = 0;
	
	private List<Tunnel> tunnels = new ArrayList<>();
	private PriorityVehicles vehicles = new PriorityVehicles();
	
	public PreemptivePriorityScheduler(String name) {
		this(name, Collections.emptyList());
	}

	public PreemptivePriorityScheduler(String name, Collection<? extends Tunnel> c) {
		this(name, c, Tunnel.DEFAULT_LOG);
	}

	public PreemptivePriorityScheduler(String name, Collection<? extends Tunnel> c, Log log) {
		super(name, log);
		tunnels.addAll(c);
	}
	
	@Override
	public boolean tryToEnterInner(Vehicle vehicle) {
		// check priority
		if (vehicles.isHighestPriority(vehicle)) {
			for (Tunnel tunnel : tunnels) {
				if (tunnel.tryToEnter(vehicle)) {
					if (vehicle instanceof Ambulance) {
						
					}					
					// vehicle entered tunnel, remove it from the waiting queue
					vehicles.tunnelWaiting(vehicle, tunnel);
					return true;
				}
			}
		}
		// vehicle didn't enter a tunnel, add it to waiting queue
		vehicles.addWaiting(vehicle);
		return false;
	}

	@Override
	public void exitTunnelInner(Vehicle vehicle) {
		vehicles.exitTunnel(vehicle);
	}
	
	// returns true if a vehicle should not be able to access a tunnel
	public boolean noAccess() {
		return (activeAmbulances > 0);
	}
	
	public void startAmbulance() {
		lock.lock();
		try {
			waitingAmbulances++;
			while (noAccess()) {
				shouldEnter.await();
			}
			waitingAmbulances--;
			activeAmbulances++;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void stopAmbulance() {
		lock.lock();
		try {
			activeAmbulances--;
			assert(activeAmbulances == 0);
			shouldEnter.signal();
		} finally {
			lock.unlock();
		}
	}
	
}
