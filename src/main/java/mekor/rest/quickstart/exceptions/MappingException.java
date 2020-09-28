/**
 * 
 */
package mekor.rest.quickstart.exceptions;

/**
 * 
 * Thrown when an error occurs during a mapping operation (DTO to Entity /
 * Entity to DTO)
 * 
 * @author mekor
 *
 */
public class MappingException extends RuntimeException {

	private static final long serialVersionUID = -1538222309619774044L;

	public MappingException(String message, Throwable cause) {
		super(message, cause);
	}

	public MappingException(String message) {
		super(message);
	}

}
