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
				this.output = new ArrayBlockingQueue<>(IO_QUEUE_SIZE);
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}

	public Filter getNext() {
		return next;
	}

	public void process() throws InterruptedException {
		while (!prev.isDone()) {
			String line = input.take();
			String processedLine = processLine(line);
			if (processedLine != null) {
				output.put(processedLine);
			}
		}
	}

	protected void done() {
		done = true;
		input.clear();
	}

	@Override
	public boolean isDone() {
		return done;
	}

	protected abstract String processLine(String line);

	@Override
	public void run() {
		// stub method, lin-ye's filling this in
	}
}
