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

import java.io.File;

public class LsFilter extends ConcurrentOutputFilter {
	public LsFilter(Arguments args) {
		super(args);
		ensureNoArgs();
	}

	@Override
	public void process() {
		if (!ensureNoInput()) {
			return;
		}
		var entries = new File(SequentialREPL.cwd.getWorkingDirectory().toString()).list();
		if (entries == null) {
			// this is very bad...
			error(Message.DIRECTORY_NOT_FOUND);
			return;
		}
		for(var entry : entries) {
			outputln(entry);
		}
	}
}