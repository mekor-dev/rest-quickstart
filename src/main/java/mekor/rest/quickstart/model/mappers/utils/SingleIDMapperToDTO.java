/**
 * 
 */
package mekor.rest.quickstart.model.mappers.utils;

import mekor.rest.quickstart.model.dtos.utils.SingleIDDTO;
import mekor.rest.quickstart.model.entities.utils.SingleIDEntity;

/**
 * Generic interface for mapping {@link SingleIDEntity} to {@link SingleIDDTO}
 * 
 * @param <E> Base entity
 * @param <DTO> DTO to map
 * 
 * @author mekor
 *
 */
public interface SingleIDMapperToDTO<E extends SingleIDEntity, DTO extends SingleIDDTO> extends GenericMapperToDTO<E, DTO> {

	/**
	 * 
	 * @param entity The entity we want ID from
	 * 
	 * @return The ID of the entity
	 */
	public default Long entityIdFromEntity(E entity) {
		return entity == null ? null : entity.getId();
	}

}
