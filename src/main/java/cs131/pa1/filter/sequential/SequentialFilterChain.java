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
import java.util.Collection;

/**
 * A pipeline
 */
public class SequentialFilterChain extends ArrayList<GoodSequentialFilter> {
	public static final int DEFAULT_SIZE = 4;

	public SequentialFilterChain(Collection<? extends GoodSequentialFilter> filters) {
		super(filters);
	}

	SequentialFilterChain(int size) {
		super(size);
	}

	SequentialFilterChain() {
		super(DEFAULT_SIZE);
	}

	/**
	 * ends a pipeline prematurely by writing its output to System.out;
	 * intended for errors
	 */
	private void finish(GoodSequentialFilter filter) {
		filter.output.forEach(System.out::print);
	}

	public void process() {
		linkFilters();
		for (var filter : this) {
			if (filter.isOk()) {
				filter.process();
			}
			if (!filter.isOk()) {
				finish(filter);
				return;
			}
		}
	}

	/**
	 * prints output of this filter chain by adding a filter which
	 * redirects its input to System.out to the end of the chain.
	 * Returns this instance for fluent building approaches
	 */
	public SequentialFilterChain printOutput() {
		add(new OutputStreamFilter(System.out));
		return this;
	}

	protected void linkFilters() {
		stream().reduce(new EmptyFilter(), (p, n) -> {
			p.setNextFilter(n);
			return n;
		});
	}
}
