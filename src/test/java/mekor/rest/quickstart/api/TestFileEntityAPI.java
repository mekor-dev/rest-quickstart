/**
 * 
 */
package mekor.rest.quickstart.api;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.deltaspike.core.api.resourceloader.InjectableResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.net.HttpHeaders;

import mekor.rest.quickstart.api.utils.error.APIError;
import mekor.rest.quickstart.configuration.AppConfig;
import mekor.rest.quickstart.configuration.ArquillianTest;
import mekor.rest.quickstart.configuration.TestResourcesURL;
import mekor.rest.quickstart.model.dtos.file.FileEntityDTO;
import mekor.rest.quickstart.model.entities.file.FileEntity;
import mekor.rest.quickstart.model.entities.file.FileType;
import mekor.rest.quickstart.services.FileEntityService;

/**
 * @author mekor
 *
 */
@RunWith(Arquillian.class)
public class TestFileEntityAPI extends ArquillianTest {

	@Inject
	private TestResourcesURL resourcesUrl;

	@Inject
	private FileEntityService fileEntityService;

	@Inject
	private AppConfig appConfig;

	@Inject
	@InjectableResource(location = "assets/test-upload.jpg")
	private InputStream testFile;

	@Inject
	@InjectableResource(location = "assets/test-upload-resize.jpg")
	private InputStream testResizeFile;

	@Inject
	@InjectableResource(location = "assets/test-upload-txt.txt")
	private InputStream testTextFile;

	@Inject
	@InjectableResource(location = "assets/test-upload-txt.jpg")
	private InputStream testTextJPGFile;

