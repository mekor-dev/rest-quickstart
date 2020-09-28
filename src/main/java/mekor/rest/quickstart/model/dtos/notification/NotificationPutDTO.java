/**
 * 
 */
package mekor.rest.quickstart.model.dtos.notification;

import javax.validation.constraints.NotNull;

import mekor.rest.quickstart.model.dtos.utils.SingleIDDTO;

/**
 * 
 * Data for editing a Notification
 * 
 * @author mekor
 *
 */
public class NotificationPutDTO extends SingleIDDTO {

	/**
	 * True if the notification has been seen by the user
	 */
	@NotNull
	public Boolean seen;

}
