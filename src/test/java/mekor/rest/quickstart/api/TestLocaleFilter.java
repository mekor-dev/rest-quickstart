/**
 * 
 */
package mekor.rest.quickstart.api;

import static org.junit.Assume.assumeTrue;

import java.util.Locale;

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

import mekor.rest.quickstart.api.AccessTokenAPI;
import mekor.rest.quickstart.configuration.ArquillianTest;
import mekor.rest.quickstart.configuration.TestResourcesURL;
import mekor.rest.quickstart.utils.LocaleUtils;

/**
 * @author mekor
 *
 */
@RunWith(Arquillian.class)
public class TestLocaleFilter extends ArquillianTest {

	@Inject
	private TestResourcesURL resourcesUrl;

	public String callURI() {
		return resourcesUrl.get(AccessTokenAPI.class) + "/user";
	}

	@Test
	public void checkSupportedLanguage() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(callURI());
		Response response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
				.header(HttpHeaders.ACCEPT_LANGUAGE, Locale.ENGLISH)
				.get();

		Assert.assertEquals(200, response.getStatus());

		Assert.assertEquals(Locale.ENGLISH.getLanguage(), response.getHeaderString(HttpHeaders.CONTENT_LANGUAGE));
	}

	public void checkSupportedStringLanguage() {
		assumeTrue(LocaleUtils.supportedLocales().contains(Locale.ENGLISH));
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(callURI());
		Response response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
				.header(HttpHeaders.ACCEPT_LANGUAGE, "en")
				.get();

		Assert.assertEquals(200, response.getStatus());

		Assert.assertEquals(Locale.ENGLISH.getLanguage(), response.getHeaderString(HttpHeaders.CONTENT_LANGUAGE));
	}

	@Test
	public void checkUnsupportedLanguage() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(callURI());
		Response response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
				.header(HttpHeaders.ACCEPT_LANGUAGE, "fafazfza")
				.get();

		Assert.assertEquals(200, response.getStatus());

		Assert.assertEquals(Locale.FRENCH.getLanguage(), response.getHeaderString(HttpHeaders.CONTENT_LANGUAGE));
	}

	@Test
	public void checkNoLanguage() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(callURI());
		Response response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
				.get();

		Assert.assertEquals(200, response.getStatus());

		Assert.assertEquals(Locale.FRENCH.getLanguage(), response.getHeaderString(HttpHeaders.CONTENT_LANGUAGE));
	}
}
