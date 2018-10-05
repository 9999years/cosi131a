/*
 * Copyright 2018 Rebecca Turner (rebeccaturner@brandeis.edu)
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

package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;

import java.util.LinkedHashSet;
import java.util.Scanner;

public class ConcurrentREPL {
	static String currentWorkingDirectory;
	/**
	 * our job-list needs to have fast random insertion and deletion as
	 * well as a predictable iteration order; we avoid the O(n) amortized
	 * complexity of list deletions with a hashset structure while keeping
	 * iteration predictable with the linked list the LinkedHashSet
	 * maintains internally
	 */
	private static LinkedHashSet<ConcurrentFilter> jobs;

	public static void main(String[] args) {
		currentWorkingDirectory = System.getProperty("user.dir");
		jobs = new LinkedHashSet<>();
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		while (true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			if (command.equals("exit")) {
				break;
			} else if (!command.trim().equals("")) {
				//building the filters list from the command
				ConcurrentFilter filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				while (filterlist != null) {
					var thread = new Thread(filterlist);
					filterlist = (ConcurrentFilter) filterlist.getNext();
				}
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}

}
