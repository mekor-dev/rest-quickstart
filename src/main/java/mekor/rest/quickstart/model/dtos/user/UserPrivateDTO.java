package mekor.rest.quickstart.model.dtos.user;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;

import mekor.rest.quickstart.model.dtos.file.FileEntityDTO;
import mekor.rest.quickstart.model.dtos.utils.SingleIDDTO;
import mekor.rest.quickstart.utils.gson.GsonIgnore;

/**
 * DTO for retrieving the private informations of the user (used for user to get
 * their own infos).
 */
public class UserPrivateDTO extends SingleIDDTO {

	/**
	 * Email of the user
	 */
	@Email
	@Column(nullable = false, unique = true)
	public String email;

	/**
	 * Firstname of the user
	 */
	@Column(nullable = false)
	public String nickname;

	/**
	 * Configuration of the user
	 */
	@GsonIgnore
	@Embedded
	public UserConfigurationDTO configuration;

	/**
	 * Avatar of the user.
	 */
	@GsonIgnore
	@ManyToOne
	@JoinColumn(name = "avatarFileID")
	public FileEntityDTO avatar;

}
