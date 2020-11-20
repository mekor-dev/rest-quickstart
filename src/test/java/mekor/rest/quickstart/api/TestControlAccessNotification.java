package mekor.rest.quickstart.api;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.net.HttpHeaders;

import mekor.rest.quickstart.configuration.ArquillianTest;
import mekor.rest.quickstart.configuration.TestResourcesURL;
import mekor.rest.quickstart.model.dtos.notification.NotificationPutDTO;

/**
 * 
 * @author pgavoille
 *
 */
@RunWith(Arquillian.class)
public class TestControlAccessNotification extends ArquillianTest {

	@Inject
	private TestResourcesURL resourcesUrl;

	public NotificationPutDTO createNotifPutDTO(Long id) {
		NotificationPutDTO res = new NotificationPutDTO();

		res.id = id;
		res.seen = true;

		return res;
	}

	@Test
	public void testAuthorized() {
		NotificationPutDTO dto = createNotifPutDTO(1L);
		Entity<NotificationPutDTO> body = Entity.entity(dto, MediaType.APPLICATION_JSON);

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(NotificationAPI.class) + "/1");
		Response response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
				.put(body);

		Assert.assertEquals(200, response.getStatus());
	}

	@Test
	public void testForbidden() {
		NotificationPutDTO dto = createNotifPutDTO(2L);
		Entity<NotificationPutDTO> body = Entity.entity(dto, MediaType.APPLICATION_JSON);

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(resourcesUrl.get(NotificationAPI.class) + "/2");
		Response response = target.request()
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(1L))
				.put(body);

		Assert.assertEquals(403, response.getStatus());
	}

}
