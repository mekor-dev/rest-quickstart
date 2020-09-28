/**
 * 
 */
package mekor.rest.quickstart.model.entities.notification;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.model.entities.utils.LocalizedString;
import mekor.rest.quickstart.model.entities.utils.SQLConstants;
import mekor.rest.quickstart.model.entities.utils.SingleIDEntity;

/**
 * 
 * Notification sent to a user
 * 
 * @author mekor
 *
 */
@Entity
@Table
public class Notification extends SingleIDEntity {

	private static final long serialVersionUID = -7240774476945015265L;

	/**
	 * Type of the notification
	 */
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationType type;

	/**
	 * Title of the notification
	 */
	@Column(nullable = false)
	@AttributeOverride(name = "jsonText", column = @Column(name = "title", columnDefinition = SQLConstants.TEXT_DEFINITION))
	private LocalizedString title;

	/**
	 * The relative URL where this notification redirects
	 */
	@Column(nullable = false)
	private String redirection;

	/**
	 * True if the notification has been seen by the user
	 */
	@Column(nullable = false)
	private Boolean seen = false;

	/**
	 * The user who send the notification
	 */
	@ManyToOne
	@JoinColumn(name = "senderID", nullable = false)
	private User sender;

	/**
	 * The user who recieve the notification
	 */
	@ManyToOne
	@JoinColumn(name = "userID", nullable = false)
	private User user;

	/**
	 * @see {@link #type}
	 */
	public NotificationType getType() {
		return type;
	}

	/**
	 * @see {@link #type}
	 */
	public void setType(NotificationType type) {
		this.type = type;
	}

	/**
	 * @see {@link #title}
	 */
	public LocalizedString getTitle() {
		return title;
	}

	/**
	 * @see {@link #title}
	 */
	public void setTitle(LocalizedString title) {
		this.title = title;
	}

	/**
	 * @see {@link #redirection}
	 */
	public String getRedirection() {
		return redirection;
	}

	/**
	 * @see {@link #redirection}
	 */
	public void setRedirection(String redirection) {
		this.redirection = redirection;
	}

	/**
	 * @see {@link #seen}
	 */
	public Boolean getSeen() {
		return seen;
	}

	/**
	 * @see {@link #seen}
	 */
	public void setSeen(Boolean seen) {
		this.seen = seen;
	}

	/**
	 * @see {@link #sender}
	 */
	public User getSender() {
		return sender;
	}

	/**
	 * @see {@link #sender}
	 */
	public void setSender(User sender) {
		this.sender = sender;
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