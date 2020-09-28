/**
 * 
 */
package mekor.rest.quickstart.security.authentication;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.MDC;

import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import mekor.rest.quickstart.api.utils.APIUtils;
import mekor.rest.quickstart.api.utils.CurrentRequest;
import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.security.control.annotations.ControlPublic;
import mekor.rest.quickstart.services.JWTService;
import mekor.rest.quickstart.services.UserService;
import mekor.rest.quickstart.utils.JWTTokenType;

/**
 * Called before every service that are not annotated
 * with @{@link ControlPublic}.<br />
 * Check the Authorization header to know if the user has been authenticated
 * before. <br />
 * Also set the user and the organization in the {@link CurrentRequest} and the
 * {@link MDC}
 * 
 * @author mekor
 *
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AccessTokenFilter implements ContainerRequestFilter {

	public static final String AUTHORIZATION_PREFIX = "Bearer";

	public static final String EXPIRED_HEADER = "expired";

	@Context
	private ResourceInfo resourceInfo;

	@Inject
	private Logger log;

	@Inject
	private APIUtils apiUtils;

	@Inject
	private CurrentRequest currentRequest;

	@Inject
	private UserService userService;

	@Inject
	private JWTService jwtService;

	/**
	 * Check the Authorization header for a Bearer accessToken. Check this token
	 * validity and set the corresponding user and orga in the
	 * {@link CurrentRequest} bean.
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		if (apiUtils.isAnnotationPresent(resourceInfo, ControlPublic.class)) {
			log.debug("Entering and Leaving filter() -> Method is public");
			return;
		}

		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		log.debug("Entering filter() -> authorization header: {}", authorizationHeader);
		if (authorizationHeader != null && authorizationHeader.startsWith(AUTHORIZATION_PREFIX)) {
			String token = authorizationHeader.substring(AUTHORIZATION_PREFIX.length()).trim();
			try {
				Jws<Claims> claims = jwtService.checkToken(token);
				log.debug("JWT is correcty signed. Getting user");

				String type = claims.getBody().get(JWTService.TOKEN_TYPE_CLAIM_FIELD, String.class);
				if (!JWTTokenType.AUTH.toString().equals(type)) {
					log.debug("Leaving filter() -> Incorrect token type ({})", type);
					requestContext.abortWith(apiUtils.buildError(401, "Incorect token type", null, null).build());
					return;
				}

				String userEmail = claims.getBody().getSubject();
				User user = userService.findByEmail(userEmail);

				if (user == null) {
					log.warn("Leaving filter() -> Subject of the token ({}) has no account", userEmail);
					requestContext.abortWith(apiUtils.buildError(401, "The subject of this token has no account. It may have been deleted", null, null).build());
					return;
				}

				currentRequest.setUser(user);
				MDC.put("userMail", userEmail);
				log.debug("User authorized: {}", user);
				return;
			}
			catch (ExpiredJwtException e) {
				log.debug("Leaving filter() -> Token has expired. {CurrentDate: {}, expirationDate: {}}", new Date(), e.getClaims().getExpiration());
				requestContext.abortWith(apiUtils.buildError(401, "The token has expired", null, null).header(EXPIRED_HEADER, true).build());
				return;
			}
			catch (Exception e) {
				log.debug("Leaving filter() -> Error while parsing token", e);
			}
		}
		log.warn("Leaving filter() -> Wrong authorization header: {}", authorizationHeader);
		requestContext.abortWith(apiUtils.buildError(401, "Wrong authorization header", null, null).build());
	}

}
