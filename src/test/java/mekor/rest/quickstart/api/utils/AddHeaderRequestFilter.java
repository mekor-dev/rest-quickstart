/**
 * 
 */
package mekor.rest.quickstart.api.utils;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

/**
 * @author mekor
 *
 */
public class AddHeaderRequestFilter implements ClientRequestFilter {

	private final String headerName;

	private final Object headerValue;

	public AddHeaderRequestFilter(String headerName, Object headerValue) {
		super();
		this.headerName = headerName;
		this.headerValue = headerValue;
	}

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		requestContext.getHeaders().add(headerName, headerValue);
	}

}
