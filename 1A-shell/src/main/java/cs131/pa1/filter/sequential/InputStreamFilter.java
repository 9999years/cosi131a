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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * filter which takes input from an input stream
 *
 * try: new InputStreamFilter(System.in)
 */
public class InputStreamFilter extends SequentialOutputFilter {
	private final BufferedReader bufferedReader;

	InputStreamFilter(InputStream inputStream) {
		super(Arguments.empty());
		this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	}

	@Override
	public void process() {
		bufferedReader.lines().forEach(output::add);
	}
}
