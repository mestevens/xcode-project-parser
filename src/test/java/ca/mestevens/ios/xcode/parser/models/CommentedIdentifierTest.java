package ca.mestevens.ios.xcode.parser.models;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertEquals;

@Test(groups = "automated")
public class CommentedIdentifierTest {
	
	public final String testIdentifier = "test-id";
	public final String testComment = "test-comment";

	@BeforeMethod
	public void setUp() {
		
	}
	
	@AfterMethod
	public void tearDown() {
		
	}
	
	@Test
	public void testNormalCommentedIdentifier() {
		CommentedIdentifier identifier = new CommentedIdentifier(testIdentifier, testComment);
		assertEquals(identifier.getIdentifier(), testIdentifier);
		assertEquals(identifier.getComment(), testComment);
	}
	
	@Test
	public void testCommentedIdentifierWithNullComment() {
		CommentedIdentifier identifier = new CommentedIdentifier(testIdentifier, null);
		assertEquals(identifier.getIdentifier(), testIdentifier);
		assertNull(identifier.getComment());
	}
	
	@Test
	public void testCommentedIdentifierWithEmptyString() {
		CommentedIdentifier identifier = new CommentedIdentifier(testIdentifier, "");
		assertEquals(identifier.getIdentifier(), testIdentifier);
		assertEquals(identifier.getComment(), "");
	}
	
	@Test
	public void testCommentedIdentifierWithNullIdentifier() {
		CommentedIdentifier identifier = new CommentedIdentifier(null, testComment);
		assertNull(identifier.getIdentifier());
		assertEquals(identifier.getComment(), testComment);
	}
	
	@Test
	public void testCommentedIdentifierWithEmptyIdentifier() {
		CommentedIdentifier identifier = new CommentedIdentifier("", testComment);
		assertEquals(identifier.getIdentifier(), "");
		assertEquals(identifier.getComment(), testComment);
	}
	
	@Test
	public void testToString() {
		CommentedIdentifier identifier = new CommentedIdentifier(testIdentifier, testComment);
		assertEquals(identifier.toString(), testIdentifier + " /* " + testComment + " */");
	}
	
	@Test
	public void testToStringNullComment() {
		CommentedIdentifier identifier = new CommentedIdentifier(testIdentifier, null);
		assertEquals(identifier.toString(), testIdentifier);
	}
	
	@Test
	public void testToStringEmptyComment() {
		CommentedIdentifier identifier = new CommentedIdentifier(testIdentifier, "");
		assertEquals(identifier.toString(), testIdentifier);
	}
	
	@Test
	public void testToStringNullIdentifier() {
		CommentedIdentifier identifier = new CommentedIdentifier(null, testComment);
		assertEquals(identifier.toString(), "/* " + testComment + " */");
	}
	
	@Test
	public void testToStringEmptyIdentifier() {
		CommentedIdentifier identifier = new CommentedIdentifier("", testComment);
		assertEquals(identifier.toString(), "/* " + testComment + " */");
	}
	
	@Test
	public void testToStringBothNull() {
		CommentedIdentifier identifier = new CommentedIdentifier(null, null);
		assertEquals(identifier.toString(), "");
	}
	
	@Test
	public void testToStringWithIndents() {
		CommentedIdentifier identifier = new CommentedIdentifier(testIdentifier, testComment);
		assertEquals(identifier.toString(2), "\t\t" + testIdentifier + " /* " + testComment + " */");
	}
	
}
