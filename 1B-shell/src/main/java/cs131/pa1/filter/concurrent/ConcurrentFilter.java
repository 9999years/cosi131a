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

import cs131.pa1.filter.Filter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public abstract class ConcurrentFilter extends Filter implements Runnable {
	public static final int IO_QUEUE_SIZE = 16;
	protected boolean done = false;

	protected BlockingQueue<String> input;
	protected BlockingQueue<String> output;

	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}

	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter) {
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null) {
				this.output = new ArrayBlockingQueue<>(IO_QUEUE_SIZE); // connects filters
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}

	public Filter getNext() {
		return next;
	}

	@Override
	public void run() {
		try {
 			this.process(); // process the input as soon as it is received
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // some thread was interrupted or killed, halt process
		}
		done();
	}

	public void process() throws InterruptedException {
		while (!prev.isDone() || !input.isEmpty()) {
			// TODO this can cause problems if the previous thread
			// finishes between the loop entry and this line WHILE writing
			// no output
			String line = input.take(); // wait for input
			String processedLine = processLine(line);
			if (processedLine != null) {
				output.put(processedLine);
			}
		}
	}

	protected abstract String processLine(String line);
	// our changes start here

	protected void done() {
		done = true;
		if (input != null) {
			input.clear();
		}
	}

	@Override
	public boolean isDone() {
		return done;
	}
}
