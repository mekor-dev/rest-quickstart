/**
 * 
 */
package mekor.rest.quickstart.exceptions;

/**
 * A {@link MappingException} caused by a client error (wrong JSON_PATCH or
 * association with an ID that doesn't exists in JSON body for example)
 * 
 * @author mekor
 *
 */
public class MappingClientException extends MappingException {

	private static final long serialVersionUID = 3929547039021910576L;

	public MappingClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public MappingClientException(String message) {
		super(message);
	}

}
