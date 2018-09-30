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

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * a working directory; java is "weird" with this (doesn't allow changing
 * user.dir) so we create a structure <i>initialized</i> from user.dir and
 * then mutated with Path.resolve and Path.normalize
 *
 * @see <a href="https://bugs.java.com/bugdatabase/view_bug.do?bug_id=4045688">
 *    JDK-4045688 : Add chdir or equivalent notion of changing working
 *    directory</a>
 */
public class WorkingDirectory {
	/**
	 * an ABSOLUTE PATH representing the current working directory
	 */
	private Path workingDirectory;

	public WorkingDirectory() {
		workingDirectory = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
	}

	/**
	 * gets the current working directory
	 */
	public Path getWorkingDirectory() {
		return workingDirectory;
	}

	/**
	 * mutates the current working directory represented by this object by
	 * resolving a relative path
	 */
	public void setWorkingDirectory(Path relativePath) {
		replaceWorkingDirectory(workingDirectory.resolve(relativePath)
				.normalize());
	}

	/**
	 * mutates the current working directory represented by this object by
	 * resolving a relative path
	 */
	public void setWorkingDirectory(String relativePath) {
		setWorkingDirectory(Paths.get(relativePath));
	}

	/**
	 * replaces the current working directory with an absolute path;
	 * verifies the path is absolute
	 */
	public void replaceWorkingDirectory(Path absolutePath) {
		// trust..........but verify
		if (!absolutePath.isAbsolute()) {
			absolutePath = absolutePath.toAbsolutePath();
		}
		this.workingDirectory = absolutePath;
	}

	public void replaceWorkingDirectory(String absolutePath) {
		replaceWorkingDirectory(Paths.get(absolutePath));
	}

	/**
	 * gets an absolute path from a relative path, taking into account the current working directory
	 * @param relativePath the path to resolve as absolute
	 * @return the relative path's absolute form
	 */
	public Path absolutePath(String relativePath) {
		var rel = Paths.get(relativePath);

		if (rel.isAbsolute()) {
			return rel;
		}

		return Paths.get(
				getWorkingDirectory().toString(),
				relativePath).normalize().toAbsolutePath();
	}

	@Override
	public String toString() {
		return workingDirectory.toString();
	}
}
