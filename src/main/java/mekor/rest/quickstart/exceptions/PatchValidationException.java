/**
 * 
 */
package mekor.rest.quickstart.exceptions;

import javax.validation.ValidationException;

/**
 * Thrown when a DTO failed to be validated after a JSON_PATCH
 * 
 * @author mekor
 *
 */
public class PatchValidationException extends ValidationException {

	private static final long serialVersionUID = -651003474626231860L;

	public PatchValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public PatchValidationException(String message) {
		super(message);
	}

}
