/**
 * 
 */
package mekor.rest.quickstart.services.utils;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import mekor.rest.quickstart.services.TestAuthorizationService;
import mekor.rest.quickstart.services.TestForgottenPasswordTokenService;
import mekor.rest.quickstart.services.TestMailService;

/**
 * @author mekor
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
		TestAuthorizationService.class,
		TestMailService.class,
		TestForgottenPasswordTokenService.class
})
public class TestSuiteServices {

}
