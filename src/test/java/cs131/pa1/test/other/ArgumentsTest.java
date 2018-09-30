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

package cs131.pa1.test.other;

import cs131.pa1.Arguments;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ArgumentsTest {
	@Test
	public void simple() {
		var ls = new Arguments("ls");
		assertEquals("ls", ls.getCommand());
		assertEquals(List.of(), ls);

		var cat = new Arguments("cat xyz.txt abc.log");
		assertEquals("cat", cat.getCommand());
		assertEquals(List.of("xyz.txt", "abc.log"), cat);
	}

	@Test
	public void empty() {
		var empty = Arguments.empty();
		assertEquals("", empty.getCommand());
		assertEquals("", empty.getCommandLine());
		assertEquals(List.of(), empty);
	}
}
