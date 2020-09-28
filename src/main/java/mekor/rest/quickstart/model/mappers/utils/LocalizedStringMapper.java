/**
 * 
 */
package mekor.rest.quickstart.model.mappers.utils;

import org.mapstruct.Mapper;

import mekor.rest.quickstart.exceptions.LocalizedStringInitException;
import mekor.rest.quickstart.model.dtos.utils.LocalizedStringDTO;
import mekor.rest.quickstart.model.entities.utils.LocalizedString;

/**
 * Map from {@link LocalizedString} to {@link LocalizedStringDTO} and backwards
 * 
 * @author mekor
 *
 */
@Mapper
public abstract class LocalizedStringMapper {

	/**
	 * Maps a {@link LocalizedString} to a {@link LocalizedStringDTO}
	 * 
	 * @param String the LocalizedString to map
	 * @return The mapped LocalizedStringDTO
	 * @throws LocalizedStringInitException @see {@link LocalizedString#init()}
	 */
	public LocalizedStringDTO localizedStringToDTO(LocalizedString string) throws LocalizedStringInitException {
		if (string == null) {
			return null;
		}
		LocalizedStringDTO res = new LocalizedStringDTO();
		res.map = string.getMap();
		return res;
	}

	/**
	 * Maps a {@link LocalizedStringDTO} to a {@link LocalizedString}
	 * 
	 * @param String the LocalizedStringDTO to map
	 * @return The mapped LocalizedString
	 */
	public LocalizedString DTOToLocalizedString(LocalizedStringDTO dto) {
		if (dto == null) {
			return null;
		}
		LocalizedString res = new LocalizedString();
		res.setMap(dto.map);
		return res;
	}
}
