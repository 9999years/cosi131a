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
import cs131.pa1.command.CatFilter;
import cs131.pa1.filter.Message;
import org.junit.Test;

import static cs131.pa1.test.command.TestCommand.testCommand;
import static org.junit.Assert.*;

public class CatFilterTest {
	static CatFilter cat(String args) {
		return new CatFilter(new Arguments("cat " + args));
	}

	@Test
	public void regular() {
		assertArrayEquals(
				new String[] {"first line of a.txt\n",
					"second line of a.txt\n"},
				testCommand(cat("src/test/resources/a.txt")));
	}

	@Test
	public void multipleFiles() {
		assertArrayEquals(
				new String[] {"first line of a.txt\n",
						"second line of a.txt\n",
						"first line of b.txt\n"},
				testCommand(cat(
						"src/test/resources/a.txt"
						+ " src/test/resources/b.txt")));
	}

	@Test
	public void nonexistentFile() {
		var args = "nonexistent-file";
		assertArrayEquals(
				new String[] {Message.FILE_NOT_FOUND.with_parameter("cat " + args)},
				testCommand(cat(args)));
	}
}
