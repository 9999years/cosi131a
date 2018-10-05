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

import cs131.pa1.Arguments;
import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;

import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * a Filter class with added utility
 * 1. this.output is never null, i.e. writing to output from the
 * constructor is OK (this.input might be null before run(), however!)
 * 2. there are several argument and state validation methods available
 * (see VALIDATION below)
 * 3. an instance has knowledge of its invocation, i.e. a command invoked
 * as "cat whatever.txt" will be aware via this.args that it was invoked
 * with the name "cat" and one argument ("whatever.txt")
 *
 * @see Arguments
 * @see ConcurrentOutputFilter
 * @see ConcurrentInputFilter
 */
public abstract class ConcurrentFilter extends Filter implements Runnable {
	public static final int IO_QUEUE_SIZE = 64;
	protected BlockingQueue<String> input;
	protected BlockingQueue<String> output;

	protected Arguments args;
	/**
	 * has this command failed?
	 */
	protected boolean ok = true;

	/**
	 * is this command finished?
	 */
	protected boolean done = false;

	public ConcurrentFilter() {
		output = new ArrayBlockingQueue<>(IO_QUEUE_SIZE);
	}

	public ConcurrentFilter(Arguments args) {
		this();
		this.args = args;
	}

	// GIVEN METHODS

	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}

	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter) {
			ConcurrentFilter concurrentNext = (ConcurrentFilter) nextFilter;
			this.next = concurrentNext;
			concurrentNext.prev = this;
			if (this.output == null) {
				this.output = new ArrayBlockingQueue<>(IO_QUEUE_SIZE);
			}
			concurrentNext.input = this.output;
		} else {
			throw new IllegalArgumentException("Should not attempt to link dissimilar filter types.");
		}
	}

	public Filter getNext() {
		return next;
	}

	@Blocks
	public void process() throws InterruptedException {
		while (!prev.isDone()) {
			String line = input.take();
			String processedLine = processLine(line);
			if (processedLine != null) {
				output.put(processedLine);
			}
		}
	}

	@Override
	public void run() {
		try {
			process();
		} catch (InterruptedException e) {
			// TODO ??? what's the error-handling case here
			e.printStackTrace();
		}
		done();
	}

	@Override
	public boolean isDone() {
		return done;
	}

	protected void done() {
		done = true;
	}

	protected abstract String processLine(String line);

	// ADDED METHODS

	/**
	 * set ok to false and print an error message
	 * @param message the message to print
	 */
	@Blocks
	protected void error(Message message) throws InterruptedException {
		output.put(errorString(message));
		notOk();
	}

	public boolean isOk() {
		return ok;
	}

	/**
	 * sets this filter to be "not OK"
	 */
	protected void notOk() {
		ok = false;
		if (input != null) {
			input.clear();
		}
	}

	@Blocks
	protected void outputln(String line) throws InterruptedException {
		output.put(line + "\n");
	}

	@Blocks
	protected void output(String string) throws InterruptedException {
		output.put(string);
	}

	protected String errorString(Message message) {
		return message.with_parameter(this.args.getCommandLine());
	}


	// VALIDATION METHODS
	// these provide significant functionality:
	// 1. they print the proper error messages -- formatted correctly
	//    using this.args
	// 2. they set ok = false to allow conditional quitting later in the
	//    execution cycle
	// 3. they return a boolean indicating if validation succeeded for use
	//    in if() statements
	//
	// these methods are either argument-based or state-based
	// * argument-based methods analyse this.args and are OK to call in
	// the constructor
	// * state-based methods analyse input, output, or this filter's position
	// in a pipeline, and should only be called in process() or later

	// ARGUMENT-BASED VALIDATORS
	/**
	 * @return true if args are OK (no args present) false otherwise
	 */
	@Blocks
	protected boolean ensureNoArgs() throws InterruptedException {
		if (args.isEmpty()) {
			return true;
		} else {
			error(Message.INVALID_PARAMETER);
			return false;
		}
	}

	@Blocks
	protected boolean ensureSomeArgs() throws InterruptedException {
		if (args.isEmpty()) {
			error(Message.REQUIRES_PARAMETER);
			return false;
		} else {
			return true;
		}
	}

	@Blocks
	protected boolean ensureOneArg() throws InterruptedException {
		if (args.size() == 1) {
			return true;
		} else if (args.isEmpty()) {
			error(Message.REQUIRES_PARAMETER);
		} else {
			error(Message.INVALID_PARAMETER);
		}
		return false;
	}

	// STATE-BASED VALIDATORS
	@Blocks
	protected boolean ensureNoInput() throws InterruptedException {
		if (input != null && prev instanceof ConcurrentFilter
				&& ((ConcurrentFilter) prev).isATTY()) {
			return true;
		} else {
			error(Message.CANNOT_HAVE_INPUT);
			return false;
		}
	}

	@Blocks
	protected boolean ensureNotFirst() throws InterruptedException {
		if (input == null || prev == null
				|| (prev instanceof ConcurrentFilter
				&& ((ConcurrentFilter) prev).isATTY())) {
			error(Message.REQUIRES_INPUT);
			return false;
		} else {
			return true;
		}
	}

	@Blocks
	protected boolean ensureIsLast() throws InterruptedException {
		if ((output == null && next == null)
				|| (next instanceof ConcurrentFilter
				&& ((ConcurrentFilter) next).isATTY())) {
			return true;
		} else {
			error(Message.CANNOT_HAVE_OUTPUT);
			return false;
		}
	}

	/**
	 * does this filter represent a TTY like stdin or stdout?
	 * there's a few "fake" filters that don't represent commands but just
	 * i.e. print input to System.out or provide a noop to make a field
	 * not null. these will return isATTY() = true. "fake" filters
	 * include: OutputStreamFilter, CollectionFilter, EmptyFilter...
	 *
	 * no filters here *actually* really represent TTYs, but usage of this
	 * method matches usage of unix TTY-checking
	 */
	protected boolean isATTY() {
		return false;
	}
}
