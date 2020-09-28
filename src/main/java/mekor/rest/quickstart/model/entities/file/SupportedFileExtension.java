package mekor.rest.quickstart.model.entities.file;

import java.util.List;

/**
 * List of extensions supported by the application
 * 
 * @author mekor
 *
 */
public enum SupportedFileExtension {

	JPG(List.of("jpg", "jpeg")),
	PNG(List.of("png")),
	GIF(List.of("gif"));

	private List<String> extensions;

	private SupportedFileExtension(List<String> extensions) {
		this.extensions = extensions;
	}

	public static SupportedFileExtension get(String extensionString) {
		for (SupportedFileExtension ext : SupportedFileExtension.values()) {
			if (ext.extensions.contains(extensionString.toLowerCase())) {
				return ext;
			}
		}
		return null;
	}

}