/**
 * 
 */
package mekor.rest.quickstart.configuration.requestfilters.locale;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;

import com.google.common.net.HttpHeaders;

import mekor.rest.quickstart.api.utils.CurrentRequest;
import mekor.rest.quickstart.utils.LocaleUtils;

/**
 * Parse the Accept-Language header and set the locale in
 * {@link CurrentRequest}. Set it to default locale if none exists.
 * 
 * @author mekor
 *
 */
@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class AcceptLanguageFilter implements ContainerRequestFilter {

	@Inject
	private Logger log;

	@Inject
	private CurrentRequest currentRequest;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		List<Locale> acceptedLocales = requestContext.getAcceptableLanguages();
		if (acceptedLocales != null) {
			Locale locale = LocaleUtils.getFromAcceptLanguages(acceptedLocales, false);
			if (locale != null) {
				currentRequest.setLocale(locale);
				log.debug("Leaving filter() -> Supported locale found: {}", locale);
				return;
			}
		}
		currentRequest.setLocale(LocaleUtils.DEFAULT_LOCALE);
		log.debug("Leaving filter() -> No supported locale found in AcceptLanguage header ({}), keeping default locale",
				requestContext.getHeaderString(HttpHeaders.ACCEPT_LANGUAGE));

	}

}
