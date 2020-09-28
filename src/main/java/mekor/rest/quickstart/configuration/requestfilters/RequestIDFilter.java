package mekor.rest.quickstart.configuration.requestfilters;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.MDC;

import mekor.rest.quickstart.configuration.AppConfig;

/**
 * Generates a requestID and adds it in the {@link MDC}
 * 
 * @author mekor
 *
 */
@Provider
@Priority(100)
public class RequestIDFilter implements ContainerRequestFilter {

	@Inject
	private AppConfig appConfig;

	/**
	 * Get a new requestID from the {@link AppConfig} and add it in the MDC map for
	 * logging
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) {
		long requestID = appConfig.newRequest();
		String requestIDString = String.format("%05d", requestID);
		MDC.put("requestID", requestIDString);
	}

}
