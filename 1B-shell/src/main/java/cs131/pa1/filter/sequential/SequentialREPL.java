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

import cs131.pa1.WorkingDirectory;
import cs131.pa1.filter.Message;
import cs131.pa1.filter.concurrent.ConcurrentCommandBuilder;

import java.util.Scanner;

public class SequentialREPL {
	public static WorkingDirectory cwd;
	/**
	 * disgusting global variable set by ExitFilter because System.exit fucks with tests
	 */
	public static boolean shouldExit;

	public static void main(String[] args) {
		shouldExit = false;
		cwd = new WorkingDirectory();
		System.out.print(Message.WELCOME.toString());
		var in = new Scanner(System.in);
		while (!shouldExit) {
			System.out.print(Message.NEWCOMMAND.toString());
			var line = "exit";
			if (in.hasNextLine()) {
				line = in.nextLine();
			}
			ConcurrentCommandBuilder.createFiltersFromCommand(line)
					.printOutput().process();
		}
		System.out.flush();
	}
}