package mekor.rest.quickstart.api;

import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.Base64;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.slf4j.Logger;

import com.webcohesion.enunciate.metadata.rs.TypeHint;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import mekor.rest.quickstart.api.utils.APIUtils;
import mekor.rest.quickstart.api.utils.CurrentRequest;
import mekor.rest.quickstart.model.dtos.user.UserPrivateDTO;
import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.model.mappers.UserMapper;
import mekor.rest.quickstart.security.control.annotations.ControlLoggedIn;
import mekor.rest.quickstart.security.control.annotations.ControlPublic;
import mekor.rest.quickstart.services.JWTService;
import mekor.rest.quickstart.services.UserService;
import mekor.rest.quickstart.utils.JWTTokenType;
import mekor.rest.quickstart.utils.gson.GsonUtils;

/**
 * API for handling authentication accessTokens and refreshTokens.
 * 
 * @author mekor
 *
 */
@RequestScoped
@Transactional
@Path("/access_tokens")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccessTokenAPI {

	public static final String AUTHORIZATION_PREFIX = "Basic";

	@Context
	private UriInfo uriInfo;

	@Context
	private HttpHeaders headers;

	@Inject
	private Logger log;

	@Inject
	private APIUtils apiUtils;

	@Inject
	private CurrentRequest currentRequest;

	@Inject
	private UserMapper userMapper;

	@Inject
	private JWTService jwtService;

	@Inject
	private UserService userService;

	/**
	 * Returns user authenticated by the accessToken given in the Authorization
	 * header
	 * 
	 * @return The user authorized with the accessToken from the Authorization
	 *         header
	 * @HTTP 200 User has been returned
	 */
	@GET
	@Path("/user")
	@TypeHint(UserPrivateDTO.class)
	@ControlLoggedIn
	public Response getCurrentUser() {
		log.debug("Entering getCurrentUser()");

		UserPrivateDTO user = userMapper.entityToDTO(currentRequest.getUser());

		log.debug("Leaving getCurrentUser() -> {}", user);
		return Response.status(200).entity(user).build();
	}

	/**
	 * Authenticate a user with a Basic authentication. The username and password of
	 * the user must be sent in the Authorization header in the 'username:password'
	 * format, then encoded in Base64 and prefixed by 'Basic '
	 * 
	 * @param orgaID The {@link OrganizationDTO} for which the user is
	 *               authenticating
	 * @return AccessToken, refreshToken, the authenticated user and the
	 *         organization it has authenticated for
	 * @HTTP 200 The User has been authenticated.
	 * @HTTP 400 The Authorization header does not have the 'username:password'
	 *       format after Base64 decoding
	 * @HTTP 401 The Authorization header is absent or has bad username or
	 *       credentials
	 */
	@POST
	@Path("/new")
	@TypeHint(AuthenticateResponse.class)
	@ControlPublic
	public Response authenticate() {
		log.debug("Entering authenticate()");

		String authorization = headers.getHeaderString(HttpHeaders.AUTHORIZATION);

		if (authorization != null && authorization.startsWith(AUTHORIZATION_PREFIX)) {
			// Parsing authorization header
			authorization = authorization.substring(AUTHORIZATION_PREFIX.length()).trim();
			authorization = new String(Base64.decodeBase64(authorization));
			String[] split = authorization.split(":");
			if (split.length != 2) {
				log.debug("Leaving authenticate() -> Wrong authorization format: {}", authorization);
				return apiUtils.buildError(400, "Wrong authorization header format", null, null).build();
			}
			String username = split[0];
			String password = split[1];
			// Checking password
			User user = userService.checkPasswordCredentials(username, password);
			if (user != null) {
				currentRequest.setUser(user);
				// Creating JWT tokens
				String acccessToken = jwtService.buildAccessToken(username);
				String refreshToken = jwtService.buildRefreshToken(username);

				// Creating response
				AuthenticateResponse responseBody = new AuthenticateResponse();
				responseBody.accessToken = acccessToken;
				responseBody.refreshToken = refreshToken;
				responseBody.user = userMapper.entityToDTO(currentRequest.getUser());

				log.debug("Leaving authenticate() -> {}", responseBody);
				return Response.status(200).entity(responseBody).build();
			}
			log.debug("Username or password is incorrect");
		}
		log.debug("Leaving authenticate() -> Not authorized");
		return apiUtils.buildError(401, "Wrong authentification", null, null).build();

	}

	/**
	 * Get a new accessToken from a refreshToken <br />
	 * 
	 * @param refreshToken the refreshToken
	 * @returnA new accessToken, the authenticated user and the organization it has
	 *          authenticated for
	 * @HTTP 200 New accessToken has been returned
	 * @HTTP 401 The owner of this token doesn't have an account anymore or is not
	 *       in the organization anymore<br />
	 *       The token is invalid or has expired.
	 */
	@POST
	@Path("/refresh")
	@TypeHint(RefreshAccessTokenResponse.class)
	@ControlPublic
	public Response refreshAccessToken(RefreshAccessTokenBody body) {
		log.debug("Entering refreshAccessToken()");

		try {
			Jws<Claims> claims = jwtService.checkToken(body.refreshToken);
			log.debug("JWT is correcty signed. Getting user");

			String type = claims.getBody().get(JWTService.TOKEN_TYPE_CLAIM_FIELD, String.class);
			if (!JWTTokenType.REFRESH.toString().equals(type)) {
				log.debug("Leaving refreshAccessToken() -> Incorrect token type ({})", type);
				return apiUtils.buildError(401, "Incorect token type", null, null).build();
			}

			String userEmail = claims.getBody().getSubject();

			User user = userService.findByEmail(userEmail);
			// Checking user
			if (user == null) {
				log.warn("Leaving refreshAccessToken() -> User {} does not exist anymore", userEmail);
				return apiUtils.buildError(401, "The subject of this token has no account anymore. It may have been deleted", null, null).build();
			}
			currentRequest.setUser(user);
			String newToken = jwtService.buildAccessToken(userEmail);
			RefreshAccessTokenResponse responseBody = new RefreshAccessTokenResponse();
			responseBody.accessToken = newToken;
			responseBody.user = userMapper.entityToDTO(currentRequest.getUser());

			log.debug("Leaving refreshAccessToken() -> Token refreshed for user: {}", user.getEmail());
			return Response.status(200).entity(responseBody).build();
		}
		catch (ExpiredJwtException e) {
			log.debug("Leaving refreshAccessToken() -> Token has expired. {currentDate: {}, expirationDate: {}}", new Date(), e.getClaims().getExpiration());
			return apiUtils.buildError(401, "The token has expired", null, null).build();
		}
		catch (Exception e) {
		}
		log.debug("Leaving refreshAccessToken() -> Wrong token");
		return apiUtils.buildError(401, "Wrong refresh token", null, null).build();
	}

	/**
	 * Response of {@link AccessTokenAPI#authenticate()}
	 * 
	 * @author mekor
	 *
	 */
	public static class AuthenticateResponse {

		/**
		 * AccessToken to add in the Authorization header (prefixed by 'Bearer ') of
		 * next requests the user will send. <br />
		 * <strong>Expiration: </strong> 30 minutes
		 */
		public String accessToken;

		/**
		 * RefreshToken that can be used to refresh the accessToken if it has
		 * expired<br />
		 * <strong>Expiration: </strong> 1 month
		 */
		public String refreshToken;

		/**
		 * The user that has been authenticated
		 */
		public UserPrivateDTO user;

		@Override
		public String toString() {
			return GsonUtils.toJson(this);
		}
	}

	/**
	 * Body of {@link AccessTokenAPI#refreshAccessToken(String)}
	 * 
	 * @author mekor
	 *
	 */
	public static class RefreshAccessTokenBody {

		/**
		 * The refreshToken
		 */
		@NotNull
		public String refreshToken;
	}

	/**
	 * Response of {@link AccessTokenAPI#refreshAccessToken(String)}
	 * 
	 * @author mekor
	 *
	 */
	public static class RefreshAccessTokenResponse {

		/**
		 * AccessToken to add in the Authorization header (prefixed by 'Bearer ') of
		 * next requests the user will send. <br />
		 * <strong>Expiration: </strong> 30 minutes
		 */
		public String accessToken;

		/**
		 * The user that has been authenticated
		 */
		public UserPrivateDTO user;

		@Override
		public String toString() {
			return GsonUtils.toJson(this);
		}
	}
}
