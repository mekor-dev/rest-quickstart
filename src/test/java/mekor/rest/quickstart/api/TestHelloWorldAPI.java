/**
 * 
 */
package mekor.rest.quickstart.api;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.net.HttpHeaders;

import mekor.rest.quickstart.api.HelloWorldAPI;
import mekor.rest.quickstart.api.utils.error.APIError;
import mekor.rest.quickstart.configuration.ArquillianTest;
import mekor.rest.quickstart.configuration.TestResourcesURL;

/**
 * @author mekor
 *
 */
@RunWith(Arquillian.class)
public class TestHelloWorldAPI extends ArquillianTest {

	@Inject
	private TestResourcesURL resourcesUrl;

	@Test
	public void testHello() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(HelloWorldAPI.class));
		Response response = target.request()
				.get();

		Assert.assertEquals(200, response.getStatus());

	}

	@Test
	public void testHelloLogged() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(HelloWorldAPI.class) + "/logged");
		Response response = target.request()
				.get();

		Assert.assertEquals(401, response.getStatus());

		client = ClientBuilder.newClient();
		target = client.target(resourcesUrl.get(HelloWorldAPI.class) + "/logged");
		response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(7L))
				.get();

		Assert.assertEquals(200, response.getStatus());
	}

	@Test
	public void testDeny() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(HelloWorldAPI.class) + "/deny");
		Response response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
				.get();

		Assert.assertEquals(500, response.getStatus());

		APIError err = response.readEntity(APIError.class);
		Assert.assertEquals("Error while authorizing access to the service", err.exception.message);
	}

}
