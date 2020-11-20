/**
 * 
 */
package mekor.rest.quickstart.security.control;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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
			ControlAccessNotification annotation = apiUtils.getAnnotation(resourceInfo, ControlAccessNotification.class);
			Long notifID = Long.parseLong(findPathParamValue(annotation.notifID()));
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
	 * Find a pathParam by name
	 * 
	 * @param pathparam Name of the pathParameter to find
	 * @param type      Type of the parameter
	 * @return The parameter value.
	 */
	private String findPathParamValue(String pathparam) {
		for (Entry<String, List<String>> param : uriInfo.getPathParameters().entrySet()) {
			if (param.getKey().equals(pathparam)) {
				return param.getValue().get(0);
			}
		}

		log.error("Could not find pathParam with given name for notifID param");
		throw new AuthorizationException(500, "Error while authorizing access to the service");
	}

}
