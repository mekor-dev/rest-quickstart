/**
 * 
 */
package mekor.rest.quickstart.repositories;

import javax.enterprise.context.RequestScoped;

import org.apache.deltaspike.data.api.Repository;

import com.querydsl.core.types.dsl.EntityPathBase;

import mekor.rest.quickstart.model.entities.notification.Notification;
import mekor.rest.quickstart.model.entities.notification.QNotification;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * 
 * Repository for the {@link Notification} entity
 * 
 * @author mekor
 *
 */
@Repository
@RequestScoped
public abstract class NotificationRepository extends GenericRepostiory<Notification, Long> {

	@Override
	protected EntityPathBase<Notification> getQBean() {
		return QNotification.notification;
	}

}
