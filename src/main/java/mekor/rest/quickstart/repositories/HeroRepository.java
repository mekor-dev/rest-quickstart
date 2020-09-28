package mekor.rest.quickstart.repositories;

import javax.enterprise.context.RequestScoped;

import org.apache.deltaspike.data.api.Repository;

import com.querydsl.core.types.dsl.EntityPathBase;

import mekor.rest.quickstart.model.entities.heroes.Hero;
import mekor.rest.quickstart.model.entities.heroes.QHero;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * 
 * Repository for the {@link Hero} entity
 * 
 * @author mekor
 *
 */
@Repository
@RequestScoped
public abstract class HeroRepository extends GenericRepostiory<Hero, Long> {

	@Override
	protected EntityPathBase<Hero> getQBean() {
		return QHero.hero;
	}

}
