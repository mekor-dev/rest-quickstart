/**
 * 
 */
package mekor.rest.quickstart.services;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.joda.time.DateTime;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import mekor.rest.quickstart.configuration.AppConfig;
import mekor.rest.quickstart.utils.JWTTokenType;

/**
 * Service that manages Java Web Tokens.
 * 
 * @author mekor
 *
 */
@ApplicationScoped
public class JWTService {

	public static final int ACCESS_TOKEN_DURATION_MINUTES = 30;

	public static final int REFRESH_TOKEN_DURATION_MONTH = 1;

	public static final String ORGA_CLAIM_FIELD = "orgaName";

	public static final String TOKEN_TYPE_CLAIM_FIELD = "token_type";

	@Inject
	private AppConfig appConfig;

	/**
	 * Build an accessToken for the username given in parameter
	 * 
	 * @param username The username of the user which requires an accessToken.
	 * @return The accessToken
	 */
	public String buildAccessToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.claim(TOKEN_TYPE_CLAIM_FIELD, JWTTokenType.AUTH)
				.setIssuer(appConfig.getApiRoot())
				.setIssuedAt(new Date())
				.setExpiration(new DateTime().plusMinutes(ACCESS_TOKEN_DURATION_MINUTES).toDate())
				.signWith(appConfig.getJwtKey())
				.compact();
	}

	/**
	 * Build a refreshToken for the username given in parameter
	 * 
	 * @param username The username of the user which requires a refreshToken
	 * @return The refreshToken
	 */
	public String buildRefreshToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.claim(TOKEN_TYPE_CLAIM_FIELD, JWTTokenType.REFRESH)
				.setIssuer(appConfig.getApiRoot())
				.setIssuedAt(new Date())
				.setExpiration(new DateTime().plusMonths(REFRESH_TOKEN_DURATION_MONTH).toDate())
				.signWith(appConfig.getJwtKey())
				.compact();
	}

	/**
	 * Check if the token is still correctly signed and return the token content
	 * 
	 * @param token The token to check
	 * @return The content of the token
	 */
	public Jws<Claims> checkToken(String token) {
		return Jwts.parser().setSigningKey(appConfig.getJwtKey()).parseClaimsJws(token);
	}

}
