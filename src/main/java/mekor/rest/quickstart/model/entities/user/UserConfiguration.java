/**
 * 
 */
package mekor.rest.quickstart.model.entities.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Configuration of the user for using the site.
 * 
 * @author mekor
 *
 */
@Embeddable
public class UserConfiguration {
	/**
	 * The default language of the user
	 */
	@Column
	private String defaultLanguage;

	/**
	 * Privacy of the profile.
	 */
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserPrivacy privacy = UserPrivacy.PUBLIC;

	/**
	 * @see {@link #defaultLanguage}
	 */
	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	/**
	 * @see {@link #defaultLanguage}
	 */
	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	/**
	 * @see {@link #privacy}
	 */
	public UserPrivacy getPrivacy() {
		return privacy;
	}

	/**
	 * @see {@link #privacy}
	 */
	public void setPrivacy(UserPrivacy privacy) {
		this.privacy = privacy;
	}
}
