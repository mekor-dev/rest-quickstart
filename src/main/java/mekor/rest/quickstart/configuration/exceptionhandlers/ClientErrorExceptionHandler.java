/**
 * 
 */
package mekor.rest.quickstart.configuration.exceptionhandlers;

import javax.inject.Inject;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import org.slf4j.Logger;

import com.google.common.net.HttpHeaders;

import mekor.rest.quickstart.api.utils.APIUtils;
import mekor.rest.quickstart.api.utils.error.APIError;

/**
 * Handle {@link ClientErrorException} and return a {@link APIError} to the
 * client with correct status and headers.
 * 
 * @author mekor
 *
 */
@Provider
public class ClientErrorExceptionHandler implements ExceptionMapper<ClientErrorException> {

	@Inject
	private Logger log;

	@Context
	private Providers providers;

	@Inject
	private APIUtils apiUtils;

	@Override
	public Response toResponse(ClientErrorException e) {
		if (e.getResponse() != null) {
			if (log.isDebugEnabled()) {
				log.warn("Client error", e);
			}

			int status = e.getResponse().getStatus();

			ResponseBuilder builder = apiUtils.buildError(status, "A client error occurred", e, null);
			for (String header : e.getResponse().getHeaders().keySet()) {
				builder.header(header, e.getResponse().getHeaders().get(header));
			}
			builder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

			return builder.build();
		}

		return providers.getExceptionMapper(Exception.class).toResponse(e);
	}

}
