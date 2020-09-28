/**
 * 
 */
package mekor.rest.quickstart.api;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

import org.apache.commons.lang3.mutable.MutableLong;
import org.slf4j.Logger;

import mekor.rest.quickstart.api.utils.CurrentRequest;
import mekor.rest.quickstart.model.dtos.notification.NotificationDTO;
import mekor.rest.quickstart.model.entities.notification.Notification;
import mekor.rest.quickstart.model.entities.utils.LocalizedString;
import mekor.rest.quickstart.model.mappers.NotificationMapper;
import mekor.rest.quickstart.security.control.annotations.ControlAdmin;
import mekor.rest.quickstart.security.control.annotations.ControlLoggedIn;

/**
 * Allow users to subscribe and revieve Server-Sent Events
 * 
 * @author mekor
 *
 */
@ApplicationScoped
@Path("/sse")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SSEAPI {

	@Context
	private UriInfo uriInfo;

	@Inject
	private Logger log;

	@Inject
	private CurrentRequest currentRequest;

	@Inject
	private NotificationMapper notifMapper;

	/**
	 * Map containing the broadcaster of each user.
	 */
	private Map<Long, SseBroadcaster> broadcasters = new ConcurrentHashMap<>();

	/**
	 * Map containing the ID of the last event sent for each user.
	 */
	private Map<Long, MutableLong> eventIDs = new ConcurrentHashMap<>();

	/**
	 * The SSE instance. Used to create broadcaster
	 */
	private Sse sse;

	/**
	 * EventBuilder used to create new events
	 */
	private OutboundSseEvent.Builder eventBuilder;

	@Context
	public void setSse(Sse sse) {
		this.sse = sse;
		this.eventBuilder = sse.newEventBuilder();
	}

	/**
	 * Subscribe the user.
	 * 
	 * @param eventSink
	 */
	@GET
	@ControlLoggedIn
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void subscribe(@Context SseEventSink eventSink) {
		log.debug("Entering subscribe()");
		Long userID = currentRequest.getUser().getId();
		subscribe(userID, eventSink);

		final OutboundSseEvent comment = eventBuilder
				.name("HeatBeat")
				.comment("Heartbeat")
				.build();

		new Thread(() -> {
			while (true) {
				try {
					if (!eventSink.isClosed()) {
						// Wait 30 sec before HeartBeat.
						Thread.sleep(30000);
						eventSink.send(comment);
					}
					else {
						log.debug("Sink closed, stopping HeartBeat");
						break;
					}
				}
				catch (Exception e) {
					log.debug("Error while sending HeartBeat");
				}
			}
		}).start();

		log.debug("Leaving subscribe() -> User {} subscribed", userID);
	}

	/**
	 * ADMIN ONLY : Send a test notification to a given user.
	 * 
	 * @param userID The user we want to send notification to
	 * @return
	 */
	@GET
	@Path("/test/{userID}")
	@ControlAdmin
	public Response test(@PathParam("userID") Long userID) {
		log.debug("Entering test()");

		Notification notif = new Notification();
		notif.setTitle(new LocalizedString());
		notif.getTitle().setText("Test for user " + userID, Locale.FRENCH);

		sendNotification(userID, notif);

		log.debug("Leaving test() -> 200");
		return Response.status(200).build();
	}

	/**
	 * Add a new {@link SseEventSink} to the {@link #broadcasters} map.
	 * 
	 * @param userID    ID of the user
	 * @param eventSink
	 */
	public void subscribe(Long userID, SseEventSink eventSink) {

		broadcasters.computeIfAbsent(userID, (k) -> {
			SseBroadcaster broadcaster = sse.newBroadcaster();
			broadcaster.onError((sink, th) -> log.debug("Error while sending message - {}", th.getMessage()));
			broadcaster.onClose((sink) -> log.debug("Sink closed"));
			return broadcaster;
		});
		broadcasters.get(userID).register(eventSink);

		eventIDs.putIfAbsent(userID, new MutableLong(1));

	}

	/**
	 * Send a notification to the given user using SSE
	 * 
	 * @param userID User we want to send the notification to
	 * @param notif  The notification to send
	 */
	public void sendNotification(Long userID, Notification notif) {
		log.debug("Entering sendNotification({}, {})", userID, notif);
		SseBroadcaster userBroadcaster = broadcasters.get(userID);
		if (userBroadcaster != null) {
			NotificationDTO notifDTO = notifMapper.entityToDTO(notif);
			OutboundSseEvent event = eventBuilder
					.name("Notification")
					.id(eventIDs.get(userID).toString())
					.mediaType(MediaType.APPLICATION_JSON_TYPE)
					.data(NotificationDTO.class, notifDTO)
					.build();
			userBroadcaster.broadcast(event);

			eventIDs.get(userID).increment();
			log.debug("Leaving sendNotification() -> Notification sent to user {}", userID);
		}
		else {
			log.debug("Leaving sendNotification() -> The user {} has no broadcaster", userID);
		}
	}

}
