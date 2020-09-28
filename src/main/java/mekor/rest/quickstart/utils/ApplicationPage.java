/**
 * 
 */
package mekor.rest.quickstart.utils;

import mekor.rest.quickstart.configuration.AppConfig;

/**
 * List of pages of the Web Application. Also store their relative path.
 * 
 * @author mekor
 *
 */
public enum ApplicationPage {

	FORGOTTEN_PASSWORD_CHANGE("/login/retrieve-password/{token}");

	/**
	 * The relative path of this page.
	 */
	private String path;

	private ApplicationPage(String path) {
		this.path = path;
	}

	/**
	 * Build a full URL with the front application root
	 * 
	 * @return The full URL.
	 */
	public String getUrl(AppConfig appConfig) {
		return appConfig.getFrontUrl() + path;
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

}
