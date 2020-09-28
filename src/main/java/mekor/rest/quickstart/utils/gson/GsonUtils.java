/**
 * 
 */
package mekor.rest.quickstart.utils.gson;

import com.google.gson.GsonBuilder;

/**
 * Gives utility methods to use Gson library (Json parsing) <br />
 * This library is manly used to override toString() for logging data.
 * 
 * @author mekor
 *
 */
public class GsonUtils {

	/**
	 * Serialize an object to JSON. Ignore every field annotated with
	 * {@link GsonIgnore}
	 * 
	 * @param obj the object to serialize
	 * @return The JSON string representing the object.
	 */
	public static String toJson(Object obj) {
		return new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).create().toJson(obj);
	}

}
