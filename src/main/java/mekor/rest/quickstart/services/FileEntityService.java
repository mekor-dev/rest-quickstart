/**
 * 
 */
package mekor.rest.quickstart.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.slf4j.Logger;

import mekor.rest.quickstart.api.utils.FileUploadForm;
import mekor.rest.quickstart.configuration.AppConfig;
import mekor.rest.quickstart.exceptions.FileUploadException;
import mekor.rest.quickstart.model.entities.file.FileEntity;
import mekor.rest.quickstart.model.entities.file.SupportedFileExtension;
import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.repositories.FileEntityRepository;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;
import mekor.rest.quickstart.services.utils.GenericEntityService;

/**
 * Service for managing {@link FileEntity}s.
 * 
 * @author mekor
 *
 */
@ApplicationScoped
public class FileEntityService extends GenericEntityService<FileEntity, Long> {

	@Inject
	private AppConfig appConfig;

	@Inject
	private FileEntityRepository fileEntityRepo;

	@Inject
	Logger log;

	/**
	 * Write a file from a {@link FileUploadForm} and create a {@link FileEntity}
	 * corresponding to the newly created file
	 * 
	 * @param form         The form containing data and fileName of the file to
	 *                     write.
	 * @param folderPath   The relative path in which we want to write the file.
	 * @param randomFolder True if we want to put the file in a folder with random
	 *                     name.
	 * @return FileEntity of the newly created file
	 * @throws FileUploadException
	 * @throws IOException
	 */
	public FileEntity writeFile(FileUploadForm form, boolean randomFolder, User uploader) throws FileUploadException {
		log.debug("Enterign writeFile(form: {}, randomFolder: {}, uploader: {})", form, randomFolder, uploader);

		try {
			String folderPath;
			// Verifying form
			String fileName = form.getFileName()
					.replaceAll(" ", "_")
					.replaceAll("/", "_");

			// Creating folder
			if (randomFolder) {
				String randomFolderPath = UUID.randomUUID().toString();
				folderPath = "/" + randomFolderPath;
			}
			else {
				folderPath = "/";
			}
			String absoluteFolderPath = appConfig.getCdnUploadRoot() + folderPath;
			File folder = new File(absoluteFolderPath);
			if (!folder.mkdirs()) {
				throw new FileUploadException("Could not prepare folders before uploading the file");
			}

			// Uploading
			File file = new File(absoluteFolderPath + "/" + fileName);

			if (!file.exists()) {
				file.createNewFile();
			}
			try (FileOutputStream fop = new FileOutputStream(file)) {
				fop.write(form.getFile());
				fop.flush();
			}
			catch (IOException e) {
				throw new FileUploadException("IOEXception while writing the new file", e);
			}

			// Creating FileEntity
			FileEntity entity = new FileEntity();
			entity.setPath(appConfig.getCdnRoot() + folderPath + "/" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()));
			entity.setUploader(uploader);
			entity.generateType();

			fileEntityRepo.persist(entity);
			log.debug("Leaving writeFile() -> {}", entity);
			return entity;
		}
		catch (Exception e) {
			throw new FileUploadException("Error while uploading a new file", e);
		}
	}

	public boolean resizeFile(FileUploadForm form, SupportedFileExtension ext) {
		log.debug("Entering resizeFile(form: {}, ext: {})", form, ext);
		// Check if it is an image and resize
		try (InputStream is = new ByteArrayInputStream(form.getFile())) {
			BufferedImage image = ImageIO.read(is);
			if (image == null) {
				return false;
			}
			BufferedImage resized = image;
			if (form.getImageType().getMaxHeight() == null && image.getWidth() > form.getImageType().getMaxWidth()) {

				log.debug("Resizing FIT_TO_WIDTH");
				resized = Scalr.resize(image, Mode.FIT_TO_WIDTH, form.getImageType().getMaxWidth(), 0);
			}
			else if (form.getImageType().getMaxWidth() == null && image.getHeight() > form.getImageType().getMaxHeight()) {
				log.debug("Resizing FIT_TO_HEIGHT");
				resized = Scalr.resize(image, Mode.FIT_TO_HEIGHT, 0, form.getImageType().getMaxHeight());
			}
			else if (form.getImageType().getMaxWidth() != null && form.getImageType().getMaxHeight() != null
					&& (image.getWidth() > form.getImageType().getMaxWidth() || image.getHeight() > form.getImageType().getMaxHeight())) {
				log.debug("Resizing AUTOMATIC");
				resized = Scalr.resize(image, Mode.AUTOMATIC, form.getImageType().getMaxWidth(), form.getImageType().getMaxHeight());
			}
			if (resized == null) {
				throw new FileUploadException("Error while resizing");
			}
			image.flush();
			try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
				ImageIO.write(resized, ext.toString(), os);
				form.setFile(os.toByteArray());
				return true;
			}
		}
		catch (IOException e) {
			throw new FileUploadException("Error while resizing the image", e);
		}
	}

	@Override
	protected GenericRepostiory<FileEntity, Long> getEntityRepo() {
		return fileEntityRepo;
	}

	@Override
	protected Logger getLogger() {
		return log;
	}

}
