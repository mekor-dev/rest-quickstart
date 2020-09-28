package mekor.rest.quickstart.api;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import mekor.rest.quickstart.security.control.annotations.ControlLoggedIn;
import mekor.rest.quickstart.security.control.annotations.ControlPublic;

/**
 * Used to check some basic thing. (Security annotations...)
 * 
 * @author mekor
 *
 */
@RequestScoped
@Transactional
@Path("/status")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldAPI {

	@GET
	@ControlPublic
	public Response hello() {
		return Response.status(200).build();
	}

	@GET
	@Path("/logged")
	@ControlLoggedIn
	public Response helloLogged() {
		return Response.status(200).build();
	}

	@GET
	@Path("/deny")
	public Response denyAll() {
		return Response.status(200).build();
	}

}