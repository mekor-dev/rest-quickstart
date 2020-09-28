/**
 * 
 */
package mekor.rest.quickstart.security.control;

import java.io.IOException;
import java.lang.reflect.Parameter;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;

import mekor.rest.quickstart.api.utils.APIUtils;
import mekor.rest.quickstart.api.utils.CurrentRequest;
import mekor.rest.quickstart.security.authorization.AuthorizationException;
import mekor.rest.quickstart.security.authorization.AuthorizationService;
import mekor.rest.quickstart.security.control.annotations.ControlAccessNotification;
import mekor.rest.quickstart.security.control.annotations.ControlAdmin;
import mekor.rest.quickstart.security.control.annotations.ControlLoggedIn;
import mekor.rest.quickstart.security.control.annotations.ControlPublic;
import mekor.rest.quickstart.security.control.annotations.param.ControlParam;
import mekor.rest.quickstart.services.NotificationService;

/**
 * 
 * Allow to control access to API methods. <br />
 * <br />
 * Every API method must be annotated with one the annotation from the package
 * mekor.rest.quickstart.security.control.annotations <br />
 * Check those annotations to know what param you need to annotate with the
 * {@link ControlParam} annotation. <br />
 * ControlParam annotation values are found in {@link ControlParams} class.
 * 
 * @author mekor
 *
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
public class ApiControlFilter implements ContainerRequestFilter {

	@Inject
	private Logger log;

	@Context
	private ResourceInfo resourceInfo;

	@Context
	private HttpServletRequest request;

	@Context
	private UriInfo uriInfo;

	@Inject
	private APIUtils apiUtils;

	@Inject
	private CurrentRequest currentRequest;

	@Inject
	private AuthorizationService authorization;

	@Inject
	private NotificationService notifService;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		log.debug("Entering filter()");
		// ControlAdmin
		if (apiUtils.isAnnotationPresent(resourceInfo, ControlAdmin.class)) {
			authorization.isAdmin();
		}
		// ControlAccessNotification
		else if (apiUtils.isAnnotationPresent(resourceInfo, ControlAccessNotification.class)) {
			Long notifID = findControlParamValue(ControlParams.NOTIFICATION_ID, Long.class);
			log.debug("ControlAccessNotification for notifID : {}", notifID);
			authorization.canAccessNotification(notifService.findByIDHandleNotFound(notifID, currentRequest.isAdmin()));
		}
		// ControlPublic - ControlLoggedIn
		else if (apiUtils.isAnnotationPresent(resourceInfo, ControlPublic.class)
				|| apiUtils.isAnnotationPresent(resourceInfo, ControlLoggedIn.class)) {
			log.debug("Method annoted with ControlPublic, ControlLoggedIn or ControlAdmin. No check performed.");
		}
		// No annotation
		else {
			log.error("No control annotation found on the service method");
			throw new AuthorizationException(500, "Error while authorizing access to the service");
		}
		log.debug("Leaving filter()");
	}

	/**
	 * Find in pathParam the parameter value corresponding to the controlParam and
	 * cast it to the type given in parameter.
	 * 
	 * @param controlParam Value of the controlParam annotating the parameter to
	 *                     find
	 * @param type         Type of the parameter
	 * @return The parameter value.
	 */
	private <T> T findControlParamValue(ControlParams controlParam, Class<T> type) {
		try {
			if (type.isAssignableFrom(Long.class)) {
				for (Parameter param : resourceInfo.getResourceMethod().getParameters()) {
					ControlParam annotation = param.getAnnotation(ControlParam.class);
					if (annotation != null) {
						if (controlParam == annotation.value()) {
							return type.cast(Long.parseLong(uriInfo.getPathParameters().getFirst(param.getAnnotation(PathParam.class).value())));
						}
					}
				}
			}
		}
		catch (Exception e) {
			log.error("Could not find pathParam value for controlParam {} and type {}", controlParam, type, e);
			throw new AuthorizationException(500, "Error while authorizing access to the service");
		}

		log.error("Could not find pathParam value for controlParam {} and type {}", controlParam, type);
		throw new AuthorizationException(500, "Error while authorizing access to the service");
	}

}
