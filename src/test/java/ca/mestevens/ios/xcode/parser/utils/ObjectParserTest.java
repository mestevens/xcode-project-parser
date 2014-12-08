package ca.mestevens.ios.xcode.parser.utils;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertEquals;

public class ObjectParserTest {
	
	private ObjectParser objectParser;
	
	private final String firstObject = "8A23807E19F94F640025EFB1 /* testProject.app */ = {isa = PBXFileReference; explicitFileType = wrapper.application; includeInIndex = 0; path = testProject.app; sourceTree = BUILT_PRODUCTS_DIR; };";
	private final String secondObject = "8A23808219F94F640025EFB1 /* Info.plist */ = {isa = PBXFileReference; lastKnownFileType = text.plist.xml; path = Info.plist; sourceTree = \"<group>\"; };";
	private final String thirdObject = "8A23808319F94F640025EFB1 /* main.m */ = {isa = PBXFileReference; lastKnownFileType = sourcecode.c.objc; path = main.m; sourceTree = \"<group>\"; };";
	private final String fourthObject = "8A23808519F94F640025EFB1 /* AppDelegate.h */ = {isa = PBXFileReference; lastKnownFileType = sourcecode.c.h; path = AppDelegate.h; sourceTree = \"<group>\"; };";
	private final String fifthObject = "8A23808819F94F640025EFB1 /* ViewController.h */ = {isa = PBXFileReference; lastKnownFileType = sourcecode.c.h; path = ViewController.h; sourceTree = \"<group>\"; };";
	private final String sixthObject = "8A23808919F94F640025EFB1 /* ViewController.m */ = {isa = PBXFileReference; lastKnownFileType = sourcecode.c.objc; path = ViewController.m; sourceTree = \"<group>\"; };";
	private final String seventhObject = "8A23808C19F94F640025EFB1 /* Base */ = {isa = PBXFileReference; lastKnownFileType = file.storyboard; name = Base; path = Base.lproj/Main.storyboard; sourceTree = \"<group>\"; };";
	private final String eighthObject = "8A23808E19F94F640025EFB1 /* Images.xcassets */ = {isa = PBXFileReference; lastKnownFileType = folder.assetcatalog; path = Images.xcassets; sourceTree = \"<group>\"; };";
	private final String firstSection = "/* Begin PBXFileReference section */\n" +
"\t\t" + firstObject + "\n" +
"\t\t" + secondObject + "\n" +
"\t\t" + thirdObject + "\n" +
"\t\t" + fourthObject + "\n" +
"\t\t" + fifthObject + "\n" +
"\t\t" + sixthObject + "\n" +
"\t\t" + seventhObject + "\n" +
"\t\t" + eighthObject + "\n" +
"/* End PBXFileReference section */";
	
	private final String ninthObject = "8A23807B19F94F630025EFB1 /* Frameworks */ = {\n" +
"\t\t\tisa = PBXFrameworksBuildPhase;\n" +
"\t\t\tbuildActionMask = 2147483647;\n" +
"\t\t\tfiles = (\n" +
"\t\t\t);\n" +
"\t\t\trunOnlyForDeploymentPostprocessing = 0;\n" +
"\t\t};";
	private final String secondSection = "/* Begin PBXFrameworksBuildPhase section */\n" +
"\t\t" + ninthObject + "\n" +
"/* End PBXFrameworksBuildPhase section */";
	
	@BeforeMethod
	public void setUp() {
		objectParser = new ObjectParser(firstSection);
	}
	
	@AfterMethod
	public void tearDown() {
		objectParser = null;
	}
	
	@Test
	public void testStringIsObject() {
		String returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, firstObject);
	}
	
	@Test
	public void testGetAllObjects() {
		String returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, firstObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, secondObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, thirdObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, fourthObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, fifthObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, sixthObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, seventhObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, eighthObject);
	}
	
	@Test
	public void testStringIsNotObject() {
		objectParser = new ObjectParser("This is a string that isn't an object! Yay!");
		assertNull(objectParser.parseNextObject());
	}
	
	@Test
	public void testStringNotEqualBrackets() {
		objectParser = new ObjectParser("8A23807E19F94F640025EFB1 /* testProject.app */ = {isa = PBXFileReference; explicitFileType = wrapper.application; includeInIndex = 0; path = testProject.app; sourceTree = BUILT_PRODUCTS_DIR; ;");
		assertNull(objectParser.parseNextObject());
	}
	
	@Test
	public void testEmptyString() {
		objectParser = new ObjectParser("");
		assertNull(objectParser.parseNextObject());
	}
	
	@Test
	public void testNullString() {
		objectParser = new ObjectParser(null);
		assertNull(objectParser.parseNextObject());
	}
	
	@Test
	public void testStringMultipleSections() {
		objectParser = new ObjectParser(firstSection + "\n\n" + secondSection);
		//First section
		String returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, firstObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, secondObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, thirdObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, fourthObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, fifthObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, sixthObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, seventhObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, eighthObject);
		
		//Second section
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, ninthObject);
	}

	@Test
	public void testGetNestedObject() {
		objectParser = new ObjectParser(firstObject);
		objectParser = objectParser.getNextNestedObjects();
		assertEquals(objectParser.parseNextObject(), "isa = PBXFileReference;");
		assertEquals(objectParser.parseNextObject(), "explicitFileType = wrapper.application;");
		assertEquals(objectParser.parseNextObject(), "includeInIndex = 0;");
		assertEquals(objectParser.parseNextObject(), "path = testProject.app;");
		assertEquals(objectParser.parseNextObject(), "sourceTree = BUILT_PRODUCTS_DIR;");
		assertNull(objectParser.parseNextObject());
	}
	
	@Test
	public void testGetNestedObjectInSection() {
		String returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, firstObject);
		
		ObjectParser nestedParser = new ObjectParser(returnedObject);
		nestedParser = nestedParser.getNextNestedObjects();
		assertEquals(nestedParser.parseNextObject(), "isa = PBXFileReference;");
		assertEquals(nestedParser.parseNextObject(), "explicitFileType = wrapper.application;");
		assertEquals(nestedParser.parseNextObject(), "includeInIndex = 0;");
		assertEquals(nestedParser.parseNextObject(), "path = testProject.app;");
		assertEquals(nestedParser.parseNextObject(), "sourceTree = BUILT_PRODUCTS_DIR;");
		assertNull(nestedParser.parseNextObject());
		
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, secondObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, thirdObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, fourthObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, fifthObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, sixthObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, seventhObject);
		returnedObject = objectParser.parseNextObject();
		assertEquals(returnedObject, eighthObject);
	}

}
