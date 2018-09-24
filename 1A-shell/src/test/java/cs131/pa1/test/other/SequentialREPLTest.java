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

package cs131.pa1.test.other;

import cs131.pa1.filter.Message;
import cs131.pa1.filter.sequential.SequentialREPL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class SequentialREPLTest {
	InputStream stdin;
	PrintStream stdout;
	PrintStream stderr;
	ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	public static final String RESOURCES = "src/test/resources";

	public static String loc(Object o) {
		return String.format("%x", System.identityHashCode(o));
	}

	public String debugString() {
		return "STDIN:  " + loc(System.in) + " (" + loc(stdin) + ")"
				+ "\nSTDOUT: " + loc(System.out) + " (" + loc(stdout) + ")"
				+ "\nSTDERR: " + loc(System.err) + " (" + loc(stderr) + ")";
	}

	public void rule() {
		System.out.println("---------------");
	}

	public void debug() {
		System.out.println(debugString());
	}

	@Before
	public void saveStreams() {
		stdin = System.in;
		stdout = System.out;
		stderr = System.err;
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void restoreStreams() {
		System.setIn(stdin);
		System.setOut(stdout);
		System.setErr(stderr);
	}

	public void assertOutput(String expected){
		String result = outContent.toString().replace("\r", "");
		expected = Message.WELCOME
				+ expected
				+ Message.NEWCOMMAND
				+ Message.GOODBYE;
		assertEquals(expected, result);
	}

	String lineJoin(List<String> lines) {
		return String.join("\n", lines);
	}

	public void test(List<String> expected, List<String> input) {
		// set input of line-joined input
		var inStr = "cd " + RESOURCES + "\n" + lineJoin(input);
		System.setIn(new ByteArrayInputStream(inStr.getBytes()));
		// run shell
		SequentialREPL.main(new String[0]);
		// apply a "> " to every line in expected output and join with \n before
		// comparing
		var expectStr = Message.NEWCOMMAND
				+ String.join("",
				(Iterable<String>) expected.stream()
					.map(l -> Message.NEWCOMMAND + l)
					::iterator);
		assertOutput(expectStr);
	}

	@Test
	public void cat1() {
		test(
				List.of(""),
				List.of("cd ls")
		);
	}

	@Test
	public void ls1() {
		test(
				List.of("",
						"abc\n" +
						"README.txt\n" +
						"whatever\n" +
						"x\n" +
						"y.txt\n"),
				List.of("cd ls", "ls")
		);
	}

	@Test
	public void cat2() {
		test(
			List.of("hello\nworld\n"),
			List.of("cat hello-world.txt")
		);
	}


}
