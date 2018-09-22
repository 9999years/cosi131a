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

package cs131.pa1;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * command tokenizer
 */
public class Arguments extends AbstractList<String> {
	private static final Arguments empty = new Arguments("", List.of());
	private final String commandLine;
	private String command;
	private List<String> args;

	public Arguments(String commandLine) {
		this.commandLine = commandLine.trim();
		if (this.commandLine.isEmpty()) {
			command = "";
			args = List.of();
		}
	}

	public Arguments(String command, List<String> args) {
		this.command = command;
		this.args = new ArrayList<>(args);
		this.commandLine = commandLine();
	}

	/**
	 * reconstitutes the original command-line from a command and argument
	 * list; may not be equivalent to the original depending on spacing
	 * @return
	 */
	private String commandLine() {
		return command + " " + String.join(" ", args);
	}

	private void tokenize() {
		// tokenize, splitting by spaces, ignoring empty tokens ("")
		var toks = Arrays.stream(commandLine.split("\\s+"))
				.filter(s -> s.length() > 0)
				.collect(Collectors.toUnmodifiableList());
		// first token is the command
		command = toks.get(0);
		args = toks.subList(1, toks.size());
	}

	public String getCommandLine() {
		return commandLine;
	}

	/**
	 * gets the name of the invoked command; for "cat some.txt other.txt"
	 * this would return "cat". lazily populated and only calculated once;
	 * the command isn't parsed until this method or getArgs is accessed
	 * @return
	 */
	public String getCommand() {
		if (command == null) {
			tokenize();
		}
		return command;
	}

	@Override
	public String get(int index) {
		if (args == null) {
			tokenize();
		}
		return args.get(index);
	}

	@Override
	public int size() {
		if (args == null) {
			tokenize();
		}
		return args.size();
	}

	public static Arguments empty() {
		return empty;
	}
}
