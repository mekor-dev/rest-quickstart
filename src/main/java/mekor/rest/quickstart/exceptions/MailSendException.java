/**
 * 
 */
package mekor.rest.quickstart.exceptions;

import mekor.rest.quickstart.services.MailService;

/**
 * Thrown when an error has occurred while sending a Mail with
 * {@link MailService}
 * 
 * @author mekor
 *
 */
public class MailSendException extends RuntimeException {

	private static final long serialVersionUID = -1070749715571682430L;

	public MailSendException(String message, Throwable cause) {
		super(message, cause);
	}

	public MailSendException(String message) {
		super(message);
	}

}
