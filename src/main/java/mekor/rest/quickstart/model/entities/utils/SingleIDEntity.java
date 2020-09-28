package mekor.rest.quickstart.model.entities.utils;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Superclass of every entities with a single ID. Unify the ID generation
 * 
 * @author mekor
 *
 */
@MappedSuperclass
public abstract class SingleIDEntity extends GenericEntity {

	private static final long serialVersionUID = -3633521427705056244L;

	/**
	 * Id of the entity in the database
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * @see {@link #id}
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @see {@link #id}
	 */
	public void setId(Long id) {
		this.id = id;
	}

}
