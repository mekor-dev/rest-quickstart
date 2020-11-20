/**
 * 
 */
package mekor.rest.quickstart.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;

import com.webcohesion.enunciate.metadata.rs.TypeHint;

import mekor.rest.quickstart.api.utils.APIUtils;
import mekor.rest.quickstart.api.utils.CurrentRequest;
import mekor.rest.quickstart.api.utils.FileUploadForm;
import mekor.rest.quickstart.exceptions.FileUploadException;
import mekor.rest.quickstart.model.dtos.file.FileEntityDTO;
import mekor.rest.quickstart.model.entities.file.FileEntity;
import mekor.rest.quickstart.model.entities.file.SupportedFileExtension;
import mekor.rest.quickstart.model.mappers.FileEntityMapper;
import mekor.rest.quickstart.security.control.annotations.ControlLoggedIn;
import mekor.rest.quickstart.services.FileEntityService;

/**
 * API for uploading and managing files.
 * 
 * @author mekor
 *
 */
@RequestScoped
@Transactional
@Path("/files")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FileEntityAPI {

	public static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "gif", "png");

	@Context
	private UriInfo uriInfo;

	@Inject
	private Logger log;

	@Inject
	private APIUtils apiUtils;

	@Inject
	private CurrentRequest currentRequest;

	@Inject
	private FileEntityMapper fileEntityMapper;

	@Inject
	private FileEntityService fileEntityService;

	/**
	 * Get a file
	 * 
	 * @param id The id of the file to get
	 * @return The file
	 * @HTTP 200 File has been returned
	 */
	@GET
	@Path("/{id}")
	@TypeHint(FileEntityDTO.class)
	@ControlLoggedIn
	public Response getOne(@PathParam("id") Long id) {
		log.debug("Entering getOne({})", id);

		FileEntity file = fileEntityService.findByIDHandleNotFound(id, currentRequest.isAdmin());
		FileEntityDTO dto = fileEntityMapper.entityToDTO(file);
		log.debug("Leaving getOne() -> {}", dto);
		return Response.status(200).entity(dto).build();
	}

	/**
	 * Uploads a file
	 * 
	 * @param form The form with file data. The form has following parameters:
	 *             <br />
	 *             <ul>
	 *             <li><strong>file:</strong> The file to upload</li>
	 *             <li><strong>fileName:</strong> The name that the file must have
	 *             after upload</li>
	 *             <li><strong>imageType:</strong> The type of the image for
	 *             resizing (possible values : MCOC_CHAMPION_AVATAR, USER_AVATAR,
	 *             USER_BANNER, TEAM_AVATAR, TEAM_BANNER, POST_ATTACHMENT)</li>
	 *             </ul>
	 * 
	 * @return Information about the newly uploaded file
	 * @throws FileUploadException
	 * @throws IOException
	 * 
	 * @HTTP 201 The file has been uploaded
	 * @HTTP 400 Wrong file format or wrong file name
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@TypeHint(FileEntityDTO.class)
	@ControlLoggedIn
	public Response upload(@TypeHint(FileUploadForm.class) @MultipartForm @Valid FileUploadForm form) throws FileUploadException {
		log.debug("Entering upload({})", form.getFileName());

		// Check /
		if (form.getFileName().contains("/")) {
			log.debug("LeavingUpload() -> 400 : / in file name");
			return apiUtils.buildError(400, "The name of the file cannot contains '/' character", null, this.getClass()).build();
		}
		// Check and Get extension
		if (!form.getFileName().contains(".")) {
			log.debug("LeavingUpload() -> 400 : no . in file name");
			return apiUtils.buildError(400, "The name of the file must contains '.' character", null, this.getClass()).build();
		}
		String[] nameSplit = form.getFileName().split("\\.");
		SupportedFileExtension fileExt = SupportedFileExtension.get(nameSplit[(nameSplit.length - 1)]);
		if (fileExt == null) {
			log.debug("LeavingUpload() -> 400 : unsupported format ({})", fileExt);
			return apiUtils.buildError(400, "This extension is not supported. Supported extensions are : " + SUPPORTED_EXTENSIONS, null, this.getClass()).build();
		}

		// resizing
		if (!fileEntityService.resizeFile(form, fileExt)) {
			log.debug("LeavingUpload() -> 400 : Not an image or corrupted");
			return apiUtils.buildError(400, "The file is not an image or is corrupted", null, this.getClass()).build();
		}

		// Writing file
		FileEntity entity = fileEntityService.writeFile(form, true, currentRequest.getUser());

		log.debug("Leaving upload() -> {}", entity);
		return Response.status(201).entity(fileEntityMapper.entityToDTO(entity)).build();
	}
}
