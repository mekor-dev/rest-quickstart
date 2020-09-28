/**
 * 
 */
package mekor.rest.quickstart.configuration.requestfilters.locale;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import com.google.common.net.HttpHeaders;

import mekor.rest.quickstart.api.utils.CurrentRequest;

/**
 * Add the Content-Language header with the current locale.
 * 
 * @author mekor
 *
 */
@Provider
public class ContentLanguageFilter implements ContainerResponseFilter {

	@Inject
	private CurrentRequest currentRequest;

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		responseContext.getHeaders().add(HttpHeaders.CONTENT_LANGUAGE, currentRequest.getLocale());
	}

}
