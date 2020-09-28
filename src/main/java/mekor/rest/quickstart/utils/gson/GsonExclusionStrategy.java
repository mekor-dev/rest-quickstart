/**
 * 
 */
package mekor.rest.quickstart.utils.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Excelusion strategy used in {@link GsonUtils#toJson(Object)} to ignore very
 * field annotated with {@link GsonIgnore}
 * 
 * @author mekor
 *
 */
public class GsonExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		return f.getAnnotation(GsonIgnore.class) != null;
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}
}
