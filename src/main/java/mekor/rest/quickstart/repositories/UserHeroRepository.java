package mekor.rest.quickstart.repositories;

import javax.enterprise.context.RequestScoped;

import org.apache.deltaspike.data.api.Repository;

import com.querydsl.core.types.dsl.EntityPathBase;

import mekor.rest.quickstart.model.entities.heroes.QUserHero;
import mekor.rest.quickstart.model.entities.heroes.UserHero;
import mekor.rest.quickstart.model.entities.heroes.UserHeroPK;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * 
 * Repository for the {@link UserHero} entity
 * 
 * @author mekor
 *
 */
@Repository
@RequestScoped
public abstract class UserHeroRepository extends GenericRepostiory<UserHero, UserHeroPK> {

	@Override
	protected EntityPathBase<UserHero> getQBean() {
		return QUserHero.userHero;
	}

}
