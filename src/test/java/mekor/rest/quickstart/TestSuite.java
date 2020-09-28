/**
 * 
 */
package mekor.rest.quickstart;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import mekor.rest.quickstart.api.utils.TestSuiteAPI;
import mekor.rest.quickstart.filters.utils.TestSuiteFilters;
import mekor.rest.quickstart.mappers.utils.TestSuiteMappers;
import mekor.rest.quickstart.services.utils.TestSuiteServices;

/**
 * @author mekor
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
		TestSuiteMappers.class,
		TestSuiteFilters.class,
		TestSuiteServices.class,
		TestSuiteAPI.class
})
public class TestSuite {

}
