/**
 * 
 */
package mekor.rest.quickstart.model.entities.file;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.model.entities.utils.SQLConstants;
import mekor.rest.quickstart.model.entities.utils.SingleIDEntity;
import mekor.rest.quickstart.utils.gson.GsonIgnore;

/**
 * 
 * Entity that holds information on an uploaded file.
 * 
 * @author mekor
 *
 */
@Entity
@Table
public class FileEntity extends SingleIDEntity {

	private static final long serialVersionUID = -2035778377087858420L;

	/**
	 * Path of the file
	 */
	@Column(columnDefinition = SQLConstants.TEXT_DEFINITION, nullable = false)
	private String path;

	/**
	 * Type of the file
	 */
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private FileType type;

	@GsonIgnore
	@ManyToOne
	@JoinColumn(name = "uploaderID")
	private User uploader;

	/**
	 * Tags applied to this file.
	 */
	@GsonIgnore
	@OneToMany(mappedBy = "file")
	private Set<FileTag> tags;

	@GsonIgnore
	@OneToMany(mappedBy = "avatar")
	private Set<User> avatarUser;

	/**
	 * Generate the type of the file, depending of name of the file.
	 */
	public void generateType() {
		String lowerCasePath = path.toLowerCase();
		if (lowerCasePath.endsWith(".jpg") || lowerCasePath.endsWith(".jpeg") || lowerCasePath.endsWith(".png") || lowerCasePath.endsWith(".gif")) {
			type = FileType.IMAGE;
		}
		else if (lowerCasePath.endsWith(".pdf")) {
			type = FileType.PDF;
		}
		else if (lowerCasePath.endsWith(".mp4") || lowerCasePath.endsWith(".mov") || lowerCasePath.endsWith(".avi")) {
			type = FileType.VIDEO;
		}
		else {
			type = FileType.RAW;
		}
	}

	/**
	 * @see {@link #path}
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @see {@link #path}
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @see {@link #type}
	 */
	public FileType getType() {
		return type;
	}

	/**
	 * @see {@link #type}
	 */
	public void setType(FileType type) {
		this.type = type;
	}

	/**
	 * @see {@link #tags}
	 */
	public Set<FileTag> getTags() {
		return tags;
	}

	/**
	 * @see {@link #tags}
	 */
	public void setTags(Set<FileTag> tags) {
		this.tags = tags;
	}

	/**
	 * @see {@link #uploader}
	 */
	public User getUploader() {
		return uploader;
	}

	/**
	 * @see {@link #uploader}
	 */
	public void setUploader(User uploader) {
		this.uploader = uploader;
	}

	/**
	 * @see {@link #avatarUser}
	 */
	public Set<User> getAvatarUser() {
		return avatarUser;
	}

	/**
	 * @see {@link #avatarUser}
	 */
	public void setAvatarUser(Set<User> avatarUser) {
		this.avatarUser = avatarUser;
	}
}
