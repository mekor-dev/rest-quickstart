/**
 * 
 */
package mekor.rest.quickstart.repositories;

import javax.enterprise.context.RequestScoped;

import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.SingleResultType;

import com.querydsl.core.types.dsl.EntityPathBase;

import mekor.rest.quickstart.model.entities.mail.MailMessage;
import mekor.rest.quickstart.model.entities.mail.MailType;
import mekor.rest.quickstart.model.entities.mail.QMailMessage;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * 
 * Repository for the {@link MailMessage} entity
 * 
 * @author mekor
 *
 */
@Repository
@RequestScoped
public abstract class MailMessageRepository extends GenericRepostiory<MailMessage, MailType> {

	@Query(value = "SELECT m FROM MailMessage m WHERE m.mailType = :type", singleResult = SingleResultType.JPA)
	public abstract MailMessage findByType(@QueryParam("type") MailType type);

	@Override
	protected EntityPathBase<MailMessage> getQBean() {
		return QMailMessage.mailMessage;
	}

}
