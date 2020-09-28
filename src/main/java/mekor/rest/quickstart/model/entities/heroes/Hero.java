package mekor.rest.quickstart.model.entities.heroes;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import mekor.rest.quickstart.model.entities.file.FileEntity;
import mekor.rest.quickstart.model.entities.utils.LocalizedString;
import mekor.rest.quickstart.model.entities.utils.SQLConstants;
import mekor.rest.quickstart.model.entities.utils.SingleIDEntity;

/**
 * 
 * A n in the game
 * 
 * @author mekor
 *
 */
@Entity
@Table
public class Hero extends SingleIDEntity {

	private static final long serialVersionUID = -7209903642615624264L;

	/**
	 * The name of the hero
	 */
	@Embedded
	@AttributeOverride(name = "jsonText", column = @Column(name = "name", columnDefinition = SQLConstants.TEXT_DEFINITION, nullable = false))
	public LocalizedString name;

	/**
	 * The avatar file of the hero
	 */
	@ManyToOne
	@JoinColumn(name = "avatarID", nullable = false)
	public FileEntity avatar;

	/**
	 * @see {@link #name}
	 */
	public LocalizedString getName() {
		return name;
	}

	/**
	 * @see {@link #name}
	 */
	public void setName(LocalizedString name) {
		this.name = name;
	}

	/**
	 * @see {@link #avatar}
	 */
	public FileEntity getAvatar() {
		return avatar;
	}

	/**
	 * @see {@link #avatar}
	 */
	public void setAvatar(FileEntity avatar) {
		this.avatar = avatar;
	}
}
