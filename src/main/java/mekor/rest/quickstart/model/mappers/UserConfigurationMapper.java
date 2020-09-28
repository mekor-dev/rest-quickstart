/**
 * 
 */
package mekor.rest.quickstart.model.mappers;

import org.mapstruct.Mapper;

import mekor.rest.quickstart.model.dtos.user.UserConfigurationDTO;
import mekor.rest.quickstart.model.entities.user.UserConfiguration;

/**
 * Map from {@link UserConfiguration} to {@link UserConfigurationDTO} and
 * backwards
 * 
 * @author mekor
 *
 */
@Mapper
public abstract class UserConfigurationMapper {

	/**
	 * Maps an {@link UserConfiguration} to a {@link UserConfigurationDTO} (must be
	 * overridden with Mapstruct annotations)
	 * 
	 * @param entity The Entity to map
	 * 
	 * @return the mapped DTO
	 */
	public abstract UserConfigurationDTO entityToDTO(UserConfiguration entity);

	/**
	 * Maps a {@link UserConfigurationDTO} to a {@link UserConfiguration} (must be
	 * overridden with Mapstruct annotations)
	 * 
	 * @param dto The DTO to maps
	 * 
	 * @return The mapped entity
	 */
	public abstract UserConfiguration DTOToEntity(UserConfigurationDTO dto);
}
