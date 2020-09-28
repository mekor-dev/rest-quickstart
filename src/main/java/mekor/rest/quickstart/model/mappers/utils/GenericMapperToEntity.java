/**
 * 
 */
package mekor.rest.quickstart.model.mappers.utils;

import java.util.ArrayList;
import java.util.List;

import mekor.rest.quickstart.exceptions.MappingException;
import mekor.rest.quickstart.model.dtos.utils.GenericDTO;
import mekor.rest.quickstart.model.entities.utils.GenericEntity;

/**
 * Generic interface for mapping {@link GenericDTO} to {@link GenericEntity}
 * 
 * @param <E> Entity to map
 * @param <DTO> Base dto
 * 
 * @author mekor
 *
 */
public interface GenericMapperToEntity<E extends GenericEntity, DTO extends GenericDTO> {

	/**
	 * Maps a {@link GenericDTO} to a {@link GenericEntity} (must be overridden with
	 * Mapstruct annotations)
	 * 
	 * @param dto The DTO to maps
	 * 
	 * @return The mapped entity
	 * @throws MappingException @see {@link #entityFactory(GenericDTO, Class)}
	 */
	public E DTOToEntity(DTO dto) throws MappingException;

	/**
	 * Maps a list of {@link GenericDTO} to a list of {@link GenericEntity}
	 * 
	 * @param dtos The list of DTOs to map
	 * 
	 * @return The list of mapped entities
	 * @throws MappingException @see {@link #entityFactory(GenericDTO, Class)}
	 */
	public default List<E> DTOsToEntities(List<DTO> dtos) throws MappingException {
		if (dtos != null) {
			List<E> entities = new ArrayList<>();
			for (DTO dto : dtos) {
				entities.add(DTOToEntity(dto));
			}
			return entities;
		}
		return null;
	}
}
