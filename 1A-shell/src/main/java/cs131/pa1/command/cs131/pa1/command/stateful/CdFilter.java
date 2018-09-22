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

package cs131.pa1.command.cs131.pa1.command.stateful;

import cs131.pa1.filter.Message;
import cs131.pa1.filter.sequential.SequentialOutputFilter;
import cs131.pa1.filter.sequential.SequentialREPL;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class CdFilter extends SequentialOutputFilter {
	public static String NAME = "cd";
	private Path newPath;

	public CdFilter(String name, List<String> args) {
		if (args.isEmpty()) {
			newPath = SequentialREPL.state.absolutePath("");
		} else if (ensureOneArg(name, args)) {
			newPath = SequentialREPL.state.absolutePath(args.get(0));
		}
	}

	@Override
	public void process() {
		if (newPath != null) {
			try {
				// note that .toRealPath resolves symlinks
				SequentialREPL.state.setWorkingDirectory(newPath.toRealPath().toString());
			} catch (IOException e) {
				output.add(String.format(Message.DIRECTORY_NOT_FOUND.toString(), NAME));
			}
		}
	}
}
