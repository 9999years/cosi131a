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

package cs131.pa2.CarsTunnels;

import cs131.pa2.Abstract.Direction;
import cs131.pa2.Abstract.Vehicle;

/**
 * An ambulance is a high priority vehicle
 */
public class Ambulance extends Vehicle{

	public Ambulance (String name, Direction direction) {
		super(name, direction);
	}

	@Override
	protected int getDefaultSpeed() {
		return 9;
	}

	@Override
    public String toString() {
        return String.format("%s AMBULANCE %s", super.getDirection(), super.getName());
    }
}
