/**
 * 
 */
package mekor.rest.quickstart.model.mappers.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mekor.rest.quickstart.exceptions.MappingException;
import mekor.rest.quickstart.model.dtos.utils.GenericDTO;
import mekor.rest.quickstart.model.entities.utils.GenericEntity;

/**
 * Generic interface for mapping {@link GenericEntity} to {@link GenericDTO}
 * 
 * @param <E> Base entity
 * @param <DTO> DTO to map
 * 
 * @author mekor
 *
 */
public interface GenericMapperToDTO<E extends GenericEntity, DTO extends GenericDTO> {

	/**
	 * Maps an {@link GenericEntity} to a {@link GenericDTO} (must be overridden
	 * with Mapstruct annotations)
	 * 
	 * @param entity The Entity to map
	 * 
	 * @return the mapped DTO
	 */
	public DTO entityToDTO(E entity) throws MappingException;

	/**
	 * Maps a list of {@link GenericEntity} to a list of {@link GenericDTO}
	 * 
	 * @param entities The list of entities to map
	 * 
	 * @return the list of mapped DTOs
	 */
	public default List<DTO> entitiesToDTOs(List<E> entities) throws MappingException {
		if (entities != null) {
			List<DTO> dtos = new ArrayList<>();
			for (E entity : entities) {
				dtos.add(entityToDTO(entity));
			}
			return dtos;
		}
		return null;
	}

	/**
	 * Maps a list of {@link GenericEntity} to a list of {@link GenericDTO}
	 * 
	 * @param entities The list of entities to map
	 * 
	 * @return the list of mapped DTOs
	 */
	public default List<DTO> entitiesToDTOs(Set<E> entities) throws MappingException {
		if (entities != null) {
			List<DTO> dtos = new ArrayList<>();
			for (E entity : entities) {
				dtos.add(entityToDTO(entity));
			}
			return dtos;
		}
		return null;
	}

}
