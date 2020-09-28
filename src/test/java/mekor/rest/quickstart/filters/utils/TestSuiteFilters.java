/**
 * 
 */
package mekor.rest.quickstart.filters.utils;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import mekor.rest.quickstart.filters.TestLazyLoading;
import mekor.rest.quickstart.filters.TestLazyLoadingQueryException;

/**
 * @author mekor
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
		TestLazyLoading.class,
		TestLazyLoadingQueryException.class
})
public class TestSuiteFilters {

}
