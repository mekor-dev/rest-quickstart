/**
 * 
 */
package mekor.rest.quickstart.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import mekor.rest.quickstart.model.entities.mail.MailMessage;
import mekor.rest.quickstart.model.entities.mail.MailType;
import mekor.rest.quickstart.repositories.MailMessageRepository;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;
import mekor.rest.quickstart.services.utils.GenericEntityService;

/**
 * Service for managing {@link MailMessage}s.
 * 
 * @author mekor
 *
 */
@ApplicationScoped
public class MailMessageService extends GenericEntityService<MailMessage, MailType> {

	@Inject
	private Logger log;

	@Inject
	private MailMessageRepository mailMessageRepo;

	@Override
	protected GenericRepostiory<MailMessage, MailType> getEntityRepo() {
		return mailMessageRepo;
	}

	@Override
	protected Logger getLogger() {
		return log;
	}

}
