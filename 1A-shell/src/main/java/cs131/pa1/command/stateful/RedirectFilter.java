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

package cs131.pa1.command.stateful;

import cs131.pa1.Arguments;
import cs131.pa1.filter.Message;
import cs131.pa1.filter.sequential.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class RedirectFilter extends SequentialInputFilter {
	public static final String NAME = ">";
	private PrintStream outFile;

	public RedirectFilter(Arguments args) {
		super(args);
		if (ensureOneArg()) {
			try {
				outFile = new PrintStream(
					new File(SequentialREPL
							.state
							.absolutePath(args.get(0))
							.toString()));
			} catch (FileNotFoundException e) {
				error(Message.FILE_NOT_FOUND);
			}
		}
	}

	@Override
	public void process() {
		if (outFile != null) {
			super.process();
		} else {
			// drain input
			input.clear();
		}
	}

	@Override
	protected String processLine(String line) {
		outFile.print(line);
		return null;
	}
}
