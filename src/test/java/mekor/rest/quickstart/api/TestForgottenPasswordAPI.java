/**
 * 
 */
package mekor.rest.quickstart.api;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.mock_javamail.Mailbox;

import mekor.rest.quickstart.api.ForgottenPasswordAPI.ChangePasswordBody;
import mekor.rest.quickstart.api.ForgottenPasswordAPI.CheckTokenBody;
import mekor.rest.quickstart.api.ForgottenPasswordAPI.RequestTokenBody;
import mekor.rest.quickstart.api.utils.error.APIError;
import mekor.rest.quickstart.configuration.ArquillianTest;
import mekor.rest.quickstart.configuration.TestResourcesURL;
import mekor.rest.quickstart.model.entities.user.credentials.ForgottenPasswordToken;
import mekor.rest.quickstart.repositories.ForgottenPasswordTokenRepository;
import mekor.rest.quickstart.repositories.UserRepository;
import mekor.rest.quickstart.services.ForgottenPasswordTokenService;
import mekor.rest.quickstart.services.UserService;

/**
 * @author mekor
 *
 */
@RunWith(Arquillian.class)
public class TestForgottenPasswordAPI extends ArquillianTest {

	@Inject
	private TestResourcesURL resourcesUrl;

	@Inject
	private UserService userService;

	@Inject
	private UserRepository userRepo;

	@Inject
	private ForgottenPasswordTokenRepository tokenRepo;

	@Before
	public void before() {
		Mailbox.clearAll();
	}

	@Test
	@InSequence(1)
	public void testRequestTokenWrongEmail() throws AddressException {
		Long nbToken = tokenRepo.count();

		// 400
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(ForgottenPasswordAPI.class));

		RequestTokenBody body = new RequestTokenBody();
		body.email = "pwet";

		Response response = target.request()
				.post(Entity.entity(body, MediaType.APPLICATION_JSON));

		Assert.assertEquals(400, response.getStatus());

		tokenRepo.clear();
		Assert.assertEquals(nbToken, tokenRepo.count());

		// 204 - but no new token
		client = ClientBuilder.newClient();
		target = client.target(resourcesUrl.get(ForgottenPasswordAPI.class));

		body = new RequestTokenBody();
		body.email = "pwet@pwet.com";

		response = target.request()
				.post(Entity.entity(body, MediaType.APPLICATION_JSON));

		Assert.assertEquals(204, response.getStatus());

		tokenRepo.clear();
		Assert.assertEquals(nbToken, tokenRepo.count());

