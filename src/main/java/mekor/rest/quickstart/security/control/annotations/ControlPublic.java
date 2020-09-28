/**
 * 
 */
package mekor.rest.quickstart.security.control.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import mekor.rest.quickstart.security.authentication.AccessTokenFilter;
import mekor.rest.quickstart.security.control.ApiControlFilter;

/**
 * Annotation used to bypass the {@link AccessTokenFilter} and in the
 * {@link ApiControlFilter}
 * 
 * @author mekor
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ControlPublic {

}
