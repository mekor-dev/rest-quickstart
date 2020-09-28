package mekor.rest.quickstart.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import mekor.rest.quickstart.model.entities.heroes.Hero;
import mekor.rest.quickstart.model.entities.heroes.UserHero;
import mekor.rest.quickstart.model.entities.heroes.UserHeroPK;
import mekor.rest.quickstart.repositories.UserHeroRepository;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;
import mekor.rest.quickstart.services.utils.GenericEntityService;

/**
 * Service for managing {@link Hero}.
 * 
 * @author mekor
 *
 */
@ApplicationScoped
public class UserHeroService extends GenericEntityService<UserHero, UserHeroPK> {

	@Inject
	private Logger log;

	@Inject
	private UserHeroRepository userHeroRepo;

	@Override
	protected GenericRepostiory<UserHero, UserHeroPK> getEntityRepo() {
		return userHeroRepo;
	}

	@Override
	protected Logger getLogger() {
		return log;
	}

}
