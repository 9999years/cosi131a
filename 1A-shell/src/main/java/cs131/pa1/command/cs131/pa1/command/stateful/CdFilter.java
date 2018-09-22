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

import cs131.pa1.filter.sequential.SequentialOutputFilter;
import cs131.pa1.filter.sequential.SequentialREPL;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class CdFilter extends SequentialOutputFilter {
	private Path newPath;

	public static final String WRONG_ARGS = "cd: Too many arguments";
	public static final String DIR_NOT_FOUND = "cd: %s: no such file or directory";

	CdFilter(List<String> args) {
		if (args.size() > 1) {
			output.add(WRONG_ARGS);
		} else if (args.isEmpty()) {
			newPath = SequentialREPL.state.absolutePath("");
		} else {
			// 1 argument
			newPath = SequentialREPL.state.absolutePath(args.get(0));
		}
	}

	@Override
	public void process() {
		try {
			SequentialREPL.state.setWorkingDirectory(newPath.toRealPath().toString());
		} catch (IOException e) {
			output.add(String.format(
					DIR_NOT_FOUND,
					newPath.relativize(SequentialREPL.state.getWorkingDirectory()).toString()
			));
		}
	}
}
