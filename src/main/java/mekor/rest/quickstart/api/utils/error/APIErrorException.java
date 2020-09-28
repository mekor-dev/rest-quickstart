/**
 * 
 */
package mekor.rest.quickstart.api.utils.error;

import mekor.rest.quickstart.utils.gson.GsonUtils;

/**
 * Part of a {@link APIError}. <br />
 * Adds info about the exception that has been thrown and its cause
 * 
 * @author mekor
 *
 */
public class APIErrorException {

	/**
	 * The Type of the exception
	 */
	public String type;

	/**
	 * The message of the exception
	 */
	public String message;

	/**
	 * Exception which caused the exception
	 */
	public APIErrorException causedBy;

	@Override
	public String toString() {
		return GsonUtils.toJson(this);
	}

}
