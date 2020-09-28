/**
 * 
 */
package mekor.rest.quickstart.model.dtos.utils;

import com.google.gson.internal.LinkedTreeMap;

/**
 * Used to handle localized string in the model. Contains a map with per
 * language entries
 * 
 * @author mekor
 *
 */
public class LocalizedStringDTO {

	/**
	 * A map containing localized strings
	 * <ul>
	 * <li><strong>Key: </strong> language</li>
	 * <li><strong>Value: </strong> string for associated language</li>
	 * </ul>
	 */
	public LinkedTreeMap<String, String> map = new LinkedTreeMap<>();

}
