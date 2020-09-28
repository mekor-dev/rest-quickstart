/**
 * 
 */
package mekor.rest.quickstart.services;

import static org.junit.Assume.assumeTrue;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import mekor.rest.quickstart.configuration.ArquillianTest;

/**
 * @author mekor
 *
 */
@RunWith(Arquillian.class)
public class TestAuthorizationService extends ArquillianTest {

//	@Inject
//	private UserRepository userRepo;
//
//	@Inject
//	private NotificationRepository notifRepo;
//
//	@Inject
//	private CurrentRequest currentRequest;
//
//	@Inject
//	private AuthorizationService authorization;

	@Test
	public void empty() {
		assumeTrue(false);
	}

// TODO
//	@Test
//	public void testEntityIsNotDeleted() {
//		User user = userRepo.findBy(1L);
//		currentRequest.setMemberShip(omRepo.findByOrgaAndUser(1L, 1L));
//		authorization.entityIsNotDeleted(user, "Pwet");
//		currentRequest.setMemberShip(omRepo.findByOrgaAndUser(1L, 2L));
//		authorization.entityIsNotDeleted(user, "Pwet");
//
//		user = userRepo.findBy(8L);
//		currentRequest.setMemberShip(omRepo.findByOrgaAndUser(1L, 1L));
//		authorization.entityIsNotDeleted(user, "Pwet");
//		currentRequest.setMemberShip(omRepo.findByOrgaAndUser(1L, 2L));
//		try {
//			authorization.entityIsNotDeleted(user, "Pwet");
//			Assert.fail();
//		}
//		catch (NotFoundException e) {
//		}
//	}
//
//	@Test
//	public void testCanAccessNotification() {
//		// Wrong user
//		currentRequest.setUser(userRepo.findBy(1L));
//		try {
//			authorization.canAccessNotification(notifRepo.findBy(1L));
//			Assert.fail();
//		}
//		catch (AuthorizationException e) {
//			Assert.assertEquals("The current user does not own this notification", e.getMessage());
//		}
//
//		// Wrong orga
//		currentRequest.setUser(userRepo.findBy(1L));
//		try {
//			authorization.canAccessNotification(notifRepo.findBy(4L));
//			Assert.fail();
//		}
//		catch (AuthorizationException e) {
//			Assert.assertEquals("Current user is not authenticated in orga with ID 2", e.getMessage());
//		}
//
//		// Good
//		currentRequest.setUser(userRepo.findBy(1L));
//		authorization.canAccessNotification(notifRepo.findBy(2L));
//
//	}

}
