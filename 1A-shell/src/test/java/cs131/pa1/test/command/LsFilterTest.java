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

import cs131.pa1.Arguments;
import cs131.pa1.command.stateful.LsFilter;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static cs131.pa1.test.command.TestCommand.testCommand;
import static org.junit.Assert.*;

public class LsFilterTest {
	static LsFilter ls(String args) {
		return new LsFilter(new Arguments("ls " + args));
	}

	@Test
	public void simple() {
		var listed = Set.of(testCommand(ls("src/test/resources/ls")));
		var expected = new String[] {
				"whatever", "abc", "x", "y.txt", "README.txt"};

		// same size
		assertEquals(expected.length, listed.size());

		// same contents
		for (var expect : expected) {
			assertTrue("expected `" + expect + "` in ls but not present",
					listed.contains(expect));
		}
	}
}
