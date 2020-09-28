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

import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import mekor.rest.quickstart.api.utils.APIUtils;

/**
 * Handle {@link MismatchedInputException} (thrown when the JSON body could not
 * be parsed)
 * 
 * @author mekor
 *
 */
@Provider
public class MismatchedInputExceptionHandler implements ExceptionMapper<MismatchedInputException> {

	@Inject
	private Logger log;

	@Context
	private UriInfo uriInfo;

	@Inject
	private APIUtils apiUtils;

	@Override
	public Response toResponse(MismatchedInputException e) {
		if (log.isDebugEnabled()) {
			log.warn("Client error (MismatchedInput)", e);
		}
		return apiUtils.buildError(400, "Could not parse body", e, apiUtils.getRessourceClass(uriInfo)).build();
	}

}
