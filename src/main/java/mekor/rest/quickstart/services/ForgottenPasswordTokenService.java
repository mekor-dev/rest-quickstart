package mekor.rest.quickstart.services;

import java.util.Date;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.model.entities.user.credentials.ForgottenPasswordToken;
import mekor.rest.quickstart.repositories.ForgottenPasswordTokenRepository;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;
import mekor.rest.quickstart.services.utils.GenericEntityService;

/**
 * Service for managing {@link ForgottenPasswordToken}s.
 * 
 * @author mekor
 *
 */
@ApplicationScoped
public class ForgottenPasswordTokenService extends GenericEntityService<ForgottenPasswordToken, Long> {
	
	/**
	 * Duration for which token must be valid (in hours).
	 */
	public static final int VALIDITY_DURATION = 1;
	
	@Inject
	private Logger log;
	
	@Inject
	private ForgottenPasswordTokenRepository tokenRepo;
	
	/**
	 * Find a ForgottenPasswordToken by token. Also check if the token is valid. Return null if the token is not valid.
	 * @param tokenString The token to find
	 * @return The token if it has been found and is valid. Null otherwise.
	 */
	public ForgottenPasswordToken findValidByToken(String tokenString) {
		log.debug("Entering findValidByToken({})", tokenString);
		ForgottenPasswordToken token = tokenRepo.findByToken(tokenString);
		if (token != null && new Date().after(token.getExpirationDate())) {
			log.debug("Leaving findValidByToken() -> Token expired !");
			return null;
		}
		log.debug("Leaving findValidByToken() -> {}", token);
		return token;
	}
	
	/**
	 * Create a new token for the given user. If the token already exists, replaces it.
	 * @param user The user who requested a new token
	 * @return The newly created token.
	 */
	public ForgottenPasswordToken createToken(User user) {
		ForgottenPasswordToken token = tokenRepo.findByUser(user.getId());
		if (token == null) {
			token = new ForgottenPasswordToken();
		}
		token.setUser(user);
		token.setToken(UUID.randomUUID().toString());
		token.setExpirationDate(new DateTime().plusHours(VALIDITY_DURATION).toDate());
		
		token = tokenRepo.merge(token);
		return token;
	}

	@Override
	protected GenericRepostiory<ForgottenPasswordToken, Long> getEntityRepo() {
		return tokenRepo;
	}

	@Override
	protected Logger getLogger() {
		return log;
	}

}