		Assert.assertEquals(0, Mailbox.get("pwet@pwet.com").size());
	}

	@Test
	@InSequence(2)
	public void testRequestTokenReplace() throws InterruptedException, IOException, MessagingException {
		Date minDate = new DateTime().plusHours(ForgottenPasswordTokenService.VALIDITY_DURATION).toDate();
		Thread.sleep(1000);

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(ForgottenPasswordAPI.class));

		RequestTokenBody body = new RequestTokenBody();
		body.email = "gerald.ford@mail.com";

		Response response = target.request()
				.post(Entity.entity(body, MediaType.APPLICATION_JSON));

		Assert.assertEquals(204, response.getStatus());

		ForgottenPasswordToken token = tokenRepo.noCacheFindBy(4L);
		Assert.assertTrue(token.getExpirationDate().after(minDate));
		Assert.assertTrue(token.getExpirationDate().before(new DateTime().plusHours(ForgottenPasswordTokenService.VALIDITY_DURATION).toDate()));
		Assert.assertNotEquals(token.getToken(), "replace API");
		Assert.assertEquals(Long.valueOf(7), token.getUser().getId());

		List<Message> inbox = Mailbox.get("gerald.ford@mail.com");

		Assert.assertEquals(1, inbox.size());
		Assert.assertEquals("test <test@mail.com>", inbox.get(0).getFrom()[0].toString());
		Assert.assertEquals("Forgotten password", inbox.get(0).getSubject());
		Assert.assertEquals(
				"<div>Hello Gerald</div>\r\n<div>Forgotten password</div>\r\n<div>Link: http://rest-quickstart-front.com/login/retrieve-password/"
						+ token.getToken() + "</div>",
				inbox.get(0).getContent());

	}

	@Test
	@InSequence(3)
	public void testRequestTokenNew() throws InterruptedException, MessagingException, IOException {
		Date minDate = new DateTime().plusHours(ForgottenPasswordTokenService.VALIDITY_DURATION).toDate();
		tokenRepo.clear();
		Long expectedID = tokenRepo.count() + 1;
		Thread.sleep(1000);

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(ForgottenPasswordAPI.class));

		RequestTokenBody body = new RequestTokenBody();
		body.email = "thomas.malory@mail.com";

		Response response = target.request()
				.post(Entity.entity(body, MediaType.APPLICATION_JSON));

		Assert.assertEquals(204, response.getStatus());

		ForgottenPasswordToken token = tokenRepo.noCacheFindBy(expectedID);
		Assert.assertTrue(token.getExpirationDate().after(minDate));
		Assert.assertTrue(token.getExpirationDate().before(new DateTime().plusHours(ForgottenPasswordTokenService.VALIDITY_DURATION).toDate()));
		Assert.assertNotNull(token.getToken());
		Assert.assertEquals(Long.valueOf(12), token.getUser().getId());

		List<Message> inbox = Mailbox.get("thomas.malory@mail.com");

		Assert.assertEquals(1, inbox.size());
		Assert.assertEquals("test <test@mail.com>", inbox.get(0).getFrom()[0].toString());
		Assert.assertEquals("Mot de passe oublié", inbox.get(0).getSubject());
		Assert.assertEquals(
				"<div>Bonjour Thomas</div>\r\n<div>Mot de passe oublié</div>\r\n<div>Lien: http://rest-quickstart-front.com/login/retrieve-password/"
						+ token.getToken() + "</div>",
				inbox.get(0).getContent());
	}

	@Test
	@InSequence(4)
	public void testcheckTokenNotExist() throws AddressException {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(ForgottenPasswordAPI.class) + "/check");

		CheckTokenBody body = new CheckTokenBody();
		body.token = "pwet";

		Response response = target.request()
				.post(Entity.entity(body, MediaType.APPLICATION_JSON));

		Assert.assertEquals(403, response.getStatus());
	}

	@Test
	@InSequence(4)
	public void testcheckTokenExpired() throws AddressException {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(ForgottenPasswordAPI.class) + "/check");

		CheckTokenBody body = new CheckTokenBody();
		body.token = "b23ca5ac-3b91-4334-b355-92b0915c93e5";

		Response response = target.request()
				.post(Entity.entity(body, MediaType.APPLICATION_JSON));

		Assert.assertEquals(403, response.getStatus());
	}

	@Test
	@InSequence(4)
	public void testcheckToken() throws AddressException {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(ForgottenPasswordAPI.class) + "/check");

		CheckTokenBody body = new CheckTokenBody();
		body.token = "f8760e68-9b6f-4816-8c7e-b9a451fcbd2d";

		Response response = target.request()
				.post(Entity.entity(body, MediaType.APPLICATION_JSON));

		Assert.assertEquals(204, response.getStatus());
	}

	@Test
	@InSequence(5)
	public void testChangePasswordWrongToken() throws AddressException {

		// NotExists
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(ForgottenPasswordAPI.class) + "/change_password");

		ChangePasswordBody body = new ChangePasswordBody();
		body.token = "pwet";
		body.newPassword = "hgaezguhg123";
		body.confirmation = "hgaezguhg123";

		Response response = target.request()
				.post(Entity.entity(body, MediaType.APPLICATION_JSON));

		Assert.assertEquals(403, response.getStatus());

		// Expired
		client = ClientBuilder.newClient();
		target = client.target(resourcesUrl.get(ForgottenPasswordAPI.class) + "/change_password");

		body = new ChangePasswordBody();
		body.token = "b23ca5ac-3b91-4334-b355-92b0915c93e5";
		body.newPassword = "hgaezguhg123";
		body.confirmation = "hgaezguhg123";

		response = target.request()
				.post(Entity.entity(body, MediaType.APPLICATION_JSON));

		Assert.assertEquals(403, response.getStatus());
		userRepo.clear();

		userService.checkPasswordCredentials(userRepo.noCacheFindBy(2L), "passwordTest");
	}

	@Test
	@InSequence(5)
	public void testChangePasswordWrongPassword() throws AddressException {

		// Not same
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(ForgottenPasswordAPI.class) + "/change_password");

		ChangePasswordBody body = new ChangePasswordBody();
		body.token = "Change password API";
		body.newPassword = "testPassword1";
		body.confirmation = "testPassword2";

		Response response = target.request()
				.post(Entity.entity(body, MediaType.APPLICATION_JSON));

		Assert.assertEquals(400, response.getStatus());
		userService.checkPasswordCredentials(userRepo.noCacheFindBy(19L), "passwordTest");

		APIError err = response.readEntity(APIError.class);
		Assert.assertEquals("New Password and Confirmation must be the same", err.message);

		// Too short
		client = ClientBuilder.newClient();
		target = client.target(resourcesUrl.get(ForgottenPasswordAPI.class) + "/change_password");

		body = new ChangePasswordBody();
		body.token = "Change password API";
		body.newPassword = "abc1";
		body.confirmation = "abc1";

		response = target.request()
				.post(Entity.entity(body, MediaType.APPLICATION_JSON));

		Assert.assertEquals(400, response.getStatus());
		userService.checkPasswordCredentials(userRepo.noCacheFindBy(19L), "passwordTest");

		err = response.readEntity(APIError.class);
		Assert.assertEquals("changePassword.body.newPassword: la longueur doit être comprise entre 8 et 255 caractères", err.exception.message);

		// No Numeric
		client = ClientBuilder.newClient();
		target = client.target(resourcesUrl.get(ForgottenPasswordAPI.class) + "/change_password");

		body = new ChangePasswordBody();
		body.token = "Change password API";
		body.newPassword = "testPasswords";
		body.confirmation = "testPasswords";

		response = target.request()
				.post(Entity.entity(body, MediaType.APPLICATION_JSON));

		Assert.assertEquals(400, response.getStatus());
		userService.checkPasswordCredentials(userRepo.noCacheFindBy(19L), "passwordTest");

		err = response.readEntity(APIError.class);
		Assert.assertEquals("changePassword.body.newPassword: Must have at least one numeric caracter and no spaces", err.exception.message);

		// Space
		client = ClientBuilder.newClient();
		target = client.target(resourcesUrl.get(ForgottenPasswordAPI.class) + "/change_password");

		body = new ChangePasswordBody();
		body.token = "Change password API";
		body.newPassword = "test Password2";
		body.confirmation = "test Password2";

		response = target.request()
				.post(Entity.entity(body, MediaType.APPLICATION_JSON));

		Assert.assertEquals(400, response.getStatus());
		userService.checkPasswordCredentials(userRepo.noCacheFindBy(19L), "passwordTest");

		err = response.readEntity(APIError.class);
		Assert.assertEquals("changePassword.body.newPassword: Must have at least one numeric caracter and no spaces", err.exception.message);
	}

	@Test
	@InSequence(6)
	public void testChangePassword() throws AddressException {

		// Not same
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(ForgottenPasswordAPI.class) + "/change_password");

		ChangePasswordBody body = new ChangePasswordBody();
		body.token = "Change password API";
		body.newPassword = "passwordTest2";
		body.confirmation = "passwordTest2";

		Response response = target.request()
				.post(Entity.entity(body, MediaType.APPLICATION_JSON));

		Assert.assertEquals(204, response.getStatus());
		userService.checkPasswordCredentials(userRepo.noCacheFindBy(19L), "passwordTest2");
		Assert.assertNull(tokenRepo.noCacheFindBy(5L));
	}

}
