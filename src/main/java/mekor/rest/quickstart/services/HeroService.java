package mekor.rest.quickstart.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import mekor.rest.quickstart.model.entities.heroes.Hero;
import mekor.rest.quickstart.repositories.HeroRepository;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;
import mekor.rest.quickstart.services.utils.GenericEntityService;

/**
 * Service for managing {@link Hero}.
 * 
 * @author mekor
 *
 */
@ApplicationScoped
public class HeroService extends GenericEntityService<Hero, Long> {

	@Inject
	private Logger log;

	@Inject
	private HeroRepository heroRepo;

	@Override
	protected GenericRepostiory<Hero, Long> getEntityRepo() {
		return heroRepo;
	}

	@Override
	protected Logger getLogger() {
		return log;
	}

}
