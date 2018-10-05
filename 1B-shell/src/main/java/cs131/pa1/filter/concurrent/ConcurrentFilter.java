package cs131.pa1.filter.concurrent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import cs131.pa1.Arguments;
import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;

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
	protected Queue<String> input;
	protected Queue<String> output;

	protected Arguments args;
	/**
	 * has this command failed?
	 */
	protected boolean ok = true;

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
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null) {
				this.output = new LinkedList<String>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}

	public Filter getNext() {
		return next;
	}

	public void process() {
		while (!input.isEmpty()) {
			String line = input.poll();
			String processedLine = processLine(line);
			if (processedLine != null) {
				output.add(processedLine);
			}
		}
	}

	@Override
	public void run() {
		process();
	}

	@Override
	public boolean isDone() {
		return input.size() == 0 && (prev == null || prev.isDone());
	}

	protected abstract String processLine(String line);

	// ADDED METHODS

	protected void error(Message message) {
		output.add(errorString(message));
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

	protected void outputln(String line) {
		// TODO go through uses of output and ensure thread-safety
		output.add(line + "\n");
	}

	protected void output(String string) {
		output.add(string);
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
	protected boolean ensureNoArgs() {
		if (args.isEmpty()) {
			return true;
		} else {
			error(Message.INVALID_PARAMETER);
			return false;
		}
	}

	protected boolean ensureSomeArgs() {
		if (args.isEmpty()) {
			error(Message.REQUIRES_PARAMETER);
			return false;
		} else {
			return true;
		}
	}

	protected boolean ensureOneArg() {
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
	protected boolean ensureNoInput() {
		if (input != null && prev instanceof ConcurrentFilter
				&& ((ConcurrentFilter) prev).isATTY()) {
			return true;
		} else {
			error(Message.CANNOT_HAVE_INPUT);
			return false;
		}
	}

	protected boolean ensureNotFirst() {
		if (input == null || prev == null
				|| (prev instanceof ConcurrentFilter
				&& ((ConcurrentFilter) prev).isATTY())) {
			error(Message.REQUIRES_INPUT);
			return false;
		} else {
			return true;
		}
	}

	protected boolean ensureIsLast() {
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
