/**
 * 
 */
package mekor.rest.quickstart.security.control.annotations.param;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import mekor.rest.quickstart.security.control.ControlParams;

/**
 * @author mekor
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface ControlParam {

	ControlParams value();

}
