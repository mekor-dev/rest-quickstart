package mekor.rest.quickstart.api;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.slf4j.Logger;

import com.google.gson.Gson;

import mekor.rest.quickstart.api.utils.APIUtils;
import mekor.rest.quickstart.api.utils.CurrentRequest;
import mekor.rest.quickstart.exceptions.MailSendException;
import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.model.entities.user.credentials.ForgottenPasswordToken;
import mekor.rest.quickstart.security.control.annotations.ControlPublic;
import mekor.rest.quickstart.services.ForgottenPasswordTokenService;
import mekor.rest.quickstart.services.MailService;
import mekor.rest.quickstart.services.UserService;
import mekor.rest.quickstart.utils.constraints.Password;

/**
 * API for requesting token to change password.
 * 
 * @author mekor
 *
 */
@RequestScoped
@Transactional
@Path("/forgotten_password")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ForgottenPasswordAPI {

	@Context
	private UriInfo uriInfo;

	@Inject
	private Logger log;

	@Inject
	private APIUtils apiUtils;

	@Inject
	private CurrentRequest currentRequest;

	@Inject
	private ForgottenPasswordTokenService tokenService;

	@Inject
	private MailService mailService;

	@Inject
	private UserService userService;

	/**
	 * Request a new token for a given user. The token is sent to the given email
	 * address.
	 * 
	 * @param body Data with email of the user who requests a token.
	 * 
	 * @HTTP 204 The token has been sent to the given email (if it exists)
	 * @HTTP 400 The body is not correctly formed or the organization is incorrect
	 * @return Empty
	 * @throws MailSendException
	 */
	@POST
	@ControlPublic
	public Response requestToken(@Valid RequestTokenBody body) throws MailSendException {
		log.debug("Entering requestToken({})", body);
		User user = userService.findByEmail(body.email);
		if (user == null || user.isDeleted()) {
			log.debug("Leaving requestToken() -> 204 - No user found with email {} ", body.email);
			return Response.status(204).build();
		}
		ForgottenPasswordToken token = tokenService.createToken(user);
		mailService.sendForgottenPasswordMail(user, token.getToken(), currentRequest.getLocale());
		return Response.status(204).build();
	}

	/**
	 * Check of if the token given in parameter is valid.
	 * 
	 * @param body The token to check
	 * 
	 * @HTTP 204 The token is valid
	 * @HTTP 403 The token is not valid or does not exists.
	 * @return Empty
	 */
	@POST
	@Path("/check")
	@ControlPublic
	public Response checkToken(@Valid CheckTokenBody body) {
		log.debug("Entering checkToken({})", body);
		ForgottenPasswordToken token = tokenService.findValidByToken(body.token);
		if (token == null) {
			log.debug("Leaving CheckToken() -> 403 Invalid token");
			return apiUtils.buildError(403, "Invalid token", null, this.getClass()).build();
		}
		log.debug("Leaving checkToken() -> 204 Token valid");
		return Response.status(204).build();
	}

	/**
	 * Change the password of the user who recieved the given token
	 *
	 * @param body Data with token and new password
	 * 
	 * @HTTP 204 The password has been changed
	 * @HTTP 400 Error in the password format
	 * @HTTP 403 The token is not valid or does not exists
	 * @return Empty
	 */
	@POST
	@Path("change_password")
	@ControlPublic
	public Response changePassword(@Valid ChangePasswordBody body) {
		log.debug("Entering changePassword({})", body.token);
		ForgottenPasswordToken token = tokenService.findValidByToken(body.token);
		if (token == null) {
			log.debug("Leaving changePassword() -> 403 Invalid token");
			return apiUtils.buildError(403, "Invalid token", null, this.getClass()).build();
		}
		if (!body.newPassword.equals(body.confirmation)) {
			log.debug("Leaving changePassword() -> 400 Both password are not equals");
			return apiUtils.buildError(400, "New Password and Confirmation must be the same", null, this.getClass()).build();
		}
		userService.updatePassword(token.getUser(), body.newPassword);
		tokenService.remove(token);
		log.debug("Leaving changePassword() -> 204 Password changed");
		return Response.status(204).build();
	}

	/**
	 * Body of {@link ForgottenPasswordAPI#requestToken(RequestTokenBody)}
	 * 
	 * @author pgavo
	 *
	 */
	public static class RequestTokenBody {

		/**
		 * Email of the user who requests a new token.
		 */
		@Email
		@NotNull
		public String email;

		@Override
		public String toString() {
			return new Gson().toJson(this);
		}
	}

	/**
	 * Body of {@link ForgottenPasswordAPI#checkToken(CheckTokenBody)}
	 * 
	 * @author pgavo
	 *
	 */
	public static class CheckTokenBody {
		/**
		 * The token to check
		 */
		@NotNull
		public String token;

		@Override
		public String toString() {
			return new Gson().toJson(this);
		}
	}

	public static class ChangePasswordBody {
		/**
		 * The token needed to change the password.
		 */
		@NotNull
		public String token;

		/**
		 * The new password that the user wants
		 */
		@Password
		public String newPassword;

		/**
		 * Confirmation of the password.
		 */
		@NotNull
		public String confirmation;
	}

}
