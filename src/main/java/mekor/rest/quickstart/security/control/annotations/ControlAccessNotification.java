/**
 * 
 */
package mekor.rest.quickstart.security.control.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate an API service with this to check if the user has access to a
 * notification. <br />
 * <br />
 * The notifID parameter must be name of the corresponding pathparam.
 * 
 * @author mekor
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ControlAccessNotification {

	public String notifID();

}
