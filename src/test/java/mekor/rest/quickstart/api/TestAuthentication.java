/**
 * 
 */
package mekor.rest.quickstart.api;

import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import mekor.rest.quickstart.api.AccessTokenAPI.AuthenticateResponse;
import mekor.rest.quickstart.api.AccessTokenAPI.RefreshAccessTokenBody;
import mekor.rest.quickstart.api.AccessTokenAPI.RefreshAccessTokenResponse;
import mekor.rest.quickstart.api.utils.error.APIError;
import mekor.rest.quickstart.configuration.AppConfig;
import mekor.rest.quickstart.configuration.ArquillianTest;
import mekor.rest.quickstart.configuration.TestResourcesURL;
import mekor.rest.quickstart.model.dtos.user.UserPrivateDTO;
import mekor.rest.quickstart.security.authentication.AccessTokenFilter;
import mekor.rest.quickstart.security.authentication.AccessTokenRefreshFilter;
import mekor.rest.quickstart.services.JWTService;
import mekor.rest.quickstart.utils.JWTTokenType;

/**
 * @author mekor
 *
 */
@RunWith(Arquillian.class)
public class TestAuthentication extends ArquillianTest {

	public static String ACCESS_TOKEN;

	public static Date ACCESS_TOKEN_DATE;

	public static String REFRESH_TOKEN;

	private static final String USERNAME = "fred.allen@mail.com";

	private static final String USERNAME2 = "joseph.joubert@mail.com";

	private static final String PASSWORD = "passwordTest";

	@Inject
	private AppConfig appConfig;

	@Inject
	private TestResourcesURL resourcesUrl;

	public String authenticateURI() {
		return resourcesUrl.get(AccessTokenAPI.class) + "/new";
	}

	public String refreshTokenURI() {
		return resourcesUrl.get(AccessTokenAPI.class) + "/refresh";
	}

	public String tokenUserURI() {
		return resourcesUrl.get(AccessTokenAPI.class) + "/user";
	}

	public String getAuthorizationHeader() {
		return "Bearer " + ACCESS_TOKEN;
	}

	public void sleep() throws InterruptedException {
		Thread.sleep(1000);
	}

	@Test
	@InSequence(0)
	public void tryWithoutAuthentication() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(tokenUserURI());
		Response response = target.request().get();

