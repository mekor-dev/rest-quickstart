/**
 * 
 */
package mekor.rest.quickstart.security.authorization;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import mekor.rest.quickstart.api.utils.CurrentRequest;
import mekor.rest.quickstart.model.entities.notification.Notification;
import mekor.rest.quickstart.model.entities.utils.GenericEntity;

/**
 * This service allow to check a user roles and authorize or not the user.
 * <br />
 * 
 * It throws an {@link AuthorizationException} if the authorization fails.
 * 
 * @author mekor
 *
 */
@ApplicationScoped
public class AuthorizationService {

	@Inject
	private CurrentRequest currentRequest;

	/**
	 * Check if the entity in parameter has not been deleted (if it has been, also
	 * check if the user is admin)
	 * 
	 * @param entity The entity to check
	 */
	public void entityIsNotDeleted(GenericEntity entity, String errorMsg) {
		if (entity.isDeleted() && !currentRequest.isAdmin()) {
			throw new NotFoundException(errorMsg);
		}
	}

	/**
	 * Check if the given user is admin
	 * 
	 * @param user The user to check
	 */
	public void isAdmin() {
		if (!currentRequest.getUser().getIsAdmin()) {
			throw new AuthorizationException(403, "Admin only");
		}
	}

	/**
	 * Check if the current user can access the given Notification.
	 * 
	 * @param notif The notification to check
	 */
	public void canAccessNotification(Notification notif) {
		if (!currentRequest.checkUser(notif.getUser())) {
			throw new AuthorizationException(403, "The current user does not own this notification");
		}
	}

	/**
	 * Cannot be access. Will always throw the exception.
	 * 
	 * @throws AuthorizationException
	 */
	public void denyAll() throws AuthorizationException {
		throw new AuthorizationException(403, "This ressource cannot be accessed");
	}

}
