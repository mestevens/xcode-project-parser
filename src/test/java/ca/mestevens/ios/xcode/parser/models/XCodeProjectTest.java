package ca.mestevens.ios.xcode.parser.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ca.mestevens.ios.xcode.parser.exceptions.InvalidObjectFormatException;

import static org.testng.Assert.assertEquals;

public class XCodeProjectTest {
	
	private XCodeProject xcodeProject;
	private String pbxprojPathString = Thread.currentThread().getContextClassLoader().getResource("project.pbxproj").getPath().toString();
	
	@BeforeMethod
	public void setUp() throws InvalidObjectFormatException {
		xcodeProject = new XCodeProject(pbxprojPathString);
	}
	
	@AfterMethod
	public void tearDown() {
		xcodeProject = null;
	}
	
	@Test
	public void testToString() throws IOException {
		String projectString = new String(Files.readAllBytes(Paths.get(pbxprojPathString)));
		assertEquals(xcodeProject.toString() + "\n", projectString);
	}

}
