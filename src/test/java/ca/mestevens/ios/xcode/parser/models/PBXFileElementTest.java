package ca.mestevens.ios.xcode.parser.models;


import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ca.mestevens.ios.xcode.parser.exceptions.InvalidObjectFormatException;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertEquals;

@Test(groups = "automated")
public class PBXFileElementTest {
	
	private String testReference = "8A23809119F94F640025EFB1";
	private String testReferenceComment = "Base";
	private String testIsa = "PBXFileReference";
	private String testLastKnownFileType = "file.xib";
	private String testPath = "Base.lproj/LaunchScreen.xib";
	private String testSourceTree = "\"<group>\"";
	private String testName = "Base";
	
	private String testGroupReference = "8A23809B19F94F640025EFB1";
	private String testGroupReferenceComment = "Supporting Files";
	private String testGroupIsa = "PBXGroup";
	private CommentedIdentifier testGroupChild = new CommentedIdentifier("8A23809C19F94F640025EFB1", "Info.plist");
	private String testGroupName = "\"Supporting Files\"";
	private String testGroupSourceTree = "\"<group>\"";
	
	private String testFileReferenceObject = "\t\t" + testReference + " /* " + testReferenceComment + " */ = {isa = " + testIsa + "; lastKnownFileType = " + testLastKnownFileType + "; name = " + testName + "; path = " + testPath + "; sourceTree = " + testSourceTree + "; };";
	
	private String testGroupObject = "\t\t" + testGroupReference + " /* " + testGroupReferenceComment + " */ = {\n" +
			"\t\t\tisa = " + testGroupIsa + ";\n" +
			"\t\t\tchildren = (\n" +
			"\t\t\t\t" + testGroupChild.toString() + ",\n" +
			"\t\t\t);\n" +
			"\t\t\tname = " + testGroupName + ";\n" +
			"\t\t\tsourceTree = " + testGroupSourceTree + ";\n" +
			"\t\t};";
	
	@BeforeMethod
	public void setUp() {
		
	}
	
	@AfterMethod
	public void tearDown() {
		
	}
	
	@Test
	public void testBasicFileReference() throws InvalidObjectFormatException {
		PBXFileElement fileReference = new PBXFileElement(testFileReferenceObject);
		assertEquals(fileReference.getReference().getIdentifier(), testReference);
		assertEquals(fileReference.getReference().getComment(), testReferenceComment);
		assertEquals(fileReference.getIsa(), testIsa);
		assertEquals(fileReference.getLastKnownFileType(), testLastKnownFileType);
		assertEquals(fileReference.getName(), testName);
		assertEquals(fileReference.getPath(), testPath);
		assertEquals(fileReference.getSourceTree(), testSourceTree);
		assertNull(fileReference.getExplicitFileType());
		assertNull(fileReference.getChildren());
		assertNull(fileReference.getFileEncoding());
		assertNull(fileReference.getIncludeInIndex());
	}
	
	@Test
	public void testFileReferenceToString() throws InvalidObjectFormatException {
		PBXFileElement fileReference = new PBXFileElement(testFileReferenceObject);
		assertEquals(fileReference.toString(2), testFileReferenceObject);
	}
	
	@Test
	public void testBasicGroup() throws InvalidObjectFormatException {
		PBXFileElement groupFileReference = new PBXFileElement(testGroupObject);
		assertEquals(groupFileReference.getReference().getIdentifier(), testGroupReference);
		assertEquals(groupFileReference.getReference().getComment(), testGroupReferenceComment);
		assertEquals(groupFileReference.getIsa(), testGroupIsa);
		assertEquals(groupFileReference.getChildren().size(), 1);
		assertEquals(groupFileReference.getChildren().get(0).getIdentifier(), testGroupChild.getIdentifier());
		assertEquals(groupFileReference.getChildren().get(0).getComment(), testGroupChild.getComment());
		assertEquals(groupFileReference.getName(), testGroupName);
		assertEquals(groupFileReference.getSourceTree(), testGroupSourceTree);
		assertNull(groupFileReference.getPath());
		assertNull(groupFileReference.getExplicitFileType());
		assertNull(groupFileReference.getFileEncoding());
		assertNull(groupFileReference.getIncludeInIndex());
		assertNull(groupFileReference.getLastKnownFileType());
	}
	
	@Test
	public void testGroupToString() throws InvalidObjectFormatException {
		PBXFileElement groupFileReference = new PBXFileElement(testGroupObject);
		assertEquals(groupFileReference.toString(2), testGroupObject);
	}
	
	@Test
	public void testBasicVariantGroup() {
		
	}
	
	@Test
	public void testVariantGroupToString() {
		
	}

}
