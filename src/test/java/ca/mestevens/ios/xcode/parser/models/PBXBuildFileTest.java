package ca.mestevens.ios.xcode.parser.models;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ca.mestevens.ios.xcode.parser.exceptions.InvalidObjectFormatException;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertEquals;

public class PBXBuildFileTest {
	
	private final String testReference = "8A23808419F94F640025EFB1";
	private final String testReferenceComment = "main.m in Sources";
	private final String testIsa = "PBXBuildFile";
	private final String testFileRef = "8A23808319F94F640025EFB1";
	private final String testFileRefComment = "main.m";
	private final Map<String, String> testSettings = new HashMap<String, String>();
	
	private final String testBuildFileString = testReference + " /* " + testReferenceComment + " */ = {isa = " + testIsa + "; fileRef = " + testFileRef + " /* " + testFileRefComment + " */; };";

	@BeforeMethod
	public void setUp() {
		
	}
	
	@AfterMethod
	public void tearDown() {
		
	}
	
	@Test
	public void testCreateBasicBuildFile() throws InvalidObjectFormatException {
		PBXBuildFile buildFile = new PBXBuildFile(testBuildFileString);
		assertEquals(buildFile.getReference().getIdentifier(), testReference);
		assertEquals(buildFile.getReference().getComment(), testReferenceComment);
		assertEquals(buildFile.getIsa(), testIsa);
		assertEquals(buildFile.getFileRef().getIdentifier(), testFileRef);
		assertEquals(buildFile.getFileRef().getComment(), testFileRefComment);
		assertEquals(buildFile.getSettings(), testSettings);
	}
	
	@Test
	public void testToString() throws InvalidObjectFormatException {
		PBXBuildFile buildFile = new PBXBuildFile(testBuildFileString);
		assertEquals(buildFile.toString(), testBuildFileString);
	}

	@Test(expectedExceptions = InvalidObjectFormatException.class)
	public void testEmptyString() throws InvalidObjectFormatException {
		new PBXBuildFile("");
	}
	
	@Test(expectedExceptions = InvalidObjectFormatException.class)
	public void testNullString() throws InvalidObjectFormatException {
		new PBXBuildFile(null);
	}
	
	@Test
	public void testEquals() throws InvalidObjectFormatException {
		PBXBuildFile buildFileOne = new PBXBuildFile(testBuildFileString);
		PBXBuildFile buildFileTwo = new PBXBuildFile(testBuildFileString);
		assertTrue(buildFileOne.equals(buildFileTwo));
	}
	
	@Test
	public void testEqualsWithDifferentReference() throws InvalidObjectFormatException {
		PBXBuildFile buildFileOne = new PBXBuildFile(testBuildFileString);
		PBXBuildFile buildFileTwo = new PBXBuildFile(testBuildFileString.replace(testReference, "my-new-reference"));
		assertTrue(buildFileOne.equals(buildFileTwo));
	}
	
	@Test
	public void testEqualsWithDifferentFileReferences() throws InvalidObjectFormatException {
		PBXBuildFile buildFileOne = new PBXBuildFile(testBuildFileString);
		PBXBuildFile buildFileTwo = new PBXBuildFile(testBuildFileString.replace(testFileRef, "my-new-reference"));
		assertFalse(buildFileOne.equals(buildFileTwo));
	}
	
}
