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

package cs131.pa1.command;

import cs131.pa1.command.cs131.pa1.command.stateful.CatFilter;
import cs131.pa1.command.cs131.pa1.command.stateful.CdFilter;
import cs131.pa1.command.cs131.pa1.command.stateful.LsFilter;
import cs131.pa1.command.cs131.pa1.command.stateful.PwdFilter;
import cs131.pa1.command.cs131.pa1.command.stateful.RedirectFilter;
import cs131.pa1.command.input.GrepFilter;
import cs131.pa1.command.input.UniqFilter;
import cs131.pa1.command.input.WcFilter;
import cs131.pa1.filter.sequential.SequentialFilter;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * utility class for command-related functions
 */
public class Commands {
	private Commands() {}

	/**
	 * map associating user-facing command names with the class that
	 * implements their behavior
	 */
	private static final Map<String, Class<? extends SequentialFilter>> commands = Map.of(
			"cat",   CatFilter.class,
			"cd",    CdFilter.class,
			"grep",  GrepFilter.class,
			"exit",  ExitFilter.class,
			"ls",    LsFilter.class,
			"pwd",   PwdFilter.class,
			">",     RedirectFilter.class,
			"uniq",  UniqFilter.class,
			"wc",    WcFilter.class
	);

	/**
	 * gets the Class instance for a given command; for "cat" this will
	 * return Class&lt;cs131.pa1.command.cs131.pa1.command.stateful.CatFilter&gt;. Note that this method,
	 * unlike forName, does not instantiate the class
	 * @param command
	 */
	public static Class<? extends SequentialFilter> classForName(String command) {
		return commands.getOrDefault(command, CommandNotFoundFilter.class);
	}

	/**
	 * Creates a new SequentialFilter for the specified command name with
	 * the specified arguments; instantiates the command too
	 *
	 * @param command The command to create
	 * @param args    The arguments to pass in
	 * @return
	 */
	public static SequentialFilter forName(String command, List<String> args) {
		try {
			return classForName(command)
					.getConstructor(String.class, List.class)
					.newInstance(command, args);
		} catch (InstantiationException
				| IllegalAccessException
				| InvocationTargetException
				| NoSuchMethodException e) {
			// should never happen
			e.printStackTrace();
			return null;
		}
	}
}
