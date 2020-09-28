/**
 * 
 */
package mekor.rest.quickstart.api;

import java.util.StringJoiner;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.slf4j.Logger;

import com.webcohesion.enunciate.metadata.rs.TypeHint;

import mekor.rest.quickstart.api.utils.APIUtils;
import mekor.rest.quickstart.api.utils.CurrentRequest;
import mekor.rest.quickstart.exceptions.LazyLoadingQueryException;
import mekor.rest.quickstart.model.dtos.notification.NotificationDTO;
import mekor.rest.quickstart.model.dtos.notification.NotificationPutDTO;
import mekor.rest.quickstart.model.entities.notification.Notification;
import mekor.rest.quickstart.model.entities.notification.QNotification;
import mekor.rest.quickstart.model.entities.user.QUser;
import mekor.rest.quickstart.model.mappers.NotificationMapper;
import mekor.rest.quickstart.model.mappers.NotificationPutMapper;
import mekor.rest.quickstart.security.control.ControlParams;
import mekor.rest.quickstart.security.control.annotations.ControlAccessNotification;
import mekor.rest.quickstart.security.control.annotations.ControlLoggedIn;
import mekor.rest.quickstart.security.control.annotations.param.ControlParam;
import mekor.rest.quickstart.services.NotificationService;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingConfiguration;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingFilterOperator;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingResult;

/**
 * API for managing Notifications.
 * 
 * @author mekor
 *
 */
@RequestScoped
@Transactional
@Path("/notifications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NotificationAPI {

	@Inject
	private Logger log;

	@Context
	private UriInfo uriInfo;
	@Inject
	private CurrentRequest currentRequest;

	@Inject
	private APIUtils apiUtils;

	@Inject
	private NotificationMapper notifMapper;

	@Inject
	private NotificationPutMapper notifPutMapper;

	@Inject
	private NotificationService notifService;

	/**
	 * Returns the lazyList of all Notifications
	 * 
	 * <p>
	 * Filters already applied :
	 * </p>
	 * <ul>
	 * <li>orga.id : current</li>
	 * </ul>
	 * 
	 * @return The list of notifications
	 * @throws LazyLoadingQueryException Thrown if the LazyLoading query parameters
	 *                                   are incorrect.
	 * @HTTP 200 The list of jobs has been returned
	 */
	@GET
	@TypeHint(NotificationDTO.class)
	@ControlLoggedIn
	public Response getAll() throws LazyLoadingQueryException {
		log.debug("Entering getAll()");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(uriInfo.getQueryParameters(), currentRequest.getLocale(), false, currentRequest.isAdmin());
		// Filter user
		String userIDKey = new StringJoiner(".")
				.add(QNotification.notification.user.getMetadata().getName())
				.add(QUser.user.id.getMetadata().getName())
				.toString();
		config.addFilter(userIDKey, LazyLoadingFilterOperator.EQUALS, currentRequest.getUser().getId());

		LazyLoadingResult<Notification> res = notifService.findLazyLoading(config);
		log.debug("Leaving getAll() -> {}", res);
		return apiUtils.buildLazyLoadingResponse(res, notifMapper).build();
	}

	@PUT
	@TypeHint(NotificationDTO.class)
	@ControlAccessNotification
	@Path("/{id}")
	public Response put(@ControlParam(ControlParams.NOTIFICATION_ID) @PathParam("id") Long id, @Valid @NotNull NotificationPutDTO body) {
		log.debug("Entering put(id: {}, body: {})", id, body);

		if (!id.equals(body.id)) {
			log.debug("Leaving put() -> 403 : Wrong ID");
			return apiUtils.buildError(403, "Trying to update the wrong notification", null, this.getClass()).build();
		}

		Notification notif = notifPutMapper.DTOToEntity(body);
		notifService.merge(notif);

		log.debug("Leaving put() -> {}", notif);
		return Response.status(200).entity(notifMapper.entityToDTO(notif)).build();
	}
}
