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
import java.util.stream.*;

/**
 * A pipeline
 */
public class SequentialFilterChain extends ArrayList<SequentialFilter> {
	public static final int DEFAULT_CHAIN_SIZE = 4;

	public SequentialFilterChain(Collection<? extends SequentialFilter> filters) {
		super(filters);
	}

	SequentialFilterChain(int size) {
		super(size);
	}

	SequentialFilterChain() {
		super(DEFAULT_CHAIN_SIZE);
	}

	public void process() {
		SequentialCommandBuilder.linkFilters(this);
		for (SequentialFilter filter : this) {
			filter.process();
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
}
