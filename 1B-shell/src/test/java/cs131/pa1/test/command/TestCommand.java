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

package cs131.pa1.test.command;

import cs131.pa1.filter.concurrent.CollectionFilter;
import cs131.pa1.filter.concurrent.ConcurrentFilter;
import cs131.pa1.filter.concurrent.ConcurrentFilterChain;

import java.util.List;

public class TestCommand {
	/**
	 * collects and returns a String array of all of cmd's output
	 * @param cmd
	 * @return
	 */
	public static String[] testCommand(ConcurrentFilter cmd) {
		var coll = new CollectionFilter();
		new ConcurrentFilterChain(List.of(cmd, coll)).process();
		return coll.collect().toArray(new String[0]);
	}
}
