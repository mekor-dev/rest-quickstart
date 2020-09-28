package mekor.rest.quickstart.model.entities.user.credentials;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.model.entities.utils.SingleIDEntity;

/**
 * Token that a user can use to reset its password
 * 
 * @author mekor
 *
 */
@Entity
@Table
public class ForgottenPasswordToken extends SingleIDEntity{
	
	private static final long serialVersionUID = 1583721803475602732L;

	/**
	 * Token that can be use to reset password.
	 */
	@Column(nullable = false, unique = true)
	private String token;
	
	/**
	 * The date when the token expires.
	 */
	@Column(nullable = false)
	private Date expirationDate;
	
	/**
	 * The user that can use this token.
	 */
	@OneToOne
	@JoinColumn(name = "userID", nullable = false)
	private User user;

	/**
	 * @see #token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @see #token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @see #expirationDate
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @see #expirationDate
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @see #user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @see #user
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
