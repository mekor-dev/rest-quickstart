/**
 * 
 */
package mekor.rest.quickstart.model.dtos.user;

import com.sun.istack.NotNull;

import mekor.rest.quickstart.model.entities.user.UserPrivacy;

/**
 * Configuration of the user for using the site.
 * 
 * @author mekor
 *
 */
public class UserConfigurationDTO {

	/**
	 * The default language of the user
	 */
	public String defaultLanguage;

	/**
	 * Privacy of the profile.
	 */
	@NotNull
	public UserPrivacy privacy = UserPrivacy.PUBLIC;

}
