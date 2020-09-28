/**
 * 
 */
package mekor.rest.quickstart.model.mappers.utils;

import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;

import mekor.rest.quickstart.exceptions.MappingException;
import mekor.rest.quickstart.model.dtos.utils.SingleIDDTO;
import mekor.rest.quickstart.model.entities.utils.SingleIDEntity;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * Generic interface for mapping {@link SingleIDDTO} to {@link SingleIDEntity}
 * 
 * @param <E>   Entity to map
 * @param <DTO> Base dto
 * 
 * @author mekor
 *
 */
public interface SingleIDMapperToEntity<E extends SingleIDEntity, DTO extends SingleIDDTO> extends GenericMapperToEntity<E, DTO> {

	/**
	 * 
	 * @return The repository of the Entity managed by the mapper.
	 */
	public GenericRepostiory<E, Long> getRepo();

	/**
	 * Override the Entity creation. Check if the entity exists before creating it.
	 * If it exists, return the existing entity.
	 * 
	 * @param dto  The DTO from which we want to create an Entity
	 * @param type Type of the Entity to create
	 * @return If the Entity already exists, the Entity, a new instance otherwise.
	 * @throws MappingException Thrown if the instantiation of the class has
	 *                          failed. @see #{@link Class#newInstance()}
	 */
	@ObjectFactory
	public default E entityFactory(DTO dto, @TargetType Class<E> type) throws MappingException {
		if (dto.id != null) {
			E entity = getRepo().findBy(dto.id);
			if (entity != null) {
				return entity;
			}
		}
		try {
			return type.getDeclaredConstructor().newInstance();
		}
		catch (Exception e) {
			throw new MappingException("Error while creating entity of instance: " + type.getSimpleName(), e);
		}
	}

}
