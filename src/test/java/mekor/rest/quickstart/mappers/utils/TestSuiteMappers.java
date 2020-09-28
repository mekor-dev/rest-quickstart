/**
 * 
 */
package mekor.rest.quickstart.mappers.utils;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import mekor.rest.quickstart.mappers.TestFileEntityMapper;
import mekor.rest.quickstart.mappers.TestFileTagMapper;

/**
 * @author mekor
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
		TestFileEntityMapper.class,
		TestFileTagMapper.class
})

public class TestSuiteMappers {

}
