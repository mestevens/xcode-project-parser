package ca.mestevens.ios.xcode.parser.models;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ca.mestevens.ios.xcode.parser.exceptions.InvalidObjectFormatException;

import static org.testng.Assert.assertEquals;

public class PBXContainerItemProxyTest {
	
	private String testReference = "8A23809819F94F640025EFB1";
	private String testReferenceComment = "PBXContainerItemProxy";
	private String testIsa = "PBXContainerItemProxy";
	private String testContainerPortal = "8A23807619F94F630025EFB1";
	private String testContainerPortalComment = "Project object";
	private Integer testProxyType = 1;
	private String testRemoteGlobalIDString = "8A23807D19F94F630025EFB1";
	private String testRemoteInfo = "testProject";
	
	private String testObject = "\t\t" + testReference + " /* " + testReferenceComment + " */ = {\n" +
			"\t\t\tisa = " + testIsa + ";\n" +
			"\t\t\tcontainerPortal = " + testContainerPortal + " /* " + testContainerPortalComment + " */;\n" +
			"\t\t\tproxyType = " + testProxyType + ";\n" +
			"\t\t\tremoteGlobalIDString = " + testRemoteGlobalIDString + ";\n" +
			"\t\t\tremoteInfo = " + testRemoteInfo + ";\n" +
			"\t\t};";
	//private String testSection = "/* Begin PBXContainerItemProxy section */\n" +
	//		testObject + "\n" +
	//		"/* End PBXContainerItemProxy section */";
	
	@BeforeMethod
	public void setUp() {
		
	}
	
	@AfterMethod
	public void tearDown() {
		
	}
	
	@Test
	public void testBasicContainerItemProxy() throws InvalidObjectFormatException {
		PBXContainerItemProxy containerItemProxy = new PBXContainerItemProxy(testObject);
		assertEquals(containerItemProxy.getReference().getIdentifier(), testReference);
		assertEquals(containerItemProxy.getReference().getComment(), testReferenceComment);
		assertEquals(containerItemProxy.getIsa(), testIsa);
		assertEquals(containerItemProxy.getContainerPortal().getIdentifier(), testContainerPortal);
		assertEquals(containerItemProxy.getContainerPortal().getComment(), testContainerPortalComment);
		assertEquals(containerItemProxy.getProxyType(), testProxyType);
		assertEquals(containerItemProxy.getRemoteGlobalIDString(), testRemoteGlobalIDString);
		assertEquals(containerItemProxy.getRemoteInfo(), testRemoteInfo);
	}
	
	@Test
	public void testToString() throws InvalidObjectFormatException {
		PBXContainerItemProxy containerItemProxy = new PBXContainerItemProxy(testObject);
		assertEquals(containerItemProxy.toString(2), testObject);
	}

}
