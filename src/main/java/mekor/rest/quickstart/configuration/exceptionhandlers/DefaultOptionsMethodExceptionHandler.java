/**
 * 
 */
package mekor.rest.quickstart.configuration.exceptionhandlers;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.DefaultOptionsMethodException;
import org.slf4j.Logger;

/**
 * Handle {@link DefaultOptionsMethodException} for not logging this error as it
 * is normal behavior
 * 
 * @author mekor
 *
 */
@Provider
public class DefaultOptionsMethodExceptionHandler implements ExceptionMapper<DefaultOptionsMethodException> {

	@Inject
	private Logger log;

	@Context
	private UriInfo uriInfo;

	@Override
	public Response toResponse(DefaultOptionsMethodException exception) {
		log.debug("OPTION Request recieved. URL: {}", uriInfo.getRequestUri().toString());
		return Response.status(200).build();
	}

}
