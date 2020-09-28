/**
 * 
 */
package mekor.rest.quickstart.model.dtos.file;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import mekor.rest.quickstart.model.dtos.utils.SingleIDDTO;
import mekor.rest.quickstart.model.entities.file.FileType;
import mekor.rest.quickstart.model.entities.utils.SQLConstants;

/**
 * 
 * Entity that holds information on an uploaded file.
 * 
 * @author mekor
 *
 */
public class FileEntityDTO extends SingleIDDTO {

	/**
	 * Path of the file
	 */
	@NotNull
	@Length(max = SQLConstants.TEXT_MAX_LENGTH)
	public String path;

	/**
	 * Type of the file
	 */
	@NotNull
	public FileType type;

	/**
	 * Tags applied to this file.
	 */
	public Set<FileTagDTO> tags;

}
