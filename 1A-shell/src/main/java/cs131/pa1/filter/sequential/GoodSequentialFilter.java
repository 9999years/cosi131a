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

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;

import java.util.ArrayDeque;
import java.util.List;

/**
 * a GoodSequentialFilter always has an output Queue but doesn't always
 * have an input
 */
public abstract class GoodSequentialFilter extends SequentialFilter {
	public GoodSequentialFilter() {
		output = new ArrayDeque<>();
	}

	/**
	 *
	 * @param name
	 * @param args
	 * @return true if args are OK (no args present) false otherwise
	 */
	protected boolean ensureNoArgs(String name, List<String> args) {
		if (args.isEmpty()) {
			return true;
		} else {
			output.add(Message.REQUIRES_PARAMETER.with_parameter(name));
			return false;
		}
	}

	protected boolean ensureOneArg(String name, List<String> args) {
		if (args.size() == 1) {
			return true;
		} else if (args.isEmpty()) {
			output.add(Message.REQUIRES_PARAMETER.with_parameter(name));
		} else {
			output.add(Message.INVALID_PARAMETER.with_parameter(name));
		}
		return false;
	}

	@Override
	public void setPrevFilter(Filter prevFilter) {
		// check to avoid infinite loops
		if (this.prev != prevFilter) {
			this.prev = prevFilter;
			prevFilter.setNextFilter(this);
		}
	}

	@Override
	public void setNextFilter(Filter nextFilter) {
		if (!(nextFilter instanceof GoodSequentialFilter)) {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
		var nextSequential = (GoodSequentialFilter) nextFilter;
		this.next = nextSequential;
		nextSequential.setPrevFilter(this);
		// join this.output with nextFilter.input
		nextSequential.input = output;
	}
}
