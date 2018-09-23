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

package cs131.pa1.command;

import cs131.pa1.Arguments;
import cs131.pa1.filter.sequential.SequentialOutputFilter;

public class HelpFilter extends SequentialOutputFilter {
	public HelpFilter(Arguments args) {
		super(args);
	}

	@Override
	protected boolean preprocess() {
		return ensureNoInput();
	}

	@Override
	public void process() {
		if (!preprocess()) {
			return;
		}
		outputln("A simple shell. Available commands:");
		outputln("    cat        Print file(s) to the terminal");
		outputln("    exit       Exits the shell");
		outputln("    grep       Search lines of input for a substring");
		outputln("    help       Prints this help");
		outputln("    ls         List files and directories in current directory");
		outputln("    pwd        Prints working directory");
		outputln("    uniq       Unique lines of input");
		outputln("    wc         Counts words, lines, and characters of input");
		outputln("    >          Redirect input to a file");
	}
}
