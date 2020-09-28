/**
 * 
 */
package mekor.rest.quickstart.model.entities.file;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import mekor.rest.quickstart.model.entities.utils.SingleIDEntity;
import mekor.rest.quickstart.utils.gson.GsonIgnore;

/**
 * A tag added to a {@link FileEntity}.
 * 
 * @author mekor
 *
 */
@Entity
@Table
public class FileTag extends SingleIDEntity {

	private static final long serialVersionUID = 2484966191866689620L;

	/**
	 * The tag
	 */
	@Column(nullable = false)
	private String value;

	/**
	 * The file which owns this tag
	 */
	@GsonIgnore
	@ManyToOne
	@JoinColumn(name = "fileID", nullable = false)
	private FileEntity file;

	/**
	 * @see {@link #file}
	 */
	public FileEntity getFile() {
		return file;
	}

	/**
	 * @see {@link #file}
	 */
	public void setFile(FileEntity file) {
		this.file = file;
	}

	/**
	 * @see {@link #value}
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @see {@link #value}
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
