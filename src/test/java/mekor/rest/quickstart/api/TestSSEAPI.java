/**
 * 
 */
package mekor.rest.quickstart.api;

import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.InboundSseEvent;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.net.HttpHeaders;

import mekor.rest.quickstart.api.SSEAPI;
import mekor.rest.quickstart.configuration.ArquillianTest;
import mekor.rest.quickstart.configuration.TestResourcesURL;
import mekor.rest.quickstart.configuration.TestSingleton;
import mekor.rest.quickstart.model.dtos.notification.NotificationDTO;
import net.jodah.concurrentunit.Waiter;

/**
 * @author mekor
 *
 */
@RunWith(Arquillian.class)
public class TestSSEAPI extends ArquillianTest {

	@Inject
	private TestResourcesURL resourcesUrl;

	@Test
	public void testSSEForbidden() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(SSEAPI.class));
		Response response = target.request()
				.get();
		Assert.assertEquals(401, response.getStatus());
	}

	@Test
	public void testSSE() throws InterruptedException {

		final Waiter waiter = new Waiter();
		TestSingleton.get().getSseEvents().clear();

		// Registering users
		registerSSE(1L, 1L, waiter);
		registerSSE(1L, 1L, waiter);
		registerSSE(2L, 1L, waiter);
		registerSSE(3L, 1L, waiter);

		Thread.sleep(1000);

		// Send notif to user 1
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(SSEAPI.class) + "/test/1");
		Response response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(4L))
				.get();
		Assert.assertEquals(200, response.getStatus());

		// Send notif to user 3
		client = ClientBuilder.newClient();
		target = client.target(resourcesUrl.get(SSEAPI.class) + "/test/3");
		response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(4L))
				.get();
		Assert.assertEquals(200, response.getStatus());

		// Send notif to user 4
		client = ClientBuilder.newClient();
		target = client.target(resourcesUrl.get(SSEAPI.class) + "/test/4");
		response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(4L))
				.get();
		Assert.assertEquals(200, response.getStatus());

		Thread.sleep(1000);

		waiter.resume();

		Assert.assertEquals(2, TestSingleton.get().getSseEvents().size());
		int found = 0;
		for (Entry<Long, List<InboundSseEvent>> entry : TestSingleton.get().getSseEvents().entrySet()) {
			if (entry.getKey().equals(1L)) {
				Assert.assertEquals(2, entry.getValue().size());
				for (InboundSseEvent event : entry.getValue()) {
					Assert.assertEquals("Notification", event.getName());
					NotificationDTO notif = event.readData(NotificationDTO.class, MediaType.APPLICATION_JSON_TYPE);
					Assert.assertEquals("Test for user 1", notif.title.map.get(Locale.FRENCH.toString()));
				}
				found++;
			}
			if (entry.getKey().equals(3L)) {
				Assert.assertEquals(1, entry.getValue().size());
				Assert.assertEquals("Notification", entry.getValue().get(0).getName());
				NotificationDTO notif = entry.getValue().get(0).readData(NotificationDTO.class, MediaType.APPLICATION_JSON_TYPE);
				Assert.assertEquals("Test for user 3", notif.title.map.get(Locale.FRENCH.toString()));
				found++;
			}
		}
		Assert.assertEquals(2, found);

	}

}
