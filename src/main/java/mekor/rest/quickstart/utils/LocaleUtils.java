package mekor.rest.quickstart.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

import javax.ws.rs.Produces;

import mekor.rest.quickstart.model.entities.user.User;

/**
 * Gives utils methods for getting supported locale and parsing a String to
 * {@link Locale}
 * 
 * @author mekor
 *
 */
public class LocaleUtils {

	/**
	 * The default {@link Locale} of the application
	 */
	public static final Locale DEFAULT_LOCALE = Locale.FRENCH;

	/**
	 * 
	 * @return The list of {@link Locale}s supported by the application
	 */
	@Produces
	public static List<Locale> supportedLocales() {
		List<Locale> list = new ArrayList<Locale>();
		list.add(Locale.FRENCH);
		list.add(Locale.ENGLISH);
		return list;
	}

	/**
	 * Parse a String into a supported locale
	 * 
	 * @param language              the string to parse
	 * @param defaultIfNotSupported If true, return default locale if the language
	 *                              in parameter is not supported
	 * @return the {@link Locale} if supported by the application
	 */
	public static Locale parseLocale(String language, boolean defaultIfNotSupported) {
		if (language != null && !language.isEmpty()) {
			for (Locale locale : supportedLocales()) {
				if (locale.getLanguage().equalsIgnoreCase(language)) {
					return locale;
				}
			}
		}
		if (defaultIfNotSupported) {
			return DEFAULT_LOCALE;
		}
		return null;
	}

	/**
	 * Return a supported locale from an accepted locales list.
	 * 
	 * @param defaultIfNotSupported If true, return default locale if no supported
	 *                              locale has been found.
	 * @param acceptedLocales       List of accepted locales, sorted by preference.
	 * @return The first supported locale found in the list.
	 */
	public static Locale getFromAcceptLanguages(List<Locale> acceptedLocales, boolean defaultIfNotSupported) {
		for (Locale al : acceptedLocales) {
			for (Locale sl : supportedLocales()) {
				if (al.equals(sl)) {
					return al;
				}
			}
		}
		if (defaultIfNotSupported) {
			return DEFAULT_LOCALE;
		}
		return null;
	}

	/**
	 * 
	 * @return A String representation of the supported {@link Locale}s. ex : [FR,
	 *         EN, ES]
	 */
	public static String getSupportedLanguagesString() {
		StringJoiner joiner = new StringJoiner(", ", "[", "]");
		for (Locale locale : supportedLocales()) {
			joiner.add(locale.getLanguage());
		}
		return joiner.toString();
	}

	/**
	 * 
	 * @param user          The user for which we want the locale.
	 * @param currentLocale The locale of the request.
	 * @return The user defaultLocale. If none has been found, return the current
	 *         locale.
	 */
	public static Locale getUserLocale(User user, Locale currentLocale) {
		Locale locale = null;
		if (user.getConfiguration() != null && user.getConfiguration().getDefaultLanguage() != null) {
			locale = LocaleUtils.parseLocale(user.getConfiguration().getDefaultLanguage(), false);
		}
		if (locale == null) {
			locale = currentLocale;
		}
		return locale;
	}

}
