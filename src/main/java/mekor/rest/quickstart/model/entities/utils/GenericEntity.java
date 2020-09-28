/**
 * 
 */
package mekor.rest.quickstart.model.entities.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.deltaspike.data.api.audit.CreatedOn;
import org.apache.deltaspike.data.api.audit.ModifiedOn;
import org.apache.deltaspike.data.impl.audit.AuditEntityListener;

import mekor.rest.quickstart.utils.gson.GsonUtils;

/**
 * Generic abstract class extended by every entity. Add created and updated
 * dates a flag to check if the object is deleted or not.
 * 
 * @author mekor
 *
 */
@MappedSuperclass
@EntityListeners(value = AuditEntityListener.class)
public class GenericEntity implements Serializable {

	private static final long serialVersionUID = 381812014098072529L;

	/**
	 * A deleted flag. If true, the entity won't be found in requests.
	 */
	private boolean deleted = false;

	/**
	 * Store the creation date of the entity
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedOn
	private Date creationDate;

	/**
	 * Store the last update date of the entity
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@ModifiedOn(onCreate = true)
	private Date updateDate;

	/**
	 * Write the entity in JSON format
	 */
	@Override
	public String toString() {
		return GsonUtils.toJson(this);
	}

	/**
	 * @see {@link #deleted}
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @see {@link #deleted}
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * @see {@link #creationDate}
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @see {@link #creationDate}
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @see {@link #updateDate}
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @see {@link #updateDate}
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * Clear the target set and adds every object of source into it.<br />
	 * If target is null, just act as a normal setter.<br />
	 * <br />
	 * This method is used for replacing setters of CascadeType.ALL collections.
	 * 
	 * @param source The set of objects to copy into target
	 * @param target The target set in which to copy the source set of objects
	 */
	public static <E extends GenericEntity> Set<E> clearAndAdd(Set<E> source, Set<E> target) {
		if (target == null) {
			target = source;
		}
		else {
			target.clear();
			if (source != null) {
				target.addAll(source);
			}
		}
		return target;
	}

}
