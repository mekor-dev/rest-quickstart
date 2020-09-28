/**
 * 
 */
package mekor.rest.quickstart.model.dtos.utils;

import java.util.Date;

import mekor.rest.quickstart.utils.gson.GsonUtils;

/**
 * 
 * Generic abstract class extended by every dtos. Add created and updated dates
 * and a flag to check if the object is deleted or not.
 * 
 * @author mekor
 *
 */
public abstract class GenericDTO {
	/**
	 * A deleted flag. If true, the entity won't be found in requests.
	 */
	public boolean deleted = false;

	/**
	 * Store the creation date of the entity
	 */
	public Date creationDate;

	/**
	 * Store the last update date of the entity
	 */
	public Date updateDate;

	@Override
	public String toString() {
		return GsonUtils.toJson(this);
	}

}
