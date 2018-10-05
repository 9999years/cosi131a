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

package cs131.pa1.command;

import cs131.pa1.Arguments;
import cs131.pa1.filter.concurrent.ConcurrentInputFilter;

import java.util.HashSet;

/**
 * not how unix uniq works!!!!!!!!!!!!!!!
 */
public class UniqFilter extends ConcurrentInputFilter {
	private HashSet<String> lines;

	public UniqFilter(Arguments args) {
		super(args);
		ensureNoArgs();
	}

	@Override
	public void process() {
		lines = new HashSet<>(input.size());
		super.process();
	}

	@Override
	protected String processLine(String line) {
		return lines.add(line) ? line : null;
	}
}
