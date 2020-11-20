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

import mekor.rest.quickstart.configuration.AppConfig;
import mekor.rest.quickstart.configuration.ArquillianTest;
import mekor.rest.quickstart.configuration.TestResourcesURL;
import mekor.rest.quickstart.configuration.requestfilters.HeadersFilter;

/**
 * @author mekor
 *
 */
@RunWith(Arquillian.class)
public class TestHeadersFilter extends ArquillianTest {

	@Inject
	private TestResourcesURL resourcesUrl;

	@Inject
	private AppConfig appConfig;

	@Test
	public void testCorsFilter() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(AccessTokenAPI.class) + "/user");
		Response response = target.request().header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L)).get();
		Assert.assertEquals(200, response.getStatus());

		boolean found = false;
		for (String domain : appConfig.getCorsDomains()) {
			if (response.getHeaderString(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN).equals(domain)) {
				found = true;
				break;
			}
		}

		Assert.assertTrue(found);
		Assert.assertEquals(HeadersFilter.ALLOW_METHODS, response.getHeaderString(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS));
		Assert.assertEquals(HeadersFilter.ALLOW_HEADERS, response.getHeaderString(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS));
		Assert.assertEquals(HeadersFilter.EXPOSE_HEADER, response.getHeaderString(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS));
	}

}
