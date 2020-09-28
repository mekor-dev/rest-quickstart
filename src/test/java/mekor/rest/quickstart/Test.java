/**
 * 
 */
package mekor.rest.quickstart;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import mekor.rest.quickstart.services.JWTService;
import mekor.rest.quickstart.utils.JWTTokenType;

/**
 * @author mekor
 *
 */
public class Test {

	@org.junit.Test
	public void test() {
		Long.parseLong("6.0");
	}

	/**
	 * Generate a HS256 key for signing JWT keys
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	@org.junit.Test
	public void generateJWTKey() throws NoSuchAlgorithmException {
		Key key = KeyGenerator.getInstance(SignatureAlgorithm.HS256.getJcaName()).generateKey();

		System.out.println("generateJWTKey() -> " + Base64.encodeBase64String(key.getEncoded()));
	}

	/**
	 * Encrypt a password with sha256 algorithm.
	 */
	@org.junit.Test
	public void encryptPassword() {
		String password = "passwordTest";

		System.out.println("encryptPassword() -> " + DigestUtils.sha256Hex(password));
	}

	/**
	 * Returns the current time
	 */
	@org.junit.Test
	public void currentTime() {
		Date current = new Date();
		System.out.println(current);
		System.out.println(current.getTime());
	}

	@org.junit.Test
	public void getJWTToken() {
		Key jwtKey = new SecretKeySpec(Base64.decodeBase64("NUd40nSrEpxqqZ8WHuxHqpudjaXQ4S8U578bBwYyfNg="), SignatureAlgorithm.HS256.getJcaName());

		String token = Jwts.builder()
				.setSubject("test@mail.com")
				.claim(JWTService.TOKEN_TYPE_CLAIM_FIELD, JWTTokenType.AUTH)
				.setIssuer("issuer")
				.setIssuedAt(new Date())
				.setExpiration(new DateTime().plusMonths(1).toDate())
				.signWith(jwtKey)
				.compact();

		System.out.println("getJWTToken() -> " + token);
	}

	/**
	 * Returns The type of a QBean
	 */
	@org.junit.Test
	public void getRandomUUID() {
		System.out.println("getRandomUUID() -> " + UUID.randomUUID().toString());
	}
}
