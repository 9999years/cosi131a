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

import java.util.ArrayList;
import java.util.List;

/**
 * a filter which collects its input into an internal list;
 * useful for testing. a terminal operation!
 */
public class CollectionFilter extends GoodSequentialFilter {
	private List<String> acc = new ArrayList<>();

	@Override
	protected String processLine(String line) {
		acc.add(line);
		return null;
	}

	/**
	 * collects and yields its input; possibly calls process()
	 * @return its input
	 */
	public List<String> collect() {
		if (!isDone()) {
			process();
		}
		return acc;
	}
}
