/**
 * 
 */
package mekor.rest.quickstart.model.dtos.utils;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Superclass of every DTOs with a single ID.
 * 
 * @author mekor
 *
 */
@JsonPropertyOrder("id")
public abstract class SingleIDDTO extends GenericDTO {

	/**
	 * Id of the entity in the database
	 */
	public Long id;

}
