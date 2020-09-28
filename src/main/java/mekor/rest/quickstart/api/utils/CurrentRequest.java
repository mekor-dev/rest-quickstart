/**
 * 
 */
package mekor.rest.quickstart.api.utils;

import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import mekor.rest.quickstart.model.entities.user.User;

/**
 * Hold information about the current request (Locale, User, Organization...)
 * 
 * @author mekor
 *
 */
@RequestScoped
public class CurrentRequest {

	@Inject
	private Logger log;

	/**
	 * The locale that has been defined for this request
	 */
	private Locale locale;

	/**
	 * The user that has been authorized for this request.
	 */
	private User user;

	/**
	 * True if the current user is an admin.
	 */
	private boolean isAdmin = false;

	/**
	 * Check if the user given in parameter is the same than the user of the current
	 * request
	 * 
	 * @param user The user we want to compare with the user of the current request
	 * @return True if both user are the same. False otherwise
	 */
	public boolean checkUser(User user) {
		log.debug("Entering checkUser({})", user);
		return this.user.getId().equals(user.getId());
	}

	/**
	 * @see {@link #locale}
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @see {@link #locale}
	 */
	public void setLocale(Locale locale) {
		log.debug("Current locale set -> {}", locale);
		this.locale = locale;
	}

	/**
	 * @see {@link #user}
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @see {@link #user}
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @see {@link #isAdmin}
	 */
	public boolean isAdmin() {
		return isAdmin;
	}

}
