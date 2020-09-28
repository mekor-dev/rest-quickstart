package mekor.rest.quickstart.exceptions;

import mekor.rest.quickstart.model.entities.utils.LocalizedString;

/**
 * Thrown when the initialization of a {@link LocalizedString}.map has failed.
 * Most of the times because of bad JSON string format
 * 
 * @author mekor
 *
 */
public class LocalizedStringInitException extends RuntimeException {

	private static final long serialVersionUID = -9155127425104987485L;

	/**
	 * The string that failed to be parsed
	 */
	private String jsonString;

	public LocalizedStringInitException(String jsonString) {
		super();
		this.jsonString = jsonString;
	}

	@Override
	public String getMessage() {
		return "An error has occured while parsing following string: " + jsonString + ". Check its format !";
	}

}
