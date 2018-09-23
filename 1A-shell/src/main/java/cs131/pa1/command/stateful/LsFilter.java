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

package cs131.pa1.command.stateful;

import cs131.pa1.Arguments;
import cs131.pa1.filter.Message;
import cs131.pa1.filter.sequential.SequentialOutputFilter;
import cs131.pa1.filter.sequential.SequentialREPL;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LsFilter extends SequentialOutputFilter {
	public LsFilter(Arguments args) {
		super(args);
	}

	@Override
	protected boolean preprocess() {
		return ensureNoArgs() && ensureNoInput();
	}

	@Override
	public void process() {
		if (!preprocess()) {
			return;
		}
		var entries = new File(SequentialREPL.state.getWorkingDirectory().toString()).list();
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
