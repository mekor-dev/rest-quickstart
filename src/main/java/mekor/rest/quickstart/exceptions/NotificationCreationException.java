/**
 * 
 */
package mekor.rest.quickstart.exceptions;

import mekor.rest.quickstart.services.NotificationService;

/**
 * Thrown when an error has occurred while sending a notification with
 * {@link NotificationService}
 * 
 * @author mekor
 *
 */
public class NotificationCreationException extends RuntimeException {

	private static final long serialVersionUID = -1241497860220965442L;

	public NotificationCreationException(String message, Throwable e) {
		super(message, e);
	}

	public NotificationCreationException(String message) {
		super(message);
	}

}
