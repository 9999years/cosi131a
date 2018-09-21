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

import cs131.pa1.filter.sequential.SequentialOutputFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Cat extends SequentialOutputFilter {
	public static final String FILE_NOT_FOUND = "Could not open %s for reading";
	private List<File> files;

	public Cat(List<String> args) {
		files = new ArrayList<>(args.size());
		for (String arg : args) {
			files.add(new File(arg));
		}
	}

	/**
	 * Attempt to open a buffered reader
	 * @param file the file to open
	 * @return Optional.of the reader if opening was successful,
	 * Optional.empty otherwise
	 */
	private static Optional<BufferedReader> open(File file) {
		try {
			var is = new FileInputStream(file);
			var br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			return Optional.of(br);
		} catch (FileNotFoundException e) {
			return Optional.empty();
		}
	}

	private static Optional<Stream<String>> lines(File file) {
		return open(file).map(BufferedReader::lines);
	}

	/**
	 * Gets a stream of the files lines OR a stream describing the error
	 * if the file could not be opened
	 * @param file
	 * @return
	 */
	private static Stream<String> fileOutput(File file) {
		return lines(file).orElseGet(
				() -> Stream.of(String.format(FILE_NOT_FOUND, file.getName())));
	}

	@Override
	public void process() {
		files.stream().flatMap(Cat::fileOutput).forEach(output::add);
	}
}
