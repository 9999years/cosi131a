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

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LsFilter extends SequentialOutputFilter {
	public static final String NAME = "ls";
	private List<File> dirs;

	public LsFilter(String name, List<String> args) {
		if (args.isEmpty()) {
			args = List.of("");
		}
		dirs = new ArrayList<>(args.size());
		args.stream()
				.map(SequentialREPL.state::absolutePath)
				.map(Path::toString)
				.map(File::new)
				.forEach(dirs::add);
	}

	@Override
	public void process() {
		dirs.stream()
				.map(File::list)
				.map(Optional::ofNullable)
				.map(fs -> fs.orElseGet(() -> new String[] {
						Message.DIRECTORY_NOT_FOUND.with_parameter(NAME)}))
				.flatMap(Arrays::stream)
				.forEach(output::add);
	}
}
