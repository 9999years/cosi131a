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
import java.util.Iterator;
import java.util.Spliterator;

import static java.util.Collections.addAll;

public class Jobs implements Iterable<Job> {
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
			sb.append("\t")
				.append(i)
				.append(". ")
				.append(job)
				.append("\n");
			i++;
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
		jobs.removeIf(Job::isDone);
	}

	/**
	 * note for user-friendly reasons (to match up with the numbers
	 * provided by toPrettyString()) the indexes used by this are 1-based
	 * rather than 0-based
	 * @param index
	 * @return
	 */
	public Job get(int index) {
		return jobs.get(index - 1);
	}

	public int size() {
		return jobs.size();
	}

	public boolean add(Job job) {
		return jobs.add(job);
	}

	public Job set(int index, Job element) {
		return jobs.set(index - 1, element);
	}

	public Job remove(int index) {
		return jobs.remove(index - 1);
	}

	@Override
	public Iterator<Job> iterator() {
		return jobs.iterator();
	}

	@Override
	public Spliterator<Job> spliterator() {
		return jobs.spliterator();
	}
}
