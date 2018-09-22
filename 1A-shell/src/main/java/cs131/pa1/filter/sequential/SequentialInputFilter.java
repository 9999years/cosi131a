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

import cs131.pa1.filter.Message;

import java.util.List;

/**
 * a Filter which requires input. A SequentialInputFilter subclass
 * <i>MUST</i> call its super(name) method or empty-input-detection will
 * not work properly
 */
public abstract class SequentialInputFilter extends GoodSequentialFilter {
	protected String name = "invalid command";

	public SequentialInputFilter(String name) {
		this.name = name;
	}

	@Override
	public void process() {
		if (input.isEmpty()) {
			output.add(Message.REQUIRES_INPUT.with_parameter(name));
		}
		super.process();
	}
}
