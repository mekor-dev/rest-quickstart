/**
 * 
 */
package mekor.rest.quickstart.api.utils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import mekor.rest.quickstart.model.entities.file.ImageType;

/**
 * MultipartForm for uploading a file to the server. <br />
 * Has a single parameter 'file' which contains the uploaded file as
 * octet-stream.
 * 
 * @author mekor
 *
 */
public class FileUploadForm {

	/**
	 * Name of the file to upload
	 */
	@NotNull
	@NotEmpty
	private String fileName;

	/**
	 * Type of the image.
	 */
	@NotNull
	private ImageType imageType;

	/**
	 * File to upload
	 */
	@NotNull
	@NotEmpty
	private byte[] file;

	@Override
	public String toString() {
		return "{fileName: " + fileName + ", imageType: " + imageType + "}";
	}

	/**
	 * @see {@link #fileName}
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @see {@link #fileName}
	 */
	@FormParam("fileName")
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @see {@link #file}
	 */
	public byte[] getFile() {
		return file;
	}

	/**
	 * @see {@link #file}
	 */
	@FormParam("file")
	@PartType("application/octet-stream")
	public void setFile(byte[] file) {
		this.file = file;
	}

	/**
	 * @see {@link #imageType}
	 */
	public ImageType getImageType() {
		return imageType;
	}

	/**
	 * @see {@link #imageType}
	 */
	@FormParam("imageType")
	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}

}
