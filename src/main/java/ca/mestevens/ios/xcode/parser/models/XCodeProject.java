package ca.mestevens.ios.xcode.parser.models;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import ca.mestevens.ios.xcode.parser.exceptions.FileReferenceDoesNotExistException;
import ca.mestevens.ios.xcode.parser.exceptions.InvalidObjectFormatException;
import ca.mestevens.ios.xcode.parser.utils.ObjectParser;

public class XCodeProject {
	
	public Integer archiveVersion;
	public List<String> classes;
	public Integer objectVersion;
	
	//Objects
	public List<PBXBuildFile> buildFiles;
	public PBXContainerItemProxy containerItemProxy;
	public List<PBXFileElement> fileReferences;
	public List<PBXBuildPhase> frameworksBuildPhases;
	public List<PBXFileElement> groups;
	public List<PBXTarget> nativeTargets;
	public List<PBXTarget> aggregateTargets;
	public List<PBXTarget> legacyTargets;
	public PBXProject project;
	public List<PBXBuildPhase> resourcesBuildPhases;
	public List<PBXBuildPhase> sourcesBuildPhases;
	public PBXTargetDependency targetDependency;
	public List<PBXFileElement> variantGroups;
	public List<XCBuildConfiguration> buildConfigurations;
	public List<XCConfigurationList> configurationLists;
	public List<PBXBuildPhase> appleScriptBuildPhases;
	public List<PBXBuildPhase> copyFilesBuildPhases;
	public List<PBXBuildPhase> headersBuildPhases;
	public List<PBXBuildPhase> shellScriptBuildPhases;
	
	public CommentedIdentifier rootObject;
	
	public XCodeProject(String projectPath) throws InvalidObjectFormatException {
		if (projectPath.endsWith(".xcodeproj")) {
			projectPath.concat("/project.pbxproj");
		} else if (!projectPath.endsWith(".pbxproj")) {
			throw new InvalidObjectFormatException("Invalid xcodeproj or pbxproj path.");
		}
		try {
			Path path = Paths.get(projectPath);
			String projectString = new String(Files.readAllBytes(path));
			projectString = projectString.trim();
			projectString = projectString.substring(projectString.indexOf('{') + 1);
			ObjectParser parser = new ObjectParser(projectString);
			String nextObject = parser.parseNextObject();
			while(nextObject != null) {
				nextObject = nextObject.substring(0, nextObject.length() - 1);
				String[] splitObject = nextObject.split("=");
				String key = splitObject[0].trim();
				String value = "";
				for (int i = 1; i < splitObject.length; i++) {
					if (i > 1) {
						value += "=";
					}
					value += splitObject[i];
				}
				value = value.trim();
				if (key.equals("archiveVersion")) {
					this.archiveVersion = Integer.valueOf(value);
				} else if (key.equals("classes")) {
					this.classes = new ArrayList<String>();
				} else if (key.equals("objectVersion")) {
					this.objectVersion = Integer.valueOf(value);
				} else if (key.equals("rootObject")) {
					this.rootObject = parser.getCommentedIdentifier(value);
				} else if (key.equals("objects")) {
					String section = getSection(value, "PBXBuildFile");
					this.buildFiles = getList(PBXBuildFile.class, section);
					section = getSection(value, "PBXContainerItemProxy");
					this.containerItemProxy = getObject(PBXContainerItemProxy.class, section);
					section = getSection(value, "PBXFileReference");
					this.fileReferences = getList(PBXFileElement.class, section);
					section = getSection(value, "PBXFrameworksBuildPhase");
					this.frameworksBuildPhases = getList(PBXBuildPhase.class, section);
					section = getSection(value, "PBXGroup");
					this.groups = getList(PBXFileElement.class, section);
					section = getSection(value, "PBXNativeTarget");
					this.nativeTargets = getList(PBXTarget.class, section);
					section = getSection(value, "PBXAggregateTarget");
					this.aggregateTargets = getList(PBXTarget.class, section);
					section = getSection(value, "PBXLegacyTarget");
					this.legacyTargets = getList(PBXTarget.class, section);
					section = getSection(value, "PBXProject");
					this.project = getObject(PBXProject.class, section);
					section = getSection(value, "PBXResourcesBuildPhase");
					this.resourcesBuildPhases = getList(PBXBuildPhase.class, section);
					section = getSection(value, "PBXSourcesBuildPhase");
					this.sourcesBuildPhases = getList(PBXBuildPhase.class, section);
					section = getSection(value, "PBXTargetDependency");
					this.targetDependency = getObject(PBXTargetDependency.class, section);
					section = getSection(value, "PBXVariantGroup");
					this.variantGroups = getList(PBXFileElement.class, section);
					section = getSection(value, "XCBuildConfiguration");
					this.buildConfigurations = getList(XCBuildConfiguration.class, section);
					section = getSection(value, "XCConfigurationList");
					this.configurationLists = getList(XCConfigurationList.class, section);
					section = getSection(value, "PBXAppleScriptBuildPhase");
					this.appleScriptBuildPhases = getList(PBXBuildPhase.class, section);
					section = getSection(value, "PBXCopyFilesBuildPhase");
					this.copyFilesBuildPhases = getList(PBXBuildPhase.class, section);
					section = getSection(value, "PBXHeadersBuildPhase");
					this.headersBuildPhases = getList(PBXBuildPhase.class, section);
					section = getSection(value, "PBXShellScriptBuildPhase");
					this.shellScriptBuildPhases = getList(PBXBuildPhase.class, section);
				}
				nextObject = parser.parseNextObject();
			}
		} catch (Exception ex) {
			throw new InvalidObjectFormatException(ex);
		}
	}
	
	@Override
	public String toString() {
		String returnString = "";
		returnString += "// !$*UTF8*$!\n";
		returnString += "{\n";
		returnString += "\tarchiveVersion = " + this.archiveVersion + ";\n";
		returnString += "\tclasses = {\n";
		for(String clazz : classes) {
			returnString += "\t" + clazz + "\n";
		}
		returnString += "\t};\n";
		returnString += "\tobjectVersion = " + this.objectVersion + ";\n";
		returnString += "\tobjects = {\n\n";
		if (this.buildFiles != null && this.buildFiles.size() > 0) {
			returnString += "/* Begin PBXBuildFile section */\n";
			for(PBXBuildFile buildFile : this.buildFiles) {
				returnString += buildFile.toString(2) + "\n";
			}
			returnString += "/* End PBXBuildFile section */\n\n";
		}
		if (this.containerItemProxy != null) {
			returnString += "/* Begin PBXContainerItemProxy section */\n";
			returnString += this.containerItemProxy.toString(2) + "\n";
			returnString += "/* End PBXContainerItemProxy section */\n\n";
		}
		if (this.fileReferences != null && this.fileReferences.size() > 0) {
			returnString += "/* Begin PBXFileReference section */\n";
			for(PBXFileElement fileReference : fileReferences) {
				returnString += fileReference.toString(2) + "\n";
			}
			returnString += "/* End PBXFileReference section */\n\n";
		}
		if (this.frameworksBuildPhases != null && this.frameworksBuildPhases.size() > 0) {
			returnString += "/* Begin PBXFrameworksBuildPhase section */\n";
			for (PBXBuildPhase frameworksBuildPhase : this.frameworksBuildPhases) {
				returnString += frameworksBuildPhase.toString(2) + "\n";
			}
			returnString += "/* End PBXFrameworksBuildPhase section */\n\n";
		}
		if (this.groups != null && this.groups.size() > 0) {
			returnString += "/* Begin PBXGroup section */\n";
			for(PBXFileElement group : this.groups) {
				returnString += group.toString(2) + "\n";
			}
			returnString += "/* End PBXGroup section */\n\n";
		}
		if (this.nativeTargets != null && this.nativeTargets.size() > 0) {
			returnString += "/* Begin PBXNativeTarget section */\n";
			for (PBXTarget nativeTarget : this.nativeTargets) {
				returnString += nativeTarget.toString(2) + "\n";
			}
			returnString += "/* End PBXNativeTarget section */\n\n";
		}
		if (this.aggregateTargets != null && this.aggregateTargets.size() > 0) {
			returnString += "/* Begin PBXAggregateTarget section */\n";
			for (PBXTarget aggregateTarget : this.aggregateTargets) {
				returnString += aggregateTarget.toString(2) + "\n";
			}
			returnString += "/* End PBXAggregateTarget section */\n\n";
		}
		if (this.legacyTargets != null && this.legacyTargets.size() > 0) {
			returnString += "/* Begin PBXLegacyTarget section */\n";
			for (PBXTarget legacyTarget : this.legacyTargets) {
				returnString += legacyTarget.toString(2) + "\n";
			}
			returnString += "/* End PBXLegacyTarget section */\n\n";
		}
		if (this.project != null) {
			returnString += "/* Begin PBXProject section */\n";
			returnString += this.project.toString(2) + "\n";
			returnString += "/* End PBXProject section */\n\n";
		}
		if (this.resourcesBuildPhases != null && this.resourcesBuildPhases.size() > 0) {
			returnString += "/* Begin PBXResourcesBuildPhase section */\n";
			for (PBXBuildPhase resourcesBuildPhase : this.resourcesBuildPhases) {
				returnString += resourcesBuildPhase.toString(2) + "\n";
			}
			returnString += "/* End PBXResourcesBuildPhase section */\n\n";
		}
		if (this.sourcesBuildPhases != null && this.sourcesBuildPhases.size() > 0) {
			returnString += "/* Begin PBXSourcesBuildPhase section */\n";
			for(PBXBuildPhase sourcesBuildPhase : this.sourcesBuildPhases) {
				returnString += sourcesBuildPhase.toString(2) + "\n";
			}
			returnString += "/* End PBXSourcesBuildPhase section */\n\n";
		}
		if (this.targetDependency != null) {
			returnString += "/* Begin PBXTargetDependency section */\n";
			returnString += this.targetDependency.toString(2) + "\n";
			returnString += "/* End PBXTargetDependency section */\n\n";
		}
		if (this.variantGroups != null && this.variantGroups.size() > 0) {
			returnString += "/* Begin PBXVariantGroup section */\n";
			for(PBXFileElement variantGroup : this.variantGroups) {
				returnString += variantGroup.toString(2) + "\n";
			}
			returnString += "/* End PBXVariantGroup section */\n\n";
		}
		if (this.buildConfigurations != null && this.buildConfigurations.size() > 0) {
			returnString += "/* Begin XCBuildConfiguration section */\n";
			for (XCBuildConfiguration buildConfiguration : this.buildConfigurations) {
				returnString += buildConfiguration.toString(2) + "\n";
			}
			returnString += "/* End XCBuildConfiguration section */\n\n";
		}
		if (this.configurationLists != null && this.configurationLists.size() > 0) {
			returnString += "/* Begin XCConfigurationList section */\n";
			for (XCConfigurationList configurationList : this.configurationLists) {
				returnString += configurationList.toString(2) + "\n";
			}
			returnString += "/* End XCConfigurationList section */\n\n";
		}
		if (this.appleScriptBuildPhases != null && this.appleScriptBuildPhases.size() > 0) {
			returnString += "/* Begin PBXAppleScriptBuildPhase section */\n";
			for (PBXBuildPhase appleScriptBuildPhase : this.appleScriptBuildPhases) {
				returnString += appleScriptBuildPhase.toString(2) + "\n";
			}
			returnString += "/* End PBXAppleScriptBuildPhase section */\n\n";
		}
		if (this.copyFilesBuildPhases != null && this.copyFilesBuildPhases.size() > 0) {
			returnString += "/* Begin PBXCopyFilesBuildPhase section */\n";
			for (PBXBuildPhase copyFilesBuildPhase : this.copyFilesBuildPhases) {
				returnString += copyFilesBuildPhase.toString(2) + "\n";
			}
			returnString += "/* End PBXCopyFilesBuildPhase section */\n\n";
		}
		if (this.headersBuildPhases != null && this.headersBuildPhases.size() > 0) {
			returnString += "/* Begin PBXHeadersBuildPhase section */\n";
			for (PBXBuildPhase headersBuildPhase : this.headersBuildPhases) {
				returnString += headersBuildPhase.toString(2) + "\n";
			}
			returnString += "/* End PBXHeadersBuildPhase section */\n\n";
		}
		if (this.shellScriptBuildPhases != null && this.shellScriptBuildPhases.size() > 0) {
			returnString += "/* Begin PBXShellScriptBuildPhase section */\n";
			for (PBXBuildPhase shellScriptBuildPhase : this.shellScriptBuildPhases) {
				returnString += shellScriptBuildPhase.toString(2) + "\n";
			}
			returnString += "/* End PBXShellScriptBuildPhase section */\n\n";
		}
		returnString += "\t};\n";
		returnString += "\trootObject = " + this.rootObject.toString() + ";\n";
		returnString += "}";
		return returnString;
	}
	
	public String getSection(String body, String sectionName) {
		String endString = "/* End " + sectionName + " section */";
		int startIndex = body.indexOf("/* Begin " + sectionName + " section */");
		int endIndex = body.indexOf(endString);
		if (startIndex == -1 || endIndex == -1) {
			return "";
		}
		return body.substring(startIndex, endIndex + endString.length());
	}
	
	public <T> T getObject(Class<T> clazz, String section) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (section == null || section.equals("")) {
			return null;
		}
		ObjectParser objectsParser = new ObjectParser(section);
		String objectString = objectsParser.parseNextObject();
		Constructor<T> constructor = clazz.getConstructor(String.class);
		T object = constructor.newInstance(objectString);
		return object;
	}
	
	public <T> List<T> getList(Class<T> clazz, String section) throws InvalidObjectFormatException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ObjectParser objectsParser = new ObjectParser(section);
		String objectString = objectsParser.parseNextObject();
		List<T> objects = new ArrayList<T>();
		while(objectString != null) {
			Constructor<T> constructor = clazz.getConstructor(String.class);
			T object = constructor.newInstance(objectString);
			objects.add(object);
			objectString = objectsParser.parseNextObject();
		}
		return objects;
	}
	
	public void createFileReference(String filePath) {
		createFileReference(filePath, "\"<group>\"");
	}
	
	public void createFileReference(String filePath, String sourceTree) {
		PBXFileElement fileReference = new PBXFileElement(filePath, sourceTree);
		if (!this.fileReferences.contains(fileReference)) {
			this.fileReferences.add(fileReference);
		}
	}
	
	public void createBuildFileFromFileReferencePath(String filePath) throws FileReferenceDoesNotExistException {
		createBuildFileFromFileReferencePath(filePath, Paths.get(filePath).getFileName().toString());
	}
	
	public void createBuildFileFromFileReferencePath(String filePath, String buildFileName) throws FileReferenceDoesNotExistException {
		for(PBXFileElement fileReference : this.fileReferences) {
			if (fileReference.getPath().equals(filePath)) {
				String reference = fileReference.getReference().getIdentifier();
				PBXBuildFile buildFile = new PBXBuildFile(buildFileName, reference);
				if (!this.buildFiles.contains(buildFile)) {
					this.buildFiles.add(buildFile);
				}
				return;
			}
		}
		throw new FileReferenceDoesNotExistException("No file reference for file at path \"" + filePath + "\" found.");
	}
	
	public void createGroup(String groupName) {
		createGroup(groupName, new ArrayList<CommentedIdentifier>());
	}
	
	public void createGroup(String groupName, List<CommentedIdentifier> groupChildren) {
		PBXFileElement group = new PBXFileElement("isa", "name", "sourcetree");
		for (CommentedIdentifier child : groupChildren) {
			group.addChild(child);
		}
		//There can be multiple groups of the same name
		this.groups.add(group);
	}

}
