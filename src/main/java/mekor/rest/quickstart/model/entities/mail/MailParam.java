/**
 * 
 */
package mekor.rest.quickstart.model.entities.mail;

/**
 * Params that can be set in mails.
 * 
 * @author mekor
 *
 */
public enum MailParam {
	USERNAME("userName"),
	FORGOTTEN_PASSWORD_LINK("forgotten_password_link");

	/**
	 * name of the param. If a param with name "param" is given, every occurence of
	 * "{param}" will be replaced in the mail.
	 */
	private String name;

	private MailParam(String name) {
		this.name = name;
	}

	/**
	 * @see {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see {@link #name}
	 */
	public void setName(String name) {
		this.name = name;
	}
}
