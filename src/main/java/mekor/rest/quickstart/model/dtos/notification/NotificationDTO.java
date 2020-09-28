/**
 * 
 */
package mekor.rest.quickstart.model.dtos.notification;

import mekor.rest.quickstart.model.dtos.user.UserPrivateDTO;
import mekor.rest.quickstart.model.dtos.utils.LocalizedStringDTO;
import mekor.rest.quickstart.model.dtos.utils.SingleIDDTO;
import mekor.rest.quickstart.model.entities.notification.NotificationType;

/**
 * 
 * Notification sent to a user
 * 
 * @author mekor
 *
 */
public class NotificationDTO extends SingleIDDTO {

	/**
	 * Type of the notification
	 */
	public NotificationType type;

	/**
	 * Title of the notification
	 */
	public LocalizedStringDTO title;

	/**
	 * The relative URL where this notification redirects
	 */
	public String redirection;

	/**
	 * True if the notification has been seen by the user
	 */
	public Boolean seen = false;

	/**
	 * The user who send the notification
	 */
	public UserPrivateDTO sender;
}
