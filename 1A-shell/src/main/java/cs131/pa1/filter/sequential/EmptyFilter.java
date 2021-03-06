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

/**
 * a filter with no input or output
 *
 * isDone will always return true
 */
public class EmptyFilter extends GoodSequentialFilter {
	public EmptyFilter() {
		super(Arguments.empty());
		// input = new ArrayDeque<>();
		// output field is initialized automatically when needed
	}

	/**
	 * only provided for interface-compatibility with cs131.pa1.command.stateful
	 * @param args
	 */
	public EmptyFilter(Arguments args) {
		this();
	}

	@Override
	public void process() {
		// do nothing!
	}

	@Override
	protected String processLine(String line) {
		return null;
	}

	@Override
	protected boolean isATTY() {
		return true;
	}
}