		Assert.assertEquals(401, response.getStatus());
		Assert.assertNull(response.getHeaderString(AccessTokenFilter.EXPIRED_HEADER));
	}

	@Test
	@InSequence(1)
	public void tryWithExpiredToken() throws InterruptedException {
		String authorizationHeader = Jwts.builder()
				.setSubject(USERNAME)
				.claim(JWTService.TOKEN_TYPE_CLAIM_FIELD, JWTTokenType.AUTH)
				.setIssuer(appConfig.getApiRoot())
				.setIssuedAt(new DateTime().minusMinutes(10).toDate())
				.setExpiration(new Date())
				.signWith(appConfig.getJwtKey())
				.compact();

		sleep();

		authorizationHeader = "Bearer " + authorizationHeader;
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(tokenUserURI());
		Response response = target.request().header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();

		Assert.assertEquals(401, response.getStatus());

		Assert.assertNotNull(response.getHeaderString(AccessTokenFilter.EXPIRED_HEADER));
		Assert.assertEquals("true", response.getHeaderString(AccessTokenFilter.EXPIRED_HEADER));
	}

	@Test
	@InSequence(3)
	public void tryWrongUser() throws InterruptedException {
		String authorizationHeader = Jwts.builder()
				.setSubject("Hgeaighoag")
				.claim(JWTService.TOKEN_TYPE_CLAIM_FIELD, JWTTokenType.AUTH)
				.setIssuer(appConfig.getApiRoot())
				.setIssuedAt(new DateTime().minusMinutes(5).toDate())
				.setExpiration(new DateTime().plusMinutes(5).toDate())
				.signWith(appConfig.getJwtKey())
				.compact();

		sleep();

		authorizationHeader = "Bearer " + authorizationHeader;
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(tokenUserURI());
		Response response = target.request().header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();

		Assert.assertEquals(401, response.getStatus());
		Assert.assertNull(response.getHeaderString(AccessTokenFilter.EXPIRED_HEADER));
	}

	@Test
	@InSequence(4)
	public void tryWrongTokenType() throws InterruptedException {
		String authorizationHeader = Jwts.builder()
				.setSubject(USERNAME)
				.claim(JWTService.TOKEN_TYPE_CLAIM_FIELD, JWTTokenType.REFRESH)
				.setIssuer(appConfig.getApiRoot())
				.setIssuedAt(new DateTime().minusMinutes(5).toDate())
				.setExpiration(new DateTime().plusMinutes(5).toDate())
				.signWith(appConfig.getJwtKey())
				.compact();

		sleep();

		authorizationHeader = "Bearer " + authorizationHeader;
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(tokenUserURI());
		Response response = target.request().header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();

		Assert.assertEquals(401, response.getStatus());
		Assert.assertNull(response.getHeaderString(AccessTokenFilter.EXPIRED_HEADER));

		APIError err = response.readEntity(APIError.class);
		Assert.assertEquals("Incorect token type", err.message);
	}

	@Test
	@InSequence(4)
	public void authenticateWrongUser() {
		String authorizationHeader = USERNAME + ":jgbhsuibgh";
		authorizationHeader = Base64.encodeBase64String(authorizationHeader.getBytes());
		authorizationHeader = "Basic " + authorizationHeader;

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(authenticateURI());
		Response response = target.request().header(HttpHeaders.AUTHORIZATION, authorizationHeader).post(null);

		Assert.assertEquals(401, response.getStatus());
	}

	@Test
	@InSequence(5)
	public void authenticateUserWithoutPassword() {
		String authorizationHeader = USERNAME2 + ":jgbhsuibgh";
		authorizationHeader = Base64.encodeBase64String(authorizationHeader.getBytes());
		authorizationHeader = "Basic " + authorizationHeader;

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(authenticateURI());
		Response response = target.request().header(HttpHeaders.AUTHORIZATION, authorizationHeader).post(null);

		Assert.assertEquals(401, response.getStatus());
	}

	@Test
	@InSequence(7)
	public void authenticate() throws InterruptedException {
		Date loginDate = new Date();
		Date excpectedMaxTokenDate = new DateTime().plusSeconds(10).toDate();

		sleep();

		String authorizationHeader = USERNAME + ":" + PASSWORD;
		authorizationHeader = Base64.encodeBase64String(authorizationHeader.getBytes());
		authorizationHeader = "Basic " + authorizationHeader;

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(authenticateURI());
		Response response = target.request().header(HttpHeaders.AUTHORIZATION, authorizationHeader).post(null);

		sleep();

		Assert.assertEquals(200, response.getStatus());

		AuthenticateResponse body = response.readEntity(AuthenticateResponse.class);

		Jws<Claims> claimsAccess = Jwts.parser().setSigningKey(appConfig.getJwtKey()).parseClaimsJws(body.accessToken);
		ACCESS_TOKEN = body.accessToken;
		REFRESH_TOKEN = body.refreshToken;
		ACCESS_TOKEN_DATE = claimsAccess.getBody().getIssuedAt();

		Assert.assertTrue(claimsAccess.getBody().getIssuedAt().after(loginDate));
		Assert.assertTrue(claimsAccess.getBody().getIssuedAt().before(excpectedMaxTokenDate));
		Assert.assertEquals(JWTTokenType.AUTH.toString(), claimsAccess.getBody().get(JWTService.TOKEN_TYPE_CLAIM_FIELD, String.class));

		Date expectedExpiration = new DateTime(claimsAccess.getBody().getIssuedAt()).plusMinutes(JWTService.ACCESS_TOKEN_DURATION_MINUTES).toDate();
		Assert.assertEquals(expectedExpiration, claimsAccess.getBody().getExpiration());

		Assert.assertEquals(USERNAME, body.user.email);

		Jws<Claims> claimsRefresh = Jwts.parser().setSigningKey(appConfig.getJwtKey()).parseClaimsJws(body.refreshToken);
		Assert.assertEquals(JWTTokenType.REFRESH.toString(), claimsRefresh.getBody().get(JWTService.TOKEN_TYPE_CLAIM_FIELD, String.class));

	}

	@Test
	@InSequence(8)
	public void tryWithAuthentication() throws InterruptedException {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(tokenUserURI());
		Response response = target.request().header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader()).get();
		Assert.assertEquals(200, response.getStatus());

		sleep();

		String newToken = response.getHeaderString(AccessTokenRefreshFilter.ACCESS_TOKEN_HEADER);
		UserPrivateDTO user = response.readEntity(UserPrivateDTO.class);

		Assert.assertNotNull(newToken);
		Assert.assertEquals(USERNAME, user.email);

		Jws<Claims> claims = Jwts.parser().setSigningKey(appConfig.getJwtKey()).parseClaimsJws(newToken);
		Date expectedExpiration = new DateTime(claims.getBody().getIssuedAt()).plusMinutes(JWTService.ACCESS_TOKEN_DURATION_MINUTES).toDate();

		Assert.assertEquals(USERNAME, claims.getBody().getSubject());
		Assert.assertTrue(claims.getBody().getIssuedAt().after(ACCESS_TOKEN_DATE));
		Assert.assertEquals(expectedExpiration, claims.getBody().getExpiration());

		ACCESS_TOKEN_DATE = claims.getBody().getIssuedAt();
	}

	@Test
	@InSequence(9)
	public void refreshTokenExpired() throws InterruptedException {
		AccessTokenAPI.RefreshAccessTokenBody body = new RefreshAccessTokenBody();
		body.refreshToken = Jwts.builder()
				.setSubject(USERNAME)
				.claim(JWTService.TOKEN_TYPE_CLAIM_FIELD, JWTTokenType.REFRESH)
				.setIssuer(appConfig.getApiRoot())
				.setIssuedAt(new DateTime().minusMinutes(10).toDate())
				.setExpiration(new Date())
				.signWith(appConfig.getJwtKey())
				.compact();

		sleep();

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(refreshTokenURI());
		Response response = target.request().post(Entity.entity(body, MediaType.APPLICATION_JSON));
		Assert.assertEquals(401, response.getStatus());
	}

	@Test
	@InSequence(9)
	public void refreshTokenWrongTokenType() throws InterruptedException {
		AccessTokenAPI.RefreshAccessTokenBody body = new RefreshAccessTokenBody();
		body.refreshToken = Jwts.builder()
				.setSubject(USERNAME)
				.claim(JWTService.TOKEN_TYPE_CLAIM_FIELD, JWTTokenType.AUTH)
				.setIssuer(appConfig.getApiRoot())
				.setIssuedAt(new DateTime().minusMinutes(5).toDate())
				.setExpiration(new DateTime().plusMinutes(5).toDate())
				.signWith(appConfig.getJwtKey())
				.compact();

		sleep();

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(refreshTokenURI());
		Response response = target.request().post(Entity.entity(body, MediaType.APPLICATION_JSON));
		Assert.assertEquals(401, response.getStatus());
	}

	@Test
	@InSequence(10)
	public void refreshToken() throws InterruptedException {

		AccessTokenAPI.RefreshAccessTokenBody body = new RefreshAccessTokenBody();
		body.refreshToken = REFRESH_TOKEN;

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(refreshTokenURI());
		Response response = target.request().post(Entity.entity(body, MediaType.APPLICATION_JSON));
		Assert.assertEquals(200, response.getStatus());

		RefreshAccessTokenResponse res = response.readEntity(RefreshAccessTokenResponse.class);

		String newToken = res.accessToken;
		Assert.assertNotNull(newToken);

		Jws<Claims> claimsAcess = Jwts.parser().setSigningKey(appConfig.getJwtKey()).parseClaimsJws(newToken);

		Assert.assertEquals(JWTTokenType.AUTH.toString(), claimsAcess.getBody().get(JWTService.TOKEN_TYPE_CLAIM_FIELD, String.class));
		Assert.assertTrue(claimsAcess.getBody().getIssuedAt().after(ACCESS_TOKEN_DATE));

		Date expectedExpiration = new DateTime(claimsAcess.getBody().getIssuedAt()).plusMinutes(JWTService.ACCESS_TOKEN_DURATION_MINUTES).toDate();
		Assert.assertEquals(expectedExpiration, claimsAcess.getBody().getExpiration());

		ACCESS_TOKEN_DATE = claimsAcess.getBody().getIssuedAt();

		Assert.assertEquals(USERNAME, res.user.email);

		Jws<Claims> claimsRefresh = Jwts.parser().setSigningKey(appConfig.getJwtKey()).parseClaimsJws(body.refreshToken);
		Assert.assertEquals(JWTTokenType.REFRESH.toString(), claimsRefresh.getBody().get(JWTService.TOKEN_TYPE_CLAIM_FIELD, String.class));
	}

	@Test
	@InSequence(11)
	public void accessTokenStillValid() {

		String authorizationHeader = "Bearer " + ACCESS_TOKEN;

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(tokenUserURI());
		Response response = target.request().header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
		Assert.assertEquals(200, response.getStatus());

		UserPrivateDTO user = response.readEntity(UserPrivateDTO.class);
		Assert.assertEquals(USERNAME, user.email);
	}

}
