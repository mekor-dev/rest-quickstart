package mekor.rest.quickstart.model.entities.file;

/**
 * Type of Image that the client indicates on uploads. Allow to resize image
 * depending on its type
 * 
 * @author mekor
 *
 */
public enum ImageType {

	USER_AVATAR(300, 300),
	USER_BANNER(1920, null),
	POST_ATTACHMENT(1920, null);

	private Integer maxWidth;

	private Integer maxHeight;

	private ImageType(Integer maxWidth, Integer maxHeight) {
		this.maxHeight = maxHeight;
		this.maxWidth = maxWidth;
	}

	/**
	 * @see {@link #maxHeight}
	 */
	public Integer getMaxHeight() {
		return maxHeight;
	}

	/**
	 * @see {@link #maxHeight}
	 */
	public void setMaxHeight(Integer maxHeight) {
		this.maxHeight = maxHeight;
	}

	/**
	 * @see {@link #maxWidth}
	 */
	public Integer getMaxWidth() {
		return maxWidth;
	}

	/**
	 * @see {@link #maxWidth}
	 */
	public void setMaxWidth(Integer maxWidth) {
		this.maxWidth = maxWidth;
	}

}
