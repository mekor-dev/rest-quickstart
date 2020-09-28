/**
 * 
 */
package mekor.rest.quickstart.services.utils;

import java.io.Serializable;

import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;

import mekor.rest.quickstart.exceptions.LazyLoadingQueryException;
import mekor.rest.quickstart.model.entities.utils.GenericEntity;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingConfiguration;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingResult;

/**
 * @author mekor
 *
 */
public abstract class GenericEntityService<E extends GenericEntity, PK extends Serializable> {

	/**
	 * Find the entity by ID.
	 * 
	 * @param id ID of the entity
	 * @return The entity
	 */
	public E findByID(PK id) {
		getLogger().debug("Entering findByID(id: {})", id);

		E res = getEntityRepo().findBy(id);

		getLogger().debug("Leaving findByID() -> {}", res);
		return res;
	}

	/**
	 * Find the entity by ID and throw a {@link NotFoundException} if the entity
	 * does not exists.
	 * 
	 * @param id      ID of the entity
	 * @param isAdmin true if the current user is admin
	 * @param message Message of the EntityNotFoundException thrown
	 * 
	 * @return
	 */
	public E findByIDHandleNotFound(PK id, boolean isAdmin) {
		getLogger().debug("Entering findByIDHandleNotFound(id: {}, isAdmin: {})", id, isAdmin);

		String entityName = getEntityRepo().getEntityName();

		E res = getEntityRepo().findBy(id);
		String message = "No " + entityName + " found with ID " + id;
		return handleNotFound(res, isAdmin, message);
	}

	/**
	 * throw a {@link NotFoundException} if the entity does not exists.
	 * 
	 * @param entity  The entity
	 * @param isAdmin true if the current user is admin
	 * @return
	 */
	public <T extends GenericEntity> T handleNotFound(T entity, boolean isAdmin, String exceptionMessage) {
		getLogger().debug("Entering handleNotFound(entity: {})", entity);

		if (entity == null) {
			getLogger().debug("Leaving handleNotFound() -> 404");
			throw new NotFoundException(exceptionMessage);
		}
		if (entity.isDeleted() && !isAdmin) {
			getLogger().debug("Leaving handleNotFound() -> 404");
			throw new NotFoundException(exceptionMessage);
		}

		getLogger().debug("Leaving handleNotFound() -> {}", entity);
		return entity;

	}

	/**
	 * Fetch a list of the managed entity, with LazyLoadingConfiguration given in
	 * parameter. Return LazyLoadingResponse, with entities list and pagination info
	 * 
	 * @return The result of the query, with pagination info
	 * @throws LazyLoadingQueryException
	 */
	public LazyLoadingResult<E> findLazyLoading(LazyLoadingConfiguration configuration) throws LazyLoadingQueryException {
		getLogger().debug("Entering findLazyLoading(configuration: {})", configuration);

		LazyLoadingResult<E> result = getEntityRepo().findAllLazyLoading(configuration);

		getLogger().debug("Leaving findLazyLoading() -> {}", result);
		return result;
	}

	/**
	 * Persist the entity
	 * 
	 * @param entity The entity to persist
	 */
	public void persist(E entity) {
		getLogger().debug("Entering persist(entity: {})", entity);
		getEntityRepo().persist(entity);
		getLogger().debug("Leaving persist()");
	}

	/**
	 * Merge the entity
	 * 
	 * @param entity The entity to merge
	 */
	public void merge(E entity) {
		getLogger().debug("Entering merge(entity: {})", entity);
		getEntityRepo().merge(entity);
		getLogger().debug("Leaving merge()");
	}

	/**
	 * Remove an entity by ID
	 * 
	 * @param id ID of the entity to remove
	 */
	public void remove(E entity) {
		getLogger().debug("Entering remove(entity: {})", entity);
		getEntityRepo().remove(entity);
		getLogger().debug("Leaving remove()");
	}

	/**
	 * 
	 * @return The repository of the entity managed by this service.
	 */
	protected abstract GenericRepostiory<E, PK> getEntityRepo();

	/**
	 * Must be implemented to return the logger of the actual called service
	 * 
	 * @return
	 */
	protected abstract Logger getLogger();

}
