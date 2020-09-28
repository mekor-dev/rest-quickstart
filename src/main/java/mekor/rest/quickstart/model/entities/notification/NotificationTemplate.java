/**
 * 
 */
package mekor.rest.quickstart.model.entities.notification;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import mekor.rest.quickstart.model.entities.utils.GenericEntity;
import mekor.rest.quickstart.model.entities.utils.LocalizedString;
import mekor.rest.quickstart.model.entities.utils.SQLConstants;

/**
 * 
 * Default title for a given {@link NotificationType}
 * 
 * @author mekor
 *
 */
@Entity
@Table
public class NotificationTemplate extends GenericEntity {

	private static final long serialVersionUID = 7804599939127837231L;

	/**
	 * Type of the notification
	 */
	@Id
	@Column
	@Enumerated(EnumType.STRING)
	private NotificationType notifType;

	/**
	 * Default title of the notification for the {@link #notifType}
	 */
	@Column(nullable = false)
	@AttributeOverride(name = "jsonText", column = @Column(name = "title", columnDefinition = SQLConstants.TEXT_DEFINITION))
	private LocalizedString title;

	/**
	 * @see {@link #notifType}
	 */
	public NotificationType getNotifType() {
		return notifType;
	}

	/**
	 * @see {@link #notifType}
	 */
	public void setNotifType(NotificationType notifType) {
		this.notifType = notifType;
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

}
