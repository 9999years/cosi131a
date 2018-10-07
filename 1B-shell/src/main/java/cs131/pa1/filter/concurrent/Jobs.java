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

import java.util.ArrayList;
import java.util.Collection;

public class Jobs extends ArrayList<Job> {
	public Jobs(Collection<Job> jobs) {
		this(jobs.size());
		addAll(jobs);
	}

	public Jobs(int size) {
		super(size);
	}

	public Jobs() {
		super();
	}

	public String toPrettyString() {
		StringBuilder sb = new StringBuilder();
		for (Job job : this) {
			sb.append("\t")
				.append(job.pid)
				.append(". ")
				.append(job)
				.append("\n");
		}
		return sb.toString();
	}

	/**
	 * kills the specified job
	 */
	public boolean kill(int pid) {
		if (isEmpty()) {
			return false;
		}
		var itr = iterator();
		for (Job job = itr.next(); itr.hasNext(); job = itr.next()) {
			if (pid == job.pid) {
				job.kill();
				itr.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 * removes all done jobs from this list
	 */
	public void removeDone() {
		removeIf(Job::isDone);
	}
}
