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

package cs131.pa1.test;



import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;
import cs131.pa1.filter.sequential.SequentialREPL;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class WorkingDirectoryTests {
       
    private static final String sep = Filter.FILE_SEPARATOR;
	
	@Test
	public void testBasicPwd(){
		testInput("pwd\nexit");
		SequentialREPL.main(null);
		String expectation = Message.NEWCOMMAND + "" + System.getProperty("user.dir") + "\n";
		assertOutput(expectation);
	}
	
	@Test
	public void testPwdWithChangedDirectory(){
		testInput("pwd\ncd src\npwd\nexit");
		SequentialREPL.main(null);
		String expectation = Message.NEWCOMMAND + "" + System.getProperty("user.dir");
		expectation += "\n" + Message.NEWCOMMAND + expectation + sep + "src\n";
		assertOutput(expectation);
	}
	
	@Test
	public void testCdNonExistentDirectory(){
		testInput("cd not-a-directory\nexit");
		SequentialREPL.main(null);
		String expectation = Message.NEWCOMMAND + "The directory specified by the command [cd not-a-directory] was not found.\n";
		assertOutput(expectation);
	}
	
	@Test
	public void testPwdWithCdDot(){
		testInput("pwd\ncd .\npwd\nexit");
		SequentialREPL.main(null);
		String expectation = Message.NEWCOMMAND + "" + System.getProperty("user.dir");
		expectation += "\n> " + expectation + "\n";
		assertOutput(expectation);
	}
	
	@Test
	public void testPwdWithCdDotDot(){
		testInput("pwd\ncd ..\npwd\nexit");
		SequentialREPL.main(null);
		String expectation = Message.NEWCOMMAND + "" + System.getProperty("user.dir");
		expectation += "\n> " + expectation.substring(0, expectation.lastIndexOf(sep)) + "\n";
		assertOutput(expectation);
	}
	
	@Test
	public void testLs(){
		testInput("cd dir1\ncd dir2\nls\nexit");
		SequentialREPL.main(null);
		Set<String> expected = new HashSet<String>();
		expected.add("dir3");
		expected.add("f2.txt");
		Set<String> output = new HashSet<String>(Arrays.asList(outContent.toString().replaceAll(Message.NEWCOMMAND.toString(), "")
				.replaceAll(Message.WELCOME.toString(), "").replaceAll(Message.GOODBYE.toString(), "").replaceAll(", ", "").split("\\s+")));
		output.remove("");
		assertEquals(expected, output);
	}
	
	@Test
	public void testMultiMoveDirectory(){
		testInput("cd dir1" + sep +"dir2" + sep +"dir3" + sep +"dir4\npwd\ncd ..\n pwd \n cd ..\n pwd \n cd ..\n pwd \n cd ..\n pwd\nexit");
		SequentialREPL.main(null);
		String originalLocation = System.getProperty("user.dir");
		
		String expectation = Message.NEWCOMMAND.toString() + Message.NEWCOMMAND.toString() + "" + originalLocation + sep +"dir1" + sep +"dir2" + sep +"dir3" + sep +"dir4\n" + 
							 Message.NEWCOMMAND + Message.NEWCOMMAND + "" + originalLocation + sep + "dir1" + sep + "dir2" + sep +"dir3\n" + 
							 Message.NEWCOMMAND + Message.NEWCOMMAND + "" + originalLocation + sep + "dir1" + sep +"dir2\n" + 
							 Message.NEWCOMMAND + Message.NEWCOMMAND + "" + originalLocation + sep + "dir1\n" + 
							 Message.NEWCOMMAND + Message.NEWCOMMAND + "" + originalLocation + "\n";
		assertOutput(expectation);
	}
	
	// Boilerplate, standard across test case files.
	
	private ByteArrayInputStream inContent;
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	
	public void testInput(String s){
		inContent = new ByteArrayInputStream(s.getBytes());
		System.setIn(inContent);
	}
	
	public void assertOutput(String expected){
		AllSequentialTests.assertOutput(expected, outContent);
	}
	
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
		System.setIn(null);
	    System.setOut(null);
	    System.setErr(null);
	}
}
