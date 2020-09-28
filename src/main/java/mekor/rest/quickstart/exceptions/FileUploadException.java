/**
 * 
 */
package mekor.rest.quickstart.exceptions;

import mekor.rest.quickstart.api.FileEntityAPI;
import mekor.rest.quickstart.api.utils.FileUploadForm;

/**
 * Thrown when an error occurs when uploading a file with
 * {@link FileEntityAPI#upload(FileUploadForm)}
 * 
 * @author mekor
 *
 */
public class FileUploadException extends RuntimeException {

	private static final long serialVersionUID = -1070749715571682430L;

	public FileUploadException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileUploadException(String message) {
		super(message);
	}

}
