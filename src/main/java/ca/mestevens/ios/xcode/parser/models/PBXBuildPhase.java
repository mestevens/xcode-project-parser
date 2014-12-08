package ca.mestevens.ios.xcode.parser.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ca.mestevens.ios.xcode.parser.exceptions.InvalidObjectFormatException;
import ca.mestevens.ios.xcode.parser.utils.ObjectParser;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PBXBuildPhase implements Comparable<PBXBuildPhase> {

	private CommentedIdentifier reference;
	private String isa;
	private Integer buildActionMask;
	private List<CommentedIdentifier> files;
	private Integer runOnlyForDeploymentPostprocessing;
	
	//PBXCopyFilesBuildPhase
	private String dstPath;
	private Integer dstSubfolderSpec;
	
	//PBXShellScriptBuildPhase
	private List<String> inputPaths;
	private List<String> outputPaths;
	private String shellPath;
	private String shellScript;
	
	public PBXBuildPhase(String buildPhaseString) throws InvalidObjectFormatException {
		try {
			buildPhaseString = buildPhaseString.trim();
			int equalsIndex = buildPhaseString.indexOf('=');
			String commentPart = "";
			String uuidPart = buildPhaseString.substring(0, equalsIndex).trim();
			if (uuidPart.contains("/*")) {
				int commentStartIndex = uuidPart.indexOf("/*");
				int commentEndIndex = uuidPart.indexOf("*/");
				commentPart = buildPhaseString.substring(commentStartIndex + 2, commentEndIndex).trim();
				uuidPart = uuidPart.substring(0, commentStartIndex).trim();
			}
			this.reference = new CommentedIdentifier(uuidPart, commentPart);
			ObjectParser parser = new ObjectParser(buildPhaseString);
			parser = parser.getNextNestedObjects();
			String parserObject = parser.parseNextObject();
			while(parserObject != null) {
				//Remove the ';' character
				parserObject = parserObject.substring(0, parserObject.length() - 1);
				String[] splitObject = parserObject.split("=");
				String key = splitObject[0].trim();
				String value = "";
				for (int i = 1; i < splitObject.length; i++) {
					if (i > 1) {
						value += "=";
					}
					value += splitObject[i];
				}
				value = value.trim();
				if (key.equals("isa")) {
					this.isa = value;
				} else if (key.equals("buildActionMask")) {
					this.buildActionMask = Integer.parseInt(value);
				} else if (key.equals("files")) {
					this.files = parser.getIdentifierList(value);
				} else if (key.equals("runOnlyForDeploymentPostprocessing")) {
					this.runOnlyForDeploymentPostprocessing = Integer.parseInt(value);
				} else if (key.equals("dstPath")) {
					this.dstPath = value;
				} else if (key.equals("dstSubfolderSpec")) {
					this.dstSubfolderSpec = Integer.parseInt(value);
				} else if (key.equals("inputPaths")) {
					this.inputPaths = parser.getStringList(value);
				} else if (key.equals("outputPaths")) {
					this.outputPaths = parser.getStringList(value);
				} else if (key.equals("shellPath")) {
					this.shellPath = value;
				} else if (key.equals("shellScript")) {
					this.shellScript = value;
				}
				parserObject = parser.parseNextObject();
			}
		} catch (Exception ex) {
			throw new InvalidObjectFormatException(ex);
		}
	}
	
	@Override
	public String toString() {
		return toString(0);
	}
	
	public String toString(int numberOfTabs) {
		String tabString = "";
		for (int i = 0; i < numberOfTabs; i++) {
			tabString += "\t";
		}
		String returnString = tabString + reference.toString() + " = {\n";
		returnString += tabString + "\tisa = " + this.isa + ";\n";
		returnString += tabString + "\tbuildActionMask = " + this.buildActionMask + ";\n";
		if (this.dstPath != null) {
			returnString += tabString + "\tdstPath = " + this.dstPath + ";\n";
		}
		if (this.dstSubfolderSpec != null) {
			returnString += tabString + "\tdstSubfolderSpec = " + this.dstSubfolderSpec + ";\n";
		}
		returnString += tabString + "\tfiles = (\n";
		for(CommentedIdentifier file : files) {
			returnString += tabString + "\t\t" + file.toString() + ",\n";
		}
		returnString += tabString + "\t);\n";
		if (this.inputPaths != null) {
			returnString += tabString + "\tinputPaths = (\n";
			for(String inputPath : this.inputPaths) {
				returnString += tabString + "\t\t" + inputPath + ",\n";
			}
			returnString += tabString + "\t);\n";
		}
		if (this.outputPaths != null) {
			returnString += tabString + "\toutputPaths = (\n";
			for(String outputPath : this.outputPaths) {
				returnString += tabString + "\t\t" + outputPath + ",\n";
			}
			returnString += tabString + "\t);\n";
		}
		returnString += tabString + "\trunOnlyForDeploymentPostprocessing = " + this.runOnlyForDeploymentPostprocessing + ";\n";
		if (this.shellPath != null) {
			returnString += tabString + "\tshellPath = " + this.shellPath + ";\n";
		}
		if (this.shellScript != null) {
			returnString += tabString + "\tshellScript = " + this.shellScript + ";\n";
		}
		returnString += tabString + "};";
		return returnString;
	}

	public int compareTo(PBXBuildPhase o) {
		return this.reference.getIdentifier().compareTo(o.reference.getIdentifier());
	}

	
}
