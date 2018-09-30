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
import cs131.pa1.command.Commands;

import java.util.regex.Pattern;
import java.util.stream.*;

public class SequentialCommandBuilder {
	private static final Pattern subCommandBoundary = Pattern.compile("\\||(?=>)");
	public static final String PIPE = "|";

	public static SequentialFilterChain createFiltersFromCommand(String command) {
		return new SequentialFilterChain(
				splitToSubCommands(command)
				.map(SequentialCommandBuilder::constructFilterFromSubCommand)
                .collect(Collectors.toUnmodifiableList()));
	}

	private static Stream<String> splitToSubCommands(String command) {
		return subCommandBoundary.splitAsStream(command);
	}

	private static GoodSequentialFilter constructFilterFromSubCommand(String subCommand) {
		return Commands.forName(new Arguments(subCommand));
	}
}
