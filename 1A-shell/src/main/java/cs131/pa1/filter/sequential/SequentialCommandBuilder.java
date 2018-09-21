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

import java.util.List;
import java.util.Queue;

public class SequentialCommandBuilder {
	public static List<SequentialFilter> createFiltersFromCommand(String command) {
		return null;
	}

	private static SequentialFilter determineFinalFilter(String command) {
		return null;
	}

	private static String adjustCommandToRemoveFinalFilter(String command) {
		return null;
	}

	private static SequentialFilter constructFilterFromSubCommand(String subCommand) {
		return null;
	}

	public static void linkFilters(List<SequentialFilter> filters) {
		filters.stream().reduce(new EmptyFilter(), (p, n) -> {
			p.setNextFilter(n);
			return n;
		});
	}
}
