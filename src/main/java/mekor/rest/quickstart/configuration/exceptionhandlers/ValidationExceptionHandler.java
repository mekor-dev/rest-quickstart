/**
 * 
 */
package mekor.rest.quickstart.configuration.exceptionhandlers;

import javax.inject.Inject;
import javax.validation.ValidationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import org.jboss.resteasy.api.validation.ResteasyViolationException;
import org.slf4j.Logger;

import mekor.rest.quickstart.api.utils.APIUtils;
import mekor.rest.quickstart.api.utils.error.APIError;
import mekor.rest.quickstart.exceptions.PatchValidationException;

/**
 * Handle {@link ValidationException} and return an {@link APIError} with info
 * about validation error.
 * 
 * @author mekor
 *
 */
@Provider
public class ValidationExceptionHandler implements ExceptionMapper<ValidationException> {

	@Inject
	private Logger log;

	@Context
	private UriInfo uriInfo;

	@Context
	private Providers providers;

	@Inject
	private APIUtils apiUtils;

	@Override
	public Response toResponse(ValidationException e) {
		if (e instanceof ResteasyViolationException) {
			if (log.isDebugEnabled()) {
				log.warn("Client error (Validation) : {}", e.getMessage());
			}
			return apiUtils.buildError(400, "Bad request params", e, apiUtils.getRessourceClass(uriInfo)).build();
		}
		if (e instanceof PatchValidationException) {
			if (log.isDebugEnabled()) {
				log.warn("Client error (Patch Validation)", e);
			}
			return apiUtils.buildError(400, "Error while validationg PATCH", e, apiUtils.getRessourceClass(uriInfo)).build();
		}
		return providers.getExceptionMapper(Exception.class).toResponse(e);
	}

}
