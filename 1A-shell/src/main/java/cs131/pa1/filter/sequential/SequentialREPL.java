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

import java.util.Scanner;

public class SequentialREPL {
	
	public static WorkingDirectory cwd;
	public static boolean quit;

	public static void main(String[] args) {
		quit = false;
		cwd = new WorkingDirectory();
		System.out.print(Message.WELCOME.toString());
		Scanner input = new Scanner(System.in);
		while (!quit) {
			System.out.print(Message.NEWCOMMAND.toString());
			String line = "exit";
			if (input.hasNextLine()) {
				line = input.nextLine();
			}
			SequentialCommandBuilder.createFiltersFromCommand(line)
					.printOutput().process();
		}
	}
}