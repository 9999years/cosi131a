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

import cs131.pa1.Arguments;
import cs131.pa1.command.stateful.CatFilter;
import cs131.pa1.command.stateful.CdFilter;
import cs131.pa1.command.stateful.LsFilter;
import cs131.pa1.command.stateful.PwdFilter;
import cs131.pa1.command.stateful.RedirectFilter;
import cs131.pa1.command.input.GrepFilter;
import cs131.pa1.command.input.UniqFilter;
import cs131.pa1.command.input.WcFilter;
import cs131.pa1.filter.sequential.EmptyFilter;
import cs131.pa1.filter.sequential.GoodSequentialFilter;

import java.lang.reflect.InvocationTargetException;
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
	private static final Map<String, Class<? extends GoodSequentialFilter>>
		commands = Map.ofEntries(
			Map.entry("cat",   CatFilter.class),
			Map.entry("cd",    CdFilter.class),
			Map.entry("grep",  GrepFilter.class),
			Map.entry("help",  HelpFilter.class),
			Map.entry("exit",  ExitFilter.class),
			Map.entry("ls",    LsFilter.class),
			Map.entry("pwd",   PwdFilter.class),
			Map.entry(">",     RedirectFilter.class),
			Map.entry("uniq",  UniqFilter.class),
			Map.entry("wc",    WcFilter.class),
			Map.entry("",      EmptyFilter.class)
	);

	/**
	 * gets the Class instance for a given command; for "cat" this will
	 * return Class&lt;cs131.pa1.command.stateful.CatFilter&gt;. Note that
	 * this method, unlike forName, does not instantiate the class
	 * @param command
	 */
	public static Class<? extends GoodSequentialFilter> classForName(String command) {
		return commands.getOrDefault(command, CommandNotFoundFilter.class);
	}

	/**
	 * Creates a new SequentialFilter for the specified command name with
	 * the specified arguments; instantiates the command too
	 * @return
	 */
	public static GoodSequentialFilter forName(Arguments args) {
		try {
			return classForName(args.getCommand())
					.getConstructor(Arguments.class)
					.newInstance(args);
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
