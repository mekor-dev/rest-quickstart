/**
 * 
 */
package mekor.rest.quickstart.utils.gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a field with this to ignore it during serialization while using
 * {@link GsonUtils#toJson(Object)}
 * 
 * @author mekor
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface GsonIgnore {

}
