/**
 * 
 */
package mekor.rest.quickstart.model.entities.user.credentials;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.model.entities.utils.SingleIDEntity;
import mekor.rest.quickstart.utils.gson.GsonIgnore;

/**
 * Holds password for a user.
 * 
 * @author mekor
 *
 */
@Entity
@Table
public class PasswordCredentials extends SingleIDEntity {

	private static final long serialVersionUID = -4324976957265163728L;

	/**
	 * The encrypted password
	 */
	@Column
	private String password;

	/**
	 * Association leading to the {@link User} which have this password
	 */
	@GsonIgnore
	@OneToOne
	@JoinColumn(name = "userID", nullable = false)
	private User user;

	/**
	 * @see {@link #password}
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @see {@link #password}
	 */
	public void setPassword(String password) {
		this.password = password;
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

}
