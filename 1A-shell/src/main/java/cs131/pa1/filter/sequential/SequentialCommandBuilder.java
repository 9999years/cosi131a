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

import cs131.pa1.command.Cat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	protected static void linkFilters(List<SequentialFilter> filters) {
		// TODO we need "terminating" and "initializing" filter classes
		// that wrap System.in and System.out
		// var itr = filters.listIterator();
		// if (itr.hasNext()) {
		// 	var fst = itr.next();
		// 	fst.setPrevFilter(System.in);
		// }
	}
}
