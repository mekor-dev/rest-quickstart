package mekor.rest.quickstart.configuration.requestfilters;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;

import com.google.common.net.HttpHeaders;

/**
 * Used to log info about the request in DEBUG (request URL, headers...)
 * 
 * @author mekor
 *
 */
@Provider
@Priority(200)
public class RequestLoggerFilter implements ContainerRequestFilter {

	@Inject
	private Logger log;

	/**
	 * Logs URL and header of each request in DEBUG
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) {
		if (log.isDebugEnabled()) { // As the logging process is heavy, we check first if the debug is enabled
			String uri = requestContext.getUriInfo().getRequestUri().toString();
			String method = requestContext.getMethod();
			MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
			headers.putAll(requestContext.getHeaders());
			headers.remove(HttpHeaders.AUTHORIZATION);
			log.debug("New request incoming. {URI:{}, METHOD: {},  HEADERS:{}}", uri, method, headers);
		}
	}
}
