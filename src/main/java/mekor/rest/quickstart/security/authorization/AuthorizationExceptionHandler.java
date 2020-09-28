/**
 * 
 */
package mekor.rest.quickstart.security.authorization;

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
 * Handle {@link AuthorizationException} and return an {@link APIError} to the
 * client with 403 status code.
 * 
 * @author mekor
 *
 */
@Provider
public class AuthorizationExceptionHandler implements ExceptionMapper<AuthorizationException> {

	@Context
	private UriInfo uriInfo;

	@Inject
	Logger log;

	@Inject
	private APIUtils apiUtils;

	@Override
	public Response toResponse(AuthorizationException e) {
		if (e.getStatus() >= 500 && e.getStatus() < 600) {
			log.error("Error in AuthorizationException", e);
		}
		else {
			log.debug("Cannot access this ressource", e);
		}
		int status = e.getStatus() != null ? e.getStatus() : 403;
		return apiUtils.buildError(status, "You cannot access this ressource", e, apiUtils.getRessourceClass(uriInfo)).build();
	}

}
