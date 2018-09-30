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

package cs131.pa1.filter.sequential;

import cs131.pa1.Arguments;
import cs131.pa1.filter.Message;

import java.util.ArrayDeque;

/**
 * a SequentialFilter class with added utility
 * 1. this.output is never null, i.e. writing to output from the
 * constructor is OK
 * 2. there are several argument and state validation methods available
 * (see VALIDATION below)
 * 3. an instance has knowledge of its invocation, i.e. a command invoked
 * as "cat whatever.txt" will be aware via this.args that it was invoked
 * with the name "cat" and one argument ("whatever.txt")
 *
 * @see Arguments
 * @see SequentialOutputFilter
 * @see SequentialInputFilter
 */
public abstract class GoodSequentialFilter extends SequentialFilter {
	protected Arguments args;
	/**
	 * has this command failed?
	 */
	protected boolean ok = true;

	public GoodSequentialFilter() {
		output = new ArrayDeque<>();
	}

	public GoodSequentialFilter(Arguments args) {
		this();
		this.args = args;
	}

	protected void error(Message message) {
		output.add(errorString(message));
		notOk();
	}

	public boolean isOk() {
		return ok;
	}

	protected void notOk() {
		ok = false;
		if (input != null) {
			input.clear();
		}
	}

	protected void outputln(String line) {
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
		if (input != null && prev instanceof GoodSequentialFilter
				&& ((GoodSequentialFilter) prev).isATTY()) {
			return true;
		} else {
			error(Message.CANNOT_HAVE_INPUT);
			return false;
		}
	}

	protected boolean ensureNotFirst() {
		if (input == null || prev == null
				|| (prev instanceof GoodSequentialFilter
				&& ((GoodSequentialFilter) prev).isATTY())) {
			error(Message.REQUIRES_INPUT);
			return false;
		} else {
			return true;
		}
	}

	protected boolean ensureIsLast() {
		if ((output == null && next == null)
				|| (next instanceof GoodSequentialFilter
				&& ((GoodSequentialFilter) next).isATTY())) {
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
	 */
	protected boolean isATTY() {
		return false;
	}
}
