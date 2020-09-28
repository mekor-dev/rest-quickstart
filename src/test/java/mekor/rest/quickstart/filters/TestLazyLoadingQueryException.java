/**
 * 
 */
package mekor.rest.quickstart.filters;

import java.util.Locale;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import mekor.rest.quickstart.configuration.ArquillianTest;
import mekor.rest.quickstart.exceptions.LazyLoadingQueryException;
import mekor.rest.quickstart.services.UserService;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingConfiguration;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingFilterOperator;

/**
 * @author mekor
 *
 */
@RunWith(Arquillian.class)
public class TestLazyLoadingQueryException extends ArquillianTest {

	@Inject
	private UserService userService;

	@Test(expected = LazyLoadingQueryException.class)
	public void testInService() throws LazyLoadingQueryException {
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.addFilter("pwet", LazyLoadingFilterOperator.EQUALS, "pwet");
		userService.findLazyLoading(config);
	}

}
