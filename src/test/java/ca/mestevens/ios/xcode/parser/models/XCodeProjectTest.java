package ca.mestevens.ios.xcode.parser.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ca.mestevens.ios.xcode.parser.exceptions.InvalidObjectFormatException;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test(groups = "automated")
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
	
	@Test
	public void testRemoveBuildPhase() {
		assertTrue(xcodeProject.toString().contains("8A23807A19F94F630025EFB1"));
		xcodeProject.removeSourcesBuildPhaseWithIdentifier("8A23807A19F94F630025EFB1");
		assertFalse(xcodeProject.toString().contains("8A23807A19F94F630025EFB1"));
	}
	
	@Test
	public void testAddEmptyGroup() {
		String testGroupName = "Test Group";
		String testGroupId = null;
		String mainGroup = xcodeProject.getProject().getMainGroup().getIdentifier();
		String firstGroup = null;
		xcodeProject.createGroup(testGroupName);
		boolean foundGroup = false;
		for (PBXFileElement group : xcodeProject.getGroups()) {
			if (group.getName() != null && group.getName().equals(testGroupName)) {
				foundGroup = true;
				testGroupId = group.getReference().getIdentifier();
			} else if (group.getReference().getIdentifier().equals(mainGroup)) {
				firstGroup = group.getChildren().get(0).getIdentifier();
			}
		}
		boolean foundInParentGroup = false;
		for (PBXFileElement group : xcodeProject.getGroups()) {
			if (group.getReference().getIdentifier().equals(firstGroup)) {
				for (CommentedIdentifier identifier : group.getChildren()) {
					if (identifier.getIdentifier().equals(testGroupId)) {
						foundInParentGroup = true;
					}
				}
			}
		}
		assertTrue(foundGroup);
		assertTrue(foundInParentGroup);
	}
	
	@Test
	public void testAddBuildPhase() {
		String nativeTarget = "8A23807D19F94F630025EFB1";
		PBXBuildPhase buildPhase = new PBXBuildPhase("PBXCopyFilesBuildPhase", "Copy Test Files", null, "Destination", 1);
		String buildPhaseIdentifier = buildPhase.getReference().getIdentifier();
		xcodeProject.addCopyFilesBuildPhase("8A23807D19F94F630025EFB1", buildPhase);
		boolean foundInTarget = false;
		for (PBXTarget target : xcodeProject.getNativeTargets()) {
			if (target.getReference().getIdentifier().equals(nativeTarget)) {
				for (CommentedIdentifier identifier : target.getBuildPhases()) {
					if (identifier.getIdentifier().equals(buildPhaseIdentifier)) {
						foundInTarget = true;
					}
				}
			}
		}
		assertTrue(foundInTarget);
		boolean foundInBuildPhases = false;
		for (PBXBuildPhase buildPhaseElement : xcodeProject.getCopyFilesBuildPhases()) {
			if (buildPhaseElement.getReference().getIdentifier().equals(buildPhaseIdentifier)) {
				foundInBuildPhases = true;
			}
		}
		assertTrue(foundInBuildPhases);
	}
	
	@Test
	public void testGetBuildProperty() {
		String configurationIdentifier = "8A23809F19F94F640025EFB1";
		String propertyName = "CLANG_ENABLE_MODULES";
		String value = xcodeProject.getBuildConfigurationProperty(configurationIdentifier, propertyName);
		assertEquals(value, "YES");
	}
	
	@Test
	public void testGetKeyDoesNotExist() {
		String configurationIdentifier = "8A23809F19F94F640025EFB1";
		String propertyName = "A_CRAZY_BUILD_PARAM";
		String value = xcodeProject.getBuildConfigurationProperty(configurationIdentifier, propertyName);
		assertNull(value);
	}
	
	@Test
	public void testGetBuildPropertyAsListIfNotList() {
		String configurationIdentifier = "8A23809F19F94F640025EFB1";
		String propertyName = "CLANG_ENABLE_MODULES";
		List<String> value = xcodeProject.getBuildConfigurationPropertyAsList(configurationIdentifier, propertyName);
		assertNull(value);
	}
	
	@Test
	public void testGetListBuildProperty() {
		String configurationIdentifier = "8A23809F19F94F640025EFB1";
		String propertyName = "GCC_PREPROCESSOR_DEFINITIONS";
		List<String> value = xcodeProject.getBuildConfigurationPropertyAsList(configurationIdentifier, propertyName);
		assertNotNull(value);
		assertEquals(value.size(), 2);
		assertTrue(value.contains("\"DEBUG=1\""));
		assertTrue(value.contains("\"$(inherited)\""));
	}
	
	@Test
	public void testGetListBuildPropertyThatIsString() {
		String configurationIdentifier = "8A23809F19F94F640025EFB1";
		String propertyName = "CLANG_ENABLE_MODULES";
		List<String> value = xcodeProject.getBuildConfigurationPropertyAsList(configurationIdentifier, propertyName);
		assertNull(value);
	}
	
	@Test
	public void testGetListDoesNotExist() {
		String configurationIdentifier = "8A23809F19F94F640025EFB1";
		String propertyName = "A_CRAZY_BUILD_PARAM";
		List<String> value = xcodeProject.getBuildConfigurationPropertyAsList(configurationIdentifier, propertyName);
		assertNull(value);
	}

}
