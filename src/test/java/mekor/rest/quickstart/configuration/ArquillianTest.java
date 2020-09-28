/**
 * 
 */
package mekor.rest.quickstart.configuration;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.SseEventSource;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.Jwts;
import mekor.rest.quickstart.api.SSEAPI;
import mekor.rest.quickstart.api.utils.APIUtils;
import mekor.rest.quickstart.api.utils.AddHeaderRequestFilter;
import mekor.rest.quickstart.configuration.AppConfig;
import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.services.JWTService;
import mekor.rest.quickstart.services.UserService;
import mekor.rest.quickstart.utils.JWTTokenType;
import net.jodah.concurrentunit.Waiter;

/**
 * Super class of every arquillian tests. Include the Shrink Package and logs of
 * tests names.
 * 
 * @author mekor
 *
 */
@ArquillianSuiteDeployment
public class ArquillianTest {

	@Inject
	private UserService userService;

	@Inject
	private AppConfig appConfig;

	@Inject
	private TestResourcesURL resourcesUrl;

	private static Logger log = LoggerFactory.getLogger(ArquillianTest.class);

	/**
	 * Add logs with entering and leaving tests.
	 */
	@Rule
	public TestRule watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			log.info("Starting test: " + description.getClassName() + "." + description.getMethodName());
		}

		@Override
		protected void finished(Description description) {
			log.info("Ending test: " + description.getClassName() + "." + description.getMethodName());
		}
	};

	/**
	 * Produces the Arquillian WebArchive archive with project classes, dependencies
	 * and resource files. <br />
	 * For more information, see project wiki or Arquillian documentation
	 * 
	 * @return The produced WebArchive
	 */
	@Deployment
	public static Archive<?> createTestArchive() {

		// Récupération de la totalité des dépendences du Pom.xml
		File[] files = Maven.resolver().loadPomFromFile("pom.xml").importCompileAndRuntimeDependencies().importTestDependencies().resolve()
				.withTransitivity().asFile();

		WebArchive war = ShrinkWrap.create(WebArchive.class, "test-rest-quickstart.war")
				.addAsLibraries(files)
				.addPackages(true, "mekor.rest.quickstart")
				.addAsWebInfResource("test-jboss-deployment-structure.xml", "jboss-deployment-structure.xml")
				.addAsManifestResource("test-jboss-deployment-structure.xml")
				.addAsResource("test-config.properties", "app-config.properties")
				.addAsResource(new File("src/main/resources/META-INF/apache-deltaspike.properties"), "META-INF/apache-deltaspike.properties")
				.addAsResource("test-log4j.properties", "log4j.properties")
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsResource("test-import.sql", "import.sql")
				.addAsResource("assets/test-upload.jpg", "assets/test-upload.jpg")
				.addAsResource("assets/test-upload-txt.txt", "assets/test-upload-txt.txt")
				.addAsResource("assets/test-upload-txt.jpg", "assets/test-upload-txt.jpg")
				.addAsResource("assets/test-upload-resize.jpg", "assets/test-upload-resize.jpg");

		return war;
	}

	public void assertIsLazyResponse(Response response) {
		Assert.assertNotNull(response.getHeaderString(APIUtils.X_PAGE_COUNT));
		Assert.assertNotNull(response.getHeaderString(APIUtils.X_PAGE_LIMIT));
		Assert.assertNotNull(response.getHeaderString(APIUtils.X_TOTAL_COUNT));
	}

	public String getAuthorizationHeader(Long userID) {
		User user = userService.findByID(userID);
		if (user == null) {
			Assert.fail("Could not connect with user with ID " + userID + ". No OrganizationMember found.");
		}
		return "Bearer " + Jwts.builder()
				.setSubject(user.getEmail())
				.claim(JWTService.TOKEN_TYPE_CLAIM_FIELD, JWTTokenType.AUTH)
				.setIssuer(appConfig.getApiRoot()).setIssuedAt(new DateTime().minusMinutes(5).toDate())
				.setExpiration(new DateTime().plusMinutes(5).toDate())
				.signWith(appConfig.getJwtKey())
				.compact();
	}

	public void registerSSE(final Long userID, final Long orgaID, final Waiter waiter) {
		Client client = ClientBuilder.newClient();
		client.register(new AddHeaderRequestFilter(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(userID)));
		WebTarget target = client.target(resourcesUrl.get(SSEAPI.class));
		final SseEventSource eventSource = SseEventSource.target(target).build();
		new Thread(() -> {
			try (SseEventSource source = eventSource) {
				source.register(
						(sseEvent) -> {
							try {
								TestSingleton.get().getSseEvents().putIfAbsent(userID, new ArrayList<>());
								TestSingleton.get().getSseEvents().get(userID).add(sseEvent);
							}
							catch (Exception e) {
								log.error("Error while processing message", e);
							}
						},
						(e) -> {
							log.error("Error while listening SSE", e);
						},
						() -> {
							log.info("Source closed");
						});
				source.open();
				waiter.await(5000);
			}
			catch (Exception e) {
				log.error("Error while creating eventSource", e);
			}
		}).start();
	}
}
