/**
 * 
 */
package mekor.rest.quickstart.api.utils.error;

import mekor.rest.quickstart.utils.gson.GsonUtils;

/**
 * Used to send information to the client about an error that occurred during an
 * API call <br />
 * Contains a message, information about the exception if an exception has been
 * thrown and a link to the documentation
 * 
 * @author mekor
 *
 */
public class APIError {

	/**
	 * Message provided by the developer for helping the client to handle the error
	 */
	public String message;

	/**
	 * Exception that has been thrown, if an exception has been thrown
	 */
	public APIErrorException exception;

	/**
	 * URL to the documentation
	 */
	public String documentation;

	@Override
	public String toString() {
		return GsonUtils.toJson(this);
	}

}
