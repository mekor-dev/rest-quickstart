/**
 * 
 */
package mekor.rest.quickstart.services;

import java.util.Date;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import mekor.rest.quickstart.configuration.ArquillianTest;
import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.model.entities.user.credentials.ForgottenPasswordToken;
import mekor.rest.quickstart.repositories.ForgottenPasswordTokenRepository;
import mekor.rest.quickstart.repositories.UserRepository;
import mekor.rest.quickstart.services.ForgottenPasswordTokenService;

/**
 * @author mekor
 *
 */
@RunWith(Arquillian.class)
public class TestForgottenPasswordTokenService extends ArquillianTest {

	@Inject
	private ForgottenPasswordTokenService tokenService;

	@Inject
	private ForgottenPasswordTokenRepository tokenRepo;

	@Inject
	private UserRepository userRepo;

	@Test
	@InSequence(1)
	public void testFindValidByToken() {
		ForgottenPasswordToken token = tokenService.findValidByToken("b23ca5ac-3b91-4334-b355-92b0915c93e5");
		Assert.assertNull(token);
		token = tokenService.findValidByToken("safafzf");
		Assert.assertNull(token);
		token = tokenService.findValidByToken("f8760e68-9b6f-4816-8c7e-b9a451fcbd2d");
		Assert.assertEquals(Long.valueOf(1), token.getId());
		Assert.assertEquals(Long.valueOf(1), token.getUser().getId());
	}

	@Test
	@InSequence(2)
	@Transactional
	public void testCreateTokenReplace() throws InterruptedException {
		Date minDate = new DateTime().plusHours(ForgottenPasswordTokenService.VALIDITY_DURATION).toDate();
		Thread.sleep(1000);
		User user = userRepo.findBy(3L);
		ForgottenPasswordToken token = tokenService.createToken(user);
		Assert.assertEquals(Long.valueOf(3), token.getId());
		token = tokenRepo.noCacheFindBy(3L);
		Assert.assertTrue(token.getExpirationDate().after(minDate));
		Assert.assertTrue(token.getExpirationDate().before(new DateTime().plusHours(ForgottenPasswordTokenService.VALIDITY_DURATION).toDate()));
		Assert.assertNotEquals(token.getToken(), "replace Service");
		Assert.assertEquals(Long.valueOf(3), token.getUser().getId());
	}

	@Test
	@InSequence(2)
	@Transactional
	public void testCreateTokenNew() throws InterruptedException {
		Date minDate = new DateTime().plusHours(ForgottenPasswordTokenService.VALIDITY_DURATION).toDate();
		Long expectedID = tokenRepo.count() + 1;
		Thread.sleep(1000);
		User user = userRepo.findBy(4L);
		ForgottenPasswordToken token = tokenService.createToken(user);
		Assert.assertEquals(expectedID, token.getId());
		token = tokenRepo.noCacheFindBy(expectedID);
		Assert.assertTrue(token.getExpirationDate().after(minDate));
		Assert.assertTrue(token.getExpirationDate().before(new DateTime().plusHours(ForgottenPasswordTokenService.VALIDITY_DURATION).toDate()));
		Assert.assertNotNull(token.getToken());
		Assert.assertEquals(Long.valueOf(4), token.getUser().getId());
	}

}
