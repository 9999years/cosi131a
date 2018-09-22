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

public class ShellState {
	private Path workingDirectory;

	public ShellState() {
		workingDirectory = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
	}

	public Path getWorkingDirectory() {
		return workingDirectory;
	}

	public void setWorkingDirectory(String relativePath) {
		replaceWorkingDirectory(workingDirectory.resolve(relativePath)
				.normalize());
	}

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
	 * @param relativePath
	 * @return an absolute path
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
}
