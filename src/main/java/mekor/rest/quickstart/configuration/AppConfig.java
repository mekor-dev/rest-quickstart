package mekor.rest.quickstart.configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.apache.deltaspike.core.api.resourceloader.InjectableResource;
import org.slf4j.Logger;

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Holds parameter of the application. Inited at the startup with correct values
 * depending on the development stage.
 * 
 * @author mekor
 *
 */
@ApplicationScoped
public class AppConfig {

	public static final SignatureAlgorithm JWT_SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

	@Inject
	private Logger log;

	/**
	 * The Properties of the app-config.properties file. Used to init the controller
	 */
	@Inject
	@InjectableResource(location = "app-config.properties")
	private Properties configProps;

	/**
	 * Stage of the project. Can be [local, development, production]
	 */
	private String projectStage;

	/**
	 * Root URL of the API
	 */
	private String apiRoot;

	/**
	 * Root URL of the API documentation
	 */
	private String apiDocsRoot;

	/**
	 * A request counter used to log requestID
	 */
	private long nbRequest = 0l;

	/**
	 * Secret password for encrypting JSON Web Tokens.
	 */
	private Key jwtKey;

	/**
	 * List of authorized domains for CORS requests.
	 */
	private List<String> corsDomains;

	/**
	 * Root URL of the CDN.
	 */
	private String cdnRoot;

	/**
	 * Root path for uploading file on the CDN
	 */
	private String cdnUploadRoot;

	/**
	 * Root URL of the front server
	 */
	private String frontUrl;

	/**
	 * Init this controllers using the {@link #appConfigStream}.
	 * 
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * 
	 * 
	 */
	public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
		log.debug("Entering init()");

		// Setting properties
		projectStage = configProps.getProperty("project.stage");
		apiRoot = configProps.getProperty("core.api");
		apiDocsRoot = configProps.getProperty("core.api.docs");
		frontUrl = configProps.getProperty("front.url");

		// Parsing JWT secret
		try {
			String jwtSecret = configProps.getProperty("core.jwt.secret");
			jwtKey = new SecretKeySpec(Base64.decodeBase64(jwtSecret), JWT_SIGNATURE_ALGORITHM.getJcaName());
		}
		catch (Exception e) {
			log.error("Could not init AppConfig controller: Error while parsing core.jwt.secret parameter", e);
			return;
		}

		// CORS domains
		String[] domains = configProps.getProperty("cors.domains").split(",");
		corsDomains = Arrays.asList(domains);

		// CDN
		cdnRoot = configProps.getProperty("cdn.root");
		cdnUploadRoot = configProps.getProperty("cdn.upload.root");

		// Logging properties
		log.info("-------------------------------------------------");

		log.info("AppConfig controller has inited with following values:");
		log.info("Project Stage: {}", projectStage);
		log.info("API Root: {}", apiRoot);
		log.info("API Documentation Root: {}", apiDocsRoot);
		log.info("CORS domains authorized: {}", corsDomains);
		log.info("CDN root: {}", cdnRoot);
		log.info("CDN upload root: {}", cdnUploadRoot);
		log.info("Front URL: {}", frontUrl);

		log.info("-------------------------------------------------");
		log.debug("Leaving init()");
	}

	/**
	 * Increment and return the request counter. If it reaches 10000, reset it.
	 * 
	 * @return the updated counter
	 */
	public long newRequest() {
		nbRequest++;
		if (nbRequest >= 10000) {
			nbRequest = 1l;
		}
		return nbRequest;
	}

	/**
	 * 
	 * @return True if project stage is local.
	 */
	public boolean isLocal() {
		return "local".equals(projectStage);
	}

	/**
	 * 
	 * @return True if project stage is development.
	 */
	public boolean isDevelopment() {
		return "development".equals(projectStage);
	}

	/**
	 * 
	 * @return True if project stage is production.
	 */
	public boolean isProduction() {
		return "production".equals(projectStage);
	}

	/**
	 * @see {@link #apiDocsRoot}
	 */
	public String getApiDocsRoot() {
		return apiDocsRoot;
	}

	/**
	 * @see {@link #jwtKey}
	 */
	public Key getJwtKey() {
		return jwtKey;
	}

	/**
	 * @see {@link #apiRoot}
	 */
	public String getApiRoot() {
		return apiRoot;
	}

	/**
	 * @see {@link #corsDomains}
	 */
	public List<String> getCorsDomains() {
		return corsDomains;
	}

	/**
	 * @see {@link #cdnRoot}
	 */
	public String getCdnRoot() {
		return cdnRoot;
	}

	/**
	 * @see {@link #cdnUploadRoot}
	 */
	public String getCdnUploadRoot() {
		return cdnUploadRoot;
	}

	/**
	 * @see {@link #frontUrl}
	 */
	public String getFrontUrl() {
		return frontUrl;
	}
}
