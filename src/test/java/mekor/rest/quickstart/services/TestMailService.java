/**
 * 
 */
package mekor.rest.quickstart.services;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.mock_javamail.Mailbox;

import mekor.rest.quickstart.configuration.ArquillianTest;
import mekor.rest.quickstart.exceptions.MailSendException;
import mekor.rest.quickstart.repositories.UserRepository;
import mekor.rest.quickstart.services.MailService;

/**
 * @author mekor
 *
 */
@RunWith(Arquillian.class)
public class TestMailService extends ArquillianTest {

	@Inject
	private MailService mailService;

	@Inject
	private UserRepository userRepo;

	@Before
	public void before() {
		Mailbox.clearAll();
	}

	@Test
	public void testSendForgottenPasswordMail() throws MailSendException, MessagingException, IOException {

		mailService.sendForgottenPasswordMail(userRepo.findBy(1L), "password_test_token", Locale.FRENCH);

		List<Message> inbox = Mailbox.get("fred.allen@mail.com");

		Assert.assertEquals(1, inbox.size());
		Assert.assertEquals("test <test@mail.com>", inbox.get(0).getFrom()[0].toString());
		Assert.assertEquals("Mot de passe oublié", inbox.get(0).getSubject());
		Assert.assertEquals(
				"<div>Bonjour Fred</div>\r\n<div>Mot de passe oublié</div>\r\n<div>Lien: http://rest-quickstart-front.com/login/retrieve-password/password_test_token</div>",
				inbox.get(0).getContent());

	}

}
