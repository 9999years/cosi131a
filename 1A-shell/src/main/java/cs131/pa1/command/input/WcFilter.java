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

package cs131.pa1.command.input;

import cs131.pa1.Arguments;
import cs131.pa1.filter.sequential.SequentialInputFilter;

/**
 * Counts lines, words, and characters in a given input.
 */
public class WcFilter extends SequentialInputFilter {
	
	int lines;
	int words;
	int chars;

	public WcFilter(Arguments args) {
		super(args);
		ensureNoArgs();
	}

	@Override
	public void process() {
		if (ensureNotFirst()) {
			super.process();
			if (isOk()) {
				outputln(lines + " " + words + " " + chars);
			}
		}
	}

	@Override
	protected String processLine(String line) {
		lines++;
		words += line.split("\\s+").length;
		chars += line.replace("\n", "").length();
		return null;
	}
}
