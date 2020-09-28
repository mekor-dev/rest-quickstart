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

import org.slf4j.Logger;

import mekor.rest.quickstart.api.utils.APIUtils;
import mekor.rest.quickstart.api.utils.error.APIError;

/**
 * Handle unhandled exceptions and return a {@link APIError} to the client.
 * 
 * @author mekor
 *
 */
@Provider
public class DefaultExceptionHandler implements ExceptionMapper<Throwable> {

	@Inject
	private Logger log;

	@Context
	private UriInfo uriInfo;

	@Inject
	private APIUtils apiUtils;

	@Override
	public Response toResponse(Throwable e) {
		log.error("Unhandled error:", e);
		return apiUtils.buildError(500, "An unhandled error has occured", e, apiUtils.getRessourceClass(uriInfo)).build();
	}

}
