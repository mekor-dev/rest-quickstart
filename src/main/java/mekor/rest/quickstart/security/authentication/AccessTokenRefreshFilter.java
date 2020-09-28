/**
 * 
 */
package mekor.rest.quickstart.security.authentication;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import mekor.rest.quickstart.api.utils.CurrentRequest;
import mekor.rest.quickstart.services.JWTService;

/**
 * Join a new accesToken in the header of the response (refresh the old
 * accessToken)
 * 
 * @author mekor
 *
 */
@Provider
public class AccessTokenRefreshFilter implements ContainerResponseFilter {

	public static final String ACCESS_TOKEN_HEADER = "accessToken";

	@Inject
	private CurrentRequest currentRequest;

	@Inject
	private JWTService jwtService;

	/**
	 * Check if the currentUser exists (a.k.a. a valid accessToken has been provided
	 * in the request). If yes, create a new accessToken for this user and add it to
	 * the accessToken header
	 */
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		if (currentRequest.getUser() != null) {
			String accessToken = jwtService.buildAccessToken(currentRequest.getUser().getEmail());
			responseContext.getHeaders().add(ACCESS_TOKEN_HEADER, accessToken);
		}

	}

}
