package ca.mestevens.ios.xcode.parser.models;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.mestevens.ios.xcode.parser.exceptions.InvalidObjectFormatException;
import ca.mestevens.ios.xcode.parser.utils.ObjectParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PBXBuildFile implements Comparable<PBXBuildFile> {

	private CommentedIdentifier reference;
	private String isa;
	private CommentedIdentifier fileRef;
	private Map<String, String> settings;
	
	public PBXBuildFile(String filename, String fileRef) {
		this.reference = new CommentedIdentifier(UUID.randomUUID().toString(), filename);
		this.isa = "PBXBuildFile";
		this.fileRef = new CommentedIdentifier(fileRef, filename);
		this.settings = new TreeMap<String, String>();
	}
	
	public PBXBuildFile(String buildFileString) throws InvalidObjectFormatException {
		try {
			buildFileString = buildFileString.trim();
			int equalsIndex = buildFileString.indexOf('=');
			String commentPart = "";
			String uuidPart = buildFileString.substring(0, equalsIndex).trim();
			if (uuidPart.contains("/*")) {
				int commentStartIndex = uuidPart.indexOf("/*");
				int commentEndIndex = uuidPart.indexOf("*/");
				commentPart = buildFileString.substring(commentStartIndex + 2, commentEndIndex).trim();
				uuidPart = uuidPart.substring(0, commentStartIndex).trim();
			}
			this.reference = new CommentedIdentifier(uuidPart, commentPart);
			this.settings = new HashMap<String, String>();
			ObjectParser parser = new ObjectParser(buildFileString);
			parser = parser.getNextNestedObjects();
			String parserObject = parser.parseNextObject();
			while(parserObject != null) {
				//Remove the ';' character
				parserObject = parserObject.substring(0, parserObject.length() - 1);
				String[] splitObject = parserObject.split("=");
				String key = splitObject[0].trim();
				String value = splitObject[1].trim();
				if (key.equals("isa")) {
					this.isa = value;
				} else if (key.equals("fileRef")) {
					this.fileRef = parser.getCommentedIdentifier(value);
				} else {
					settings.put(key, value);
				}
				parserObject = parser.parseNextObject();
			}
		} catch (Exception ex) {
			throw new InvalidObjectFormatException(ex);
		}
	}
	
	@Override
	public String toString() {
		Map<String, String> tempSettings = settings;
		tempSettings.put("fileRef", fileRef.toString());
		tempSettings.put("isa", this.isa);
		String returnString = reference.toString() + " = {";
		for(String key : tempSettings.keySet()) {
			returnString += key + " = " + tempSettings.get(key) + "; ";
		}
		returnString += "};";
		return returnString;
	}
	
	public String toString(int numberOfTabs) {
		String returnString = "";
		for (int i = 0; i < numberOfTabs; i++) {
			returnString += "\t";
		}
		returnString += this.toString();
		return returnString;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof PBXBuildFile) {
			PBXBuildFile oBuildFile = (PBXBuildFile)o;
			return oBuildFile.getFileRef().equals(this.fileRef);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(this.reference);
		builder.append(this.isa);
		builder.append(this.fileRef);
		return builder.toHashCode();
	}

	public int compareTo(PBXBuildFile o) {
		return this.reference.getIdentifier().compareTo(o.getReference().getIdentifier());
	}
	
}