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

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConcurrentREPL {
	static String currentWorkingDirectory;
	/**
	 * map from command line strings to threads
	 * <p>
	 * our job-list needs to have fast random insertion and deletion as
	 * well as a predictable iteration order; we avoid the O(n) amortized
	 * complexity of list deletions with a hashset structure while keeping
	 * iteration predictable with the linked list the LinkedHashMap
	 * maintains internally
	 */
	private static Jobs jobs;

	private static void kill(String command) {
		String[] args = command.split("\\s+");
		if (args.length < 2) {
			System.out.print(Message.REQUIRES_PARAMETER.with_parameter(command));
		} else if (args.length > 2) {
			System.out.println(Message.INVALID_PARAMETER.with_parameter(command));
		}
		try {
			int job = Integer.valueOf(args[1]);
			jobs.kill(job);
		} catch (IllegalArgumentException
				| IndexOutOfBoundsException e) {
			System.out.println(Message.INVALID_PARAMETER.with_parameter(command));
		}
	}

	public static void main(String[] args) {
		currentWorkingDirectory = System.getProperty("user.dir");
		jobs = new Jobs();
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		while (true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine().trim();
			boolean background = ConcurrentCommandBuilder.isBackgroundCommand(command);
			if (command.equals("exit")) {
				break;
			} else if (command.equals("repl_jobs")) {
				jobs.removeDone();
				System.out.print(jobs.toPrettyString());
			} else if (command.startsWith("kill")) {
				// TODO implement this
				kill(command);
			} else if (!command.isEmpty()) {
				//building the filters list from the command
				var filterList = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				if (filterList == null) {
					continue;
				}
				List<Thread> threads = filterList.stream()
						.map(t -> new Thread(t, t.toString()))
						.peek(Thread::start)
						.collect(Collectors.toList());
				if (background) {
					jobs.add(new Job(command, threads));
				} else {
					for (var thread : threads) {
						try {
							thread.join();
						} catch (InterruptedException e) {
							// TODO what the hell would happen here?
							e.printStackTrace();
							break;
						}
					}
				}
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}

}
