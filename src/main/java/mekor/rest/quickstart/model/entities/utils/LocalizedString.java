package mekor.rest.quickstart.model.entities.utils;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import mekor.rest.quickstart.exceptions.LocalizedStringInitException;

/**
 * Used to handle localized string in the model. Save the data as a json string
 * in database and serialize it in json as a map.
 * 
 * @author mekor
 *
 */
@Embeddable
public class LocalizedString implements Serializable {

	private static final long serialVersionUID = 4281268643779512136L;

	/**
	 * 
	 * The string saved in database <br />
	 * (ex : {"fr":"Texte français","en":"Text Anglais"})
	 */
	@Column
	private String jsonText;

	/**
	 * True if the {@link #map} has already been initialized
	 */
	@Transient
	private boolean initialized = false;

	/**
	 * A map containing localized strings
	 * <ul>
	 * <li><strong>Key: </strong> language</li>
	 * <li><strong>Value: </strong> string for associated language</li> </strong>
	 */
	@Transient
	private LinkedTreeMap<String, String> map;

	/**
	 * Init the {@link #map} with value contained in {@link #jsonText}
	 * 
	 * @throws LocalizedStringInitException The {@link #jsonText} format is
	 *                                      incorrect
	 */
	@SuppressWarnings("unchecked")
	public void init() throws LocalizedStringInitException {
		try {
			if (jsonText != null && !jsonText.isEmpty()) {
				map = new Gson().fromJson(jsonText, LinkedTreeMap.class);
				initialized = true;
			}
			else {
				map = new LinkedTreeMap<>();
			}
		}
		catch (Exception e) {
			throw new LocalizedStringInitException(jsonText);
		}
	}

	/**
	 * Serialize the {@link #map} in {@link #jsonText}
	 */
	public void toJson() {
		jsonText = new Gson().toJson(map);
	}

	/**
	 * 
	 * @param locale locale of the language we want the text for
	 * @return the text for the given locale
	 * @throws LocalizedStringInitException @see {@link #init()}
	 */
	public String getText(Locale locale) throws LocalizedStringInitException {
		if (!initialized) {
			init();
		}
		return map.get(locale.getLanguage());
	}

	/**
	 * Set the text for the given locale
	 * 
	 * @param text   new text to set
	 * @param locale locale we want to set the text for
	 * @throws LocalizedStringInitException @see {@link #init()}
	 */
	public void setText(String text, Locale locale) throws LocalizedStringInitException {
		if (!initialized) {
			init();
		}
		map.put(locale.getLanguage(), text);
		toJson();
	}

	/**
	 * Remove the text for the given locale
	 * 
	 * @param locale locale we want to remove the text for
	 * @throws LocalizedStringInitException @see {@link #init()}
	 */
	public void removeText(Locale locale) throws LocalizedStringInitException {
		if (!initialized) {
			init();
		}
		map.remove(locale.getLanguage());
		toJson();
	}

	/**
	 * @see {@link #map}
	 */
	public LinkedTreeMap<String, String> getMap() throws LocalizedStringInitException {
		if (!initialized) {
			init();
		}
		return map;
	}

	/**
	 * @see {@link #map}
	 */
	public void setMap(LinkedTreeMap<String, String> textMap) {
		if (textMap == null) {
			this.map = new LinkedTreeMap<>();
		}
		else {
			this.map = textMap;
		}
		toJson();
	}

}
