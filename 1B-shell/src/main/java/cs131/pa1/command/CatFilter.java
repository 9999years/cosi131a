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

package cs131.pa1.command;

import cs131.pa1.Arguments;
import cs131.pa1.filter.Message;
import cs131.pa1.filter.concurrent.ConcurrentOutputFilter;
import cs131.pa1.filter.sequential.SequentialREPL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

public class CatFilter extends ConcurrentOutputFilter {
	private Stream<File> files;

	public CatFilter(Arguments args) {
		super(args);
		ensureSomeArgs();
		files = args.stream()
				.map(SequentialREPL.cwd::absolutePath)
				.map(Path::toString)
				.map(File::new);
	}

	@Override
	public void process() {
		if (!ensureNoInput()) {
			return;
		}
		var inputStreams = new ArrayList<FileInputStream>(args.size());
		for (var file : (Iterable<File>) files::iterator) {
			try {
				inputStreams.add(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				error(Message.FILE_NOT_FOUND);
				return;
			}
		}

		inputStreams.stream()
				.map(InputStreamReader::new)
				.map(BufferedReader::new)
				.flatMap(BufferedReader::lines)
				.map(l -> l + "\n")
				.forEach(this::output);
	}
}
