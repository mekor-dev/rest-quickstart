/**
 * 
 */
package mekor.rest.quickstart.model.mappers.utils;

import mekor.rest.quickstart.model.dtos.utils.SingleIDDTO;
import mekor.rest.quickstart.model.entities.utils.SingleIDEntity;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * Generic methods used by SingleIDMapperToEntity and SingleIDMapperToDTO
 * 
 * @author mekor
 *
 */
public interface SingleIDMapper<E extends SingleIDEntity, DTO extends SingleIDDTO> {

	/**
	 * 
	 * @return The repository of the Entity managed by the mapper.
	 */
	@NoMapping
	public GenericRepostiory<E, Long> getRepo();

	/**
	 * 
	 * @param id The ID of the entity we want to retrieve
	 * 
	 * @return The Entity that has the ID given in parameter
	 */
	public default E entityFromEntityId(Long id) {
		return id == null ? null : getRepo().findBy(id);
	}

}
