package mekor.rest.quickstart.model.entities.user;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import mekor.rest.quickstart.model.entities.file.FileEntity;
import mekor.rest.quickstart.model.entities.heroes.UserHero;
import mekor.rest.quickstart.model.entities.user.credentials.PasswordCredentials;
import mekor.rest.quickstart.model.entities.utils.SingleIDEntity;
import mekor.rest.quickstart.utils.gson.GsonIgnore;

/**
 * Users of the application.
 * 
 * @author mekor
 *
 */
@Entity
@Table
public class User extends SingleIDEntity {

	private static final long serialVersionUID = -7282717109756570249L;

	/**
	 * Email of the user
	 */
	@Email
	@Column(nullable = false, unique = true)
	private String email;

	/**
	 * Firstname of the user
	 */
	@Column(nullable = false)
	private String nickname;

	/**
	 * True if the user is admin
	 */
	@Column
	private Boolean isAdmin;

	/**
	 * Configuration of the user
	 */
	@GsonIgnore
	@Embedded
	private UserConfiguration configuration;

	/**
	 * Password of the user
	 */
	@GsonIgnore
	@OneToOne(mappedBy = "user")
	private PasswordCredentials passwordCredentials;

	/**
	 * Avatar of the user.
	 */
	@GsonIgnore
	@ManyToOne
	@JoinColumn(name = "avatarFileID")
	private FileEntity avatar;

	/**
	 * List of heroes that the {@link UserHero} owns.
	 */
	@GsonIgnore
	@OneToMany(mappedBy = "user")
	private Set<UserHero> userHeroes;

	/**
	 * @see {@link #email}
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @see {@link #email}
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @see {@link #passwordCredentials}
	 */
	public PasswordCredentials getPasswordCredentials() {
		return passwordCredentials;
	}

	/**
	 * @see {@link #passwordCredentials}
	 */
	public void setPasswordCredentials(PasswordCredentials passwordCredentials) {
		this.passwordCredentials = passwordCredentials;
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

	/**
	 * @see {@link #configuration}
	 */
	public UserConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * @see {@link #configuration}
	 */
	public void setConfiguration(UserConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * @see {@link #nickname}
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @see {@link #nickname}
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @see {@link #userHeroes}
	 */
	public Set<UserHero> getUserHeroes() {
		return userHeroes;
	}

	/**
	 * @see {@link #userHeroes}
	 */
	public void setUserHeroes(Set<UserHero> userHeroes) {
		this.userHeroes = userHeroes;
	}

	/**
	 * @see {@link #isAdmin}
	 */
	public Boolean getIsAdmin() {
		return isAdmin;
	}

	/**
	 * @see {@link #isAdmin}
	 */
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}
