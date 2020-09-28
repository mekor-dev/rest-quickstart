package mekor.rest.quickstart.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * Provides the EntityManager for repositories
 * 
 * @author mekor
 *
 */
@ApplicationScoped
public class EntityManagerProducer {

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@Produces
	@Default
	@RequestScoped
	public EntityManager create() {
		return this.entityManagerFactory.createEntityManager();
	}

	public void dispose(@Disposes @Default EntityManager entityManager) {
		if (entityManager.isOpen()) {
			entityManager.close();
		}
	}
}