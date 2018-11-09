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

package cs131.pa2.Abstract;

import cs131.pa2.Abstract.Log.EventType;
import cs131.pa2.Abstract.Log.Log;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A Vehicle is a Runnable which enters tunnels. You must subclass
 * Vehicle to customize its behavior (e.g., Car and Sled).
 * <p>
 * When you start a thread which runs a Vehicle, the Vehicle will
 * immediately begin trying to enter the tunnel or tunnels passed into
 * its constructor by calling tryToEnter on each Tunnel instance. As
 * long as tryToEnter returns false (indicating that the Vehicle did
 * not enter that tunnel), the Vehicle will keep trying. This is
 * called busy-stopped.
 * <p>
 * In addition to recreating the constructors, the only method that
 * you must override in Vehicle subclasses is getDefaultSpeed. This
 * instance method is called from the private init method, and the
 * integer that it returns is used as the speed for the vehicle.
 */
public abstract class Vehicle implements Runnable {
	private String name;
	private Direction direction;
	private Collection<Tunnel> tunnels;
	private int priority;
	private int speed;
	private Log log;

	// ADDED FIELDS
	/**
	 * milliseconds a vehicle will need to pass through the tunnel
	 */
	private long remainingTime;

	/**
	 * when the vehicle started progressing through the tunnel; used to
	 * calculate how much time is remaining
	 */
	private Instant startTime;

	/**
	 * the thread this vehicle belongs to
	 */
	private Thread thread;

	/**
	 * the tunnel this vehicle is in
	 */
	private Tunnel tunnel;

	private ReentrantLock lock = new ReentrantLock();
	/**
	 * wait condition if this vehicle is stopped in a tunnel
	 */
	private Condition stopped = lock.newCondition();

	/**
	 * Initialize a Vehicle; called from Vehicle constructors.
	 */
	private void init(String name, Direction direction,
					  int priority, Log log) {
		this.name = name;
		this.direction = direction;
		this.priority = 0;
		this.speed = getDefaultSpeed();
		this.log = log;
		this.tunnels = new ArrayList<Tunnel>();

		if (this.speed < 0 || this.speed > 9) {
			throw new RuntimeException("Vehicle has invalid speed");
		}
	}

	/**
	 * Override in a subclass to determine the speed of the
	 * vehicle.
	 * <p>
	 * Must return a number between 0 and 9 (inclusive). Higher
	 * numbers indicate greater speed. The faster a vehicle, the less
	 * time it will spend in a tunnel.
	 */
	protected abstract int getDefaultSpeed();

	/**
	 * Create a Vehicle with default priority that can cross one of
	 * a collection of tunnels.
	 *
	 * @param name      The name of this vehicle to be displayed in the
	 *                  output.
	 * @param direction The side of the tunnel being entered.
	 */
	public Vehicle(String name, Direction direction, Log log) {
		init(name, direction, 0, log);
	}

	public Vehicle(String name, Direction direction) {
		this(name, direction, Tunnel.DEFAULT_LOG);
	}

	/**
	 * Sets this vehicle's speed - used for preemptive priority scheduler test
	 *
	 * @param speed the new speed to be set (0 to 9)
	 */
	public void setSpeed(int speed) {
		if (this.speed < 0 || this.speed > 9) {
			throw new RuntimeException("Invalid speed: " + speed);
		}
		this.speed = speed;
	}

	/**
	 * Sets this vehicle's priority - used for priority scheduling
	 *
	 * @param priority The new priority (between 0 and 4 inclusive)
	 */
	public final void setPriority(int priority) {
		if (priority < 0 || priority > 4) {
			throw new RuntimeException("Invalid priority: " + priority);
		}
		this.priority = priority;
	}

	/**
	 * Returns the priority of this vehicle
	 *
	 * @return This vehicle's priority.
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Returns the name of this vehicle
	 *
	 * @return The name of this vehicle
	 */
	public final String getName() {
		return name;
	}

	public String toString() {
		return String.format("%s VEHICLE %s", this.direction, this.name);
	}

	public final void addTunnel(Tunnel newTunnel) {
		this.tunnels.add(newTunnel);
	}

	public final void addTunnel(Collection<Tunnel> newTunnels) {
		this.tunnels.addAll(newTunnels);
	}

	/**
	 * Find and cross through one of the tunnels.
	 * <p>
	 * When a thread is run, it keeps looping through its collection
	 * of available tunnels until it succeeds in entering one of
	 * them. Then, it will call doWhileInTunnel (to simulate doing
	 * some work inside the tunnel, i.e., that it takes time to cross
	 * the tunnel), then exit that tunnel.
	 */
	public final void run() {
		// Loop over all tunnels repeated until we can enter one, then
		// think inside the tunnel, exit the tunnel, and leave this
		// entire method.
		while (true) {
			for (Tunnel tunnel : tunnels) {
				if (tunnel.tryToEnter(this)) {
					doWhileInTunnel();
					tunnel.exitTunnel(this);
					this.log.addToLog(this, EventType.COMPLETE);
					return; // done, so leave the whole function
				}
			}
		}
	}

	/**
	 * Returns the direction of this vehicle
	 *
	 * @return the direction of this vehicle
	 */
	public final Direction getDirection() {
		return direction;
	}

	/**
	 * This is what your vehicle does while inside the tunnel to
	 * simulate taking time to "cross" the tunnel. The faster your
	 * vehicle is, the less time this will take.
	 */
	public final void doWhileInTunnel() {
		thread = Thread.currentThread();
		remainingTime = ((10 - speed) * 100);
		waitRemainingTime();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + Objects.hashCode(this.name);
		hash = 23 * hash + Objects.hashCode(this.direction);
		hash = 23 * hash + this.speed;
		hash = 23 * hash + this.priority;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Vehicle other = (Vehicle) obj;
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (this.direction != other.direction) {
			return false;
		}
		if (this.speed != other.speed) {
			return false;
		}
		if (this.priority != other.priority) {
			return false;
		}
		return true;
	}

	// ADDED METHODS

	public void pullOver() {
		// calculate remaining time
		remainingTime = ChronoUnit.MILLIS.between(startTime, Instant.now());
		startTime = null;
		lock.lock();
		waitRemainingTime();
		if (lock.isHeldByCurrentThread()) {
			lock.unlock();
		}
	}

	public void interrupt() {
		Objects.requireNonNull(thread, toString() + " has no thread!");
		thread.interrupt();
	}

	public Lock lockAndInterrupt() {
		lock.lock();
		interrupt();
		return lock;
	}

	/**
	 * Waits for the time remaining in remainingTime, saving state to
	 * gracefully pull over / restart as needed
	 */
	private void waitRemainingTime() {
		try {
			startTime = Instant.now();
			Thread.sleep(remainingTime);
		} catch (InterruptedException e) {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
			pullOver();
		}
	}

	public void setTunnel(Tunnel tunnel) {
		this.tunnel = tunnel;
	}

	public void setCondition(Condition condition) {
		this.stopped = condition;
	}

	public ReentrantLock getLock() {
		return lock;
	}
}
