/*
 * Copyright 2018 Rebecca Turner (rebeccaturner@brandeis.edu)
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

package cs131.pa1.filter.concurrent;

import java.util.List;

public class Job {
	private String commandLine;
	private List<Thread> threads;

	public Job(String commandLine, List<Thread> threads) {
		this.commandLine = commandLine;
		this.threads = threads;
	}

	public boolean isDone() {
		return threads.stream().noneMatch(Thread::isAlive);
	}

	public void kill() {
		for (Thread thread : threads) {
			thread.interrupt();
		}
	}

	@Override
	public String toString() {
		return commandLine;
	}

	@Override
	public int hashCode() {
		return threads.hashCode();
	}
}
