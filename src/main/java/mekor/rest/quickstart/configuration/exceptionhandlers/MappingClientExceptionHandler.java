/**
 * 
 */
package mekor.rest.quickstart.configuration.exceptionhandlers;

import javax.inject.Inject;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import org.slf4j.Logger;

import mekor.rest.quickstart.api.utils.APIUtils;
import mekor.rest.quickstart.api.utils.error.APIError;
import mekor.rest.quickstart.exceptions.MappingClientException;

/**
 * @author mekor
 *
 */

/**
 * Handle {@link ClientErrorException} and return a {@link APIError} to the
 * client with correct status and headers.
 * 
 * @author mekor
 *
 */
@Provider
public class MappingClientExceptionHandler implements ExceptionMapper<MappingClientException> {

	@Inject
	private Logger log;

	@Context
	private Providers providers;

	@Inject
	private APIUtils apiUtils;

	@Override
	public Response toResponse(MappingClientException e) {
		if (log.isDebugEnabled()) {
			log.warn("Client error", e);
		}

		return apiUtils.buildError(400, "Error while mapping entities", e, null).build();
	}

}
