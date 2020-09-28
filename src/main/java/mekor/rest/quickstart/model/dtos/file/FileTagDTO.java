/**
 * 
 */
package mekor.rest.quickstart.model.dtos.file;

import javax.validation.constraints.NotNull;

import mekor.rest.quickstart.model.dtos.utils.SingleIDDTO;

/**
 * A tag added to a {@link FileEntityDTO}.
 * 
 * @author mekor
 *
 */
public class FileTagDTO extends SingleIDDTO {

	/**
	 * The tag
	 */
	@NotNull
	public String value;

}
