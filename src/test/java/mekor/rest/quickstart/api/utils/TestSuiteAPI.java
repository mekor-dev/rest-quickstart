/**
 * 
 */
package mekor.rest.quickstart.api.utils;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import mekor.rest.quickstart.api.TestAuthentication;
import mekor.rest.quickstart.api.TestControlAccessNotification;
import mekor.rest.quickstart.api.TestFileEntityAPI;
import mekor.rest.quickstart.api.TestForgottenPasswordAPI;
import mekor.rest.quickstart.api.TestHeadersFilter;
import mekor.rest.quickstart.api.TestHelloWorldAPI;
import mekor.rest.quickstart.api.TestLocaleFilter;
import mekor.rest.quickstart.api.TestSSEAPI;

/**
 * @author mekor
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
		TestAuthentication.class,
		TestFileEntityAPI.class,
		TestForgottenPasswordAPI.class,
		TestHeadersFilter.class,
		TestHelloWorldAPI.class,
		TestLocaleFilter.class,
		TestControlAccessNotification.class,
		TestSSEAPI.class
})

public class TestSuiteAPI {

}
