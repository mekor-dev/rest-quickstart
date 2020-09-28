package mekor.rest.quickstart.api;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

/**
 * API for handling users.
 * 
 * @author mekor
 *
 */
@RequestScoped
@Transactional
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserAPI {

}
