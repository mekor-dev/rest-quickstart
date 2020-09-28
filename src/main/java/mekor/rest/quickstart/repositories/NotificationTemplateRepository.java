/**
 * 
 */
package mekor.rest.quickstart.repositories;

import javax.enterprise.context.RequestScoped;

import org.apache.deltaspike.data.api.Repository;

import com.querydsl.core.types.dsl.EntityPathBase;

import mekor.rest.quickstart.model.entities.notification.NotificationTemplate;
import mekor.rest.quickstart.model.entities.notification.NotificationType;
import mekor.rest.quickstart.model.entities.notification.QNotificationTemplate;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * 
 * Repository for the {@link NotificationTemplate} entity
 * 
 * @author mekor
 *
 */
@Repository
@RequestScoped
public abstract class NotificationTemplateRepository extends GenericRepostiory<NotificationTemplate, NotificationType> {

	@Override
	protected EntityPathBase<NotificationTemplate> getQBean() {
		return QNotificationTemplate.notificationTemplate;
	}

}
