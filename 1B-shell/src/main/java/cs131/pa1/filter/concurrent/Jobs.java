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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;

public class Jobs extends AbstractList<Job> {
	private ArrayList<Job> jobs;

	public Jobs(Collection<Job> jobs) {
		this(jobs.size());
		addAll(jobs);
	}

	public Jobs(int size) {
		jobs = new ArrayList<>(size);
	}

	public Jobs() {
		jobs = new ArrayList<>();
	}

	public String toPrettyString() {
		StringBuilder sb = new StringBuilder();
		int i = 1;
		for (Job job : this) {
			sb.append("  ")
				.append(i)
				.append(". ")
				.append(job)
				.append("\n");
		}
		return sb.toString();
	}

	/**
	 * kills the specified job
	 * @param index
	 */
	public void kill(int index) {
		get(index).kill();
	}

	/**
	 * removes all done jobs from this list
	 */
	public void removeDone() {
		removeIf(Job::isDone);
	}

	/**
	 * note for user-friendly reasons (to match up with the numbers
	 * provided by toPrettyString()) the indexes used by this are 1-based
	 * rather than 0-based
	 * @param index
	 * @return
	 */
	@Override
	public Job get(int index) {
		return jobs.get(index - 1);
	}

	@Override
	public int size() {
		return jobs.size();
	}
}
