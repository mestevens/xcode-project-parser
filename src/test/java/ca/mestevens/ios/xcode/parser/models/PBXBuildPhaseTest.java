package ca.mestevens.ios.xcode.parser.models;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ca.mestevens.ios.xcode.parser.exceptions.InvalidObjectFormatException;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class PBXBuildPhaseTest {
	
	private final String frameworksBuildPhaseObject = "\t\t8A23807B19F94F630025EFB1 /* Frameworks */ = {\n" +
"\t\t\tisa = PBXFrameworksBuildPhase;\n" +
"\t\t\tbuildActionMask = 2147483647;\n" +
"\t\t\tfiles = (\n" +
"\t\t\t);\n" +
"\t\t\trunOnlyForDeploymentPostprocessing = 0;\n" +
"\t\t};";
	
	@BeforeMethod
	public void setUp() {
		
	}
	
	@AfterMethod
	public void tearDown() {
		
	}
	
	@Test
	public void testBasicBuildPhaseObject() throws InvalidObjectFormatException {
		PBXBuildPhase buildPhaseObject = new PBXBuildPhase(frameworksBuildPhaseObject);
		assertEquals(buildPhaseObject.getReference().getIdentifier(), "8A23807B19F94F630025EFB1");
		assertEquals(buildPhaseObject.getReference().getComment(), "Frameworks");
		assertEquals(buildPhaseObject.getIsa(), "PBXFrameworksBuildPhase");
		assertEquals(buildPhaseObject.getBuildActionMask(), new Integer(2147483647));
		assertNotNull(buildPhaseObject.getFiles());
		assertEquals(buildPhaseObject.getFiles().size(), 0);
		assertEquals(buildPhaseObject.getRunOnlyForDeploymentPostprocessing(), new Integer(0));
		assertNull(buildPhaseObject.getDstPath());
		assertNull(buildPhaseObject.getDstSubfolderSpec());
		assertNull(buildPhaseObject.getInputPaths());
		assertNull(buildPhaseObject.getOutputPaths());
		assertNull(buildPhaseObject.getShellPath());
		assertNull(buildPhaseObject.getShellScript());
	}
	
	@Test
	public void testToString() throws InvalidObjectFormatException {
		PBXBuildPhase buildPhaseObject = new PBXBuildPhase(frameworksBuildPhaseObject);
		assertEquals(buildPhaseObject.toString(2), frameworksBuildPhaseObject);
	}

}
