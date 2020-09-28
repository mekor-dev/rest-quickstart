/**
 * 
 */
package mekor.rest.quickstart.security.control.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import mekor.rest.quickstart.security.control.ControlParams;
import mekor.rest.quickstart.security.control.annotations.param.ControlParam;

/**
 * Annotate an API service with this to check if the user has access to a
 * notification. <br />
 * <br />
 * The notificationID pathParameter must be annotated
 * with @{@link ControlParam}({@link ControlParams#NOTIFICATION_ID}).
 * 
 * @author mekor
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ControlAccessNotification {

}