	@Test
	@InSequence(1)
	public void testGetOne() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(FileEntityAPI.class) + "/1");
		Response response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
				.get();

		Assert.assertEquals(200, response.getStatus());

		FileEntityDTO res = response.readEntity(FileEntityDTO.class);
		Assert.assertEquals(Long.valueOf(1), res.id);
		Assert.assertEquals("/hero_avatar.png", res.path);
		Assert.assertEquals(FileType.IMAGE, res.type);
		Assert.assertEquals(2, res.tags.size());
	}

	@Test
	@InSequence(2)
	public void testGetOneNotFound() {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(FileEntityAPI.class) + "/0");
		Response response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
				.get();

		Assert.assertEquals(404, response.getStatus());
	}

	@Test
	@InSequence(3)
	public void testUploadWrongNull() throws InterruptedException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		testFile.transferTo(baos);
		byte[] file = baos.toByteArray();
		// file null
		{
			ResteasyClient client = new ResteasyClientBuilder().build();

			MultipartFormDataOutput mdo = new MultipartFormDataOutput();
			mdo.addFormData("fileName", "test-upload.exe", MediaType.TEXT_PLAIN_TYPE);
			mdo.addFormData("imageType", "USER_AVATAR", MediaType.TEXT_PLAIN_TYPE);

			WebTarget target = client.target(resourcesUrl.get(FileEntityAPI.class));
			Response response = target.request()
					.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
					.post(Entity.entity(mdo, MediaType.MULTIPART_FORM_DATA_TYPE));

			Assert.assertEquals(400, response.getStatus());

			APIError res = response.readEntity(APIError.class);

			Assert.assertTrue(res.exception.message.contains("upload.form.file: ne peut pas être nul"));
			Assert.assertTrue(res.exception.message.contains("upload.form.file: ne peut pas être vide"));
		}
		// fileName null
		{
			InputStream is = new ByteArrayInputStream(file);
			ResteasyClient client = new ResteasyClientBuilder().build();

			MultipartFormDataOutput mdo = new MultipartFormDataOutput();
			mdo.addFormData("file", is, MediaType.APPLICATION_OCTET_STREAM_TYPE);
			mdo.addFormData("imageType", "USER_AVATAR", MediaType.TEXT_PLAIN_TYPE);

			WebTarget target = client.target(resourcesUrl.get(FileEntityAPI.class));
			Response response = target.request()
					.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
					.post(Entity.entity(mdo, MediaType.MULTIPART_FORM_DATA_TYPE));

			Assert.assertEquals(400, response.getStatus());

			APIError res = response.readEntity(APIError.class);

			Assert.assertTrue(res.exception.message.contains("upload.form.fileName: ne peut pas être nul"));
			Assert.assertTrue(res.exception.message.contains("upload.form.fileName: ne peut pas être vide"));
		}
		// imageType null
		{
			InputStream is = new ByteArrayInputStream(file);
			ResteasyClient client = new ResteasyClientBuilder().build();

			MultipartFormDataOutput mdo = new MultipartFormDataOutput();
			mdo.addFormData("file", is, MediaType.APPLICATION_OCTET_STREAM_TYPE);
			mdo.addFormData("fileName", "test-upload.exe", MediaType.TEXT_PLAIN_TYPE);

			WebTarget target = client.target(resourcesUrl.get(FileEntityAPI.class));
			Response response = target.request()
					.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
					.post(Entity.entity(mdo, MediaType.MULTIPART_FORM_DATA_TYPE));

			Assert.assertEquals(400, response.getStatus());

			APIError res = response.readEntity(APIError.class);

			Assert.assertEquals("upload.form.imageType: ne peut pas être nul", res.exception.message);
		}
	}

	@Test
	@InSequence(3)
	public void testUploadWrongEmpty() {
		// fileName empty
		{
			ResteasyClient client = new ResteasyClientBuilder().build();

			MultipartFormDataOutput mdo = new MultipartFormDataOutput();
			mdo.addFormData("file", testFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
			mdo.addFormData("fileName", "", MediaType.TEXT_PLAIN_TYPE);
			mdo.addFormData("imageType", "USER_AVATAR", MediaType.TEXT_PLAIN_TYPE);

			WebTarget target = client.target(resourcesUrl.get(FileEntityAPI.class));
			Response response = target.request()
					.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
					.post(Entity.entity(mdo, MediaType.MULTIPART_FORM_DATA_TYPE));

			Assert.assertEquals(400, response.getStatus());

			APIError res = response.readEntity(APIError.class);

			Assert.assertEquals("upload.form.fileName: ne peut pas être vide", res.exception.message);
		}
	}

	@Test
	@InSequence(3)
	public void testUploadWrongExt() {
		{
			ResteasyClient client = new ResteasyClientBuilder().build();

			MultipartFormDataOutput mdo = new MultipartFormDataOutput();
			mdo.addFormData("file", testFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
			mdo.addFormData("fileName", "test-upload.exe", MediaType.TEXT_PLAIN_TYPE);
			mdo.addFormData("imageType", "USER_AVATAR", MediaType.TEXT_PLAIN_TYPE);

			WebTarget target = client.target(resourcesUrl.get(FileEntityAPI.class));
			Response response = target.request()
					.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
					.post(Entity.entity(mdo, MediaType.MULTIPART_FORM_DATA_TYPE));

			Assert.assertEquals(400, response.getStatus());

			APIError res = response.readEntity(APIError.class);

			Assert.assertEquals("This extension is not supported. Supported extensions are : [jpg, jpeg, gif, png]", res.message);
		}
	}

	@Test
	@InSequence(3)
	public void testUploadWrongNotAnImage() {
		// File txt and fileName jpg
		{
			ResteasyClient client = new ResteasyClientBuilder().build();

			MultipartFormDataOutput mdo = new MultipartFormDataOutput();
			mdo.addFormData("file", testTextFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
			mdo.addFormData("fileName", "test-upload.jpg", MediaType.TEXT_PLAIN_TYPE);
			mdo.addFormData("imageType", "USER_AVATAR", MediaType.TEXT_PLAIN_TYPE);

			WebTarget target = client.target(resourcesUrl.get(FileEntityAPI.class));
			Response response = target.request()
					.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
					.post(Entity.entity(mdo, MediaType.MULTIPART_FORM_DATA_TYPE));

			Assert.assertEquals(400, response.getStatus());

			APIError res = response.readEntity(APIError.class);

			Assert.assertEquals("The file is not an image or is corrupted", res.message);
		}
		// File false jpg and fileName jpg
		{
			ResteasyClient client = new ResteasyClientBuilder().build();

			MultipartFormDataOutput mdo = new MultipartFormDataOutput();
			mdo.addFormData("file", testTextJPGFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
			mdo.addFormData("fileName", "test-upload.jpg", MediaType.TEXT_PLAIN_TYPE);
			mdo.addFormData("imageType", "USER_AVATAR", MediaType.TEXT_PLAIN_TYPE);

			WebTarget target = client.target(resourcesUrl.get(FileEntityAPI.class));
			Response response = target.request()
					.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
					.post(Entity.entity(mdo, MediaType.MULTIPART_FORM_DATA_TYPE));

			Assert.assertEquals(400, response.getStatus());

			APIError res = response.readEntity(APIError.class);

			Assert.assertEquals("The file is not an image or is corrupted", res.message);
		}
	}

	@Test
	@InSequence(4)
	public void testUploadWrongNoDot() {
		ResteasyClient client = new ResteasyClientBuilder().build();

		MultipartFormDataOutput mdo = new MultipartFormDataOutput();
		mdo.addFormData("file", testFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
		mdo.addFormData("fileName", "test-uploadexe", MediaType.TEXT_PLAIN_TYPE);
		mdo.addFormData("imageType", "USER_AVATAR", MediaType.TEXT_PLAIN_TYPE);

		WebTarget target = client.target(resourcesUrl.get(FileEntityAPI.class));
		Response response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
				.post(Entity.entity(mdo, MediaType.MULTIPART_FORM_DATA_TYPE));

		Assert.assertEquals(400, response.getStatus());

		APIError res = response.readEntity(APIError.class);

		Assert.assertEquals("The name of the file must contains '.' character", res.message);
	}

	@Test
	@InSequence(5)
	public void testUploadWrongSlash() {
		ResteasyClient client = new ResteasyClientBuilder().build();

		MultipartFormDataOutput mdo = new MultipartFormDataOutput();
		mdo.addFormData("file", testFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
		mdo.addFormData("fileName", "test-/upload.pdf", MediaType.TEXT_PLAIN_TYPE);
		mdo.addFormData("imageType", "USER_AVATAR", MediaType.TEXT_PLAIN_TYPE);

		WebTarget target = client.target(resourcesUrl.get(FileEntityAPI.class));
		Response response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
				.post(Entity.entity(mdo, MediaType.MULTIPART_FORM_DATA_TYPE));

		Assert.assertEquals(400, response.getStatus());

		APIError res = response.readEntity(APIError.class);

		Assert.assertEquals("The name of the file cannot contains '/' character", res.message);
	}

	@Test
	@InSequence(6)
	public void testUploadNoResize() throws IOException {
		ResteasyClient client = new ResteasyClientBuilder().build();

		MultipartFormDataOutput mdo = new MultipartFormDataOutput();
		mdo.addFormData("file", testFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
		mdo.addFormData("fileName", "test-upload.JPeg", MediaType.TEXT_PLAIN_TYPE);
		mdo.addFormData("imageType", "USER_AVATAR", MediaType.TEXT_PLAIN_TYPE);

		WebTarget target = client.target(resourcesUrl.get(FileEntityAPI.class));
		Response response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
				.post(Entity.entity(mdo, MediaType.MULTIPART_FORM_DATA_TYPE));

		Assert.assertEquals(201, response.getStatus());

		FileEntityDTO res = response.readEntity(FileEntityDTO.class);

		Assert.assertNotNull(res.id);
		Assert.assertTrue(res.path.endsWith("test-upload.JPeg"));
		Assert.assertTrue(res.path.startsWith(appConfig.getCdnRoot()));

		FileEntity entity = fileEntityService.findByID(res.id);
		Assert.assertEquals(Long.valueOf(1), entity.getUploader().getId());

		Response responseFile = client.target(res.path).request().get();
		Assert.assertEquals(200, responseFile.getStatus());
		Assert.assertEquals("image/jpeg", responseFile.getHeaders().getFirst("Content-Type"));

		InputStream file = responseFile.readEntity(InputStream.class);
		BufferedImage image = ImageIO.read(file);
		Assert.assertEquals(72, image.getWidth());
		Assert.assertEquals(44, image.getHeight());
	}

	@Test
	@InSequence(7)
	public void testUploadResizeWidth() throws IOException {
		ResteasyClient client = new ResteasyClientBuilder().build();

		MultipartFormDataOutput mdo = new MultipartFormDataOutput();
		mdo.addFormData("file", testResizeFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
		mdo.addFormData("fileName", "test-upload.JPeg", MediaType.TEXT_PLAIN_TYPE);
		mdo.addFormData("imageType", "USER_BANNER", MediaType.TEXT_PLAIN_TYPE);

		WebTarget target = client.target(resourcesUrl.get(FileEntityAPI.class));
		Response response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
				.post(Entity.entity(mdo, MediaType.MULTIPART_FORM_DATA_TYPE));

		Assert.assertEquals(201, response.getStatus());

		FileEntityDTO res = response.readEntity(FileEntityDTO.class);

		Assert.assertNotNull(res.id);
		Assert.assertTrue(res.path.endsWith("test-upload.JPeg"));
		Assert.assertTrue(res.path.startsWith(appConfig.getCdnRoot()));

		FileEntity entity = fileEntityService.findByID(res.id);
		Assert.assertEquals(Long.valueOf(1), entity.getUploader().getId());

		Response responseFile = client.target(res.path).request().get();
		Assert.assertEquals(200, responseFile.getStatus());
		Assert.assertEquals("image/jpeg", responseFile.getHeaders().getFirst("Content-Type"));

		InputStream file = responseFile.readEntity(InputStream.class);
		BufferedImage image = ImageIO.read(file);
		Assert.assertEquals(1920, image.getWidth());
		Assert.assertEquals(1440, image.getHeight());
	}

	@Test
	@InSequence(7)
	public void testUploadResizeAuto() throws IOException {
		ResteasyClient client = new ResteasyClientBuilder().build();

		MultipartFormDataOutput mdo = new MultipartFormDataOutput();
		mdo.addFormData("file", testResizeFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
		mdo.addFormData("fileName", "test-upload.JPeg", MediaType.TEXT_PLAIN_TYPE);
		mdo.addFormData("imageType", "USER_AVATAR", MediaType.TEXT_PLAIN_TYPE);

		WebTarget target = client.target(resourcesUrl.get(FileEntityAPI.class));
		Response response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
				.post(Entity.entity(mdo, MediaType.MULTIPART_FORM_DATA_TYPE));

		Assert.assertEquals(201, response.getStatus());

		FileEntityDTO res = response.readEntity(FileEntityDTO.class);

		Assert.assertNotNull(res.id);
		Assert.assertTrue(res.path.endsWith("test-upload.JPeg"));
		Assert.assertTrue(res.path.startsWith(appConfig.getCdnRoot()));

		FileEntity entity = fileEntityService.findByID(res.id);
		Assert.assertEquals(Long.valueOf(1), entity.getUploader().getId());

		Response responseFile = client.target(res.path).request().get();
		Assert.assertEquals(200, responseFile.getStatus());
		Assert.assertEquals("image/jpeg", responseFile.getHeaders().getFirst("Content-Type"));

		InputStream file = responseFile.readEntity(InputStream.class);
		BufferedImage image = ImageIO.read(file);
		Assert.assertEquals(300, image.getWidth());
		Assert.assertEquals(225, image.getHeight());
	}

	@Test
	@InSequence(1000)
	public void cleanUploads() {
		File folder = new File(appConfig.getCdnUploadRoot());
		deleteFolderContent(folder);
	}

	public void deleteFolderContent(File folder) {
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolderContent(f);
				}
				Assert.assertTrue(f.delete());
			}
		}
	}

}
