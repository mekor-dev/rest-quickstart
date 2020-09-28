package mekor.rest.quickstart.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;

import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.repositories.PasswordCredentialsRepository;
import mekor.rest.quickstart.repositories.UserRepository;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;
import mekor.rest.quickstart.services.utils.GenericEntityService;

/**
 * Service for managing {@link User}s.
 * 
 * @author mekor
 *
 */
@ApplicationScoped
public class UserService extends GenericEntityService<User, Long> {

	@Inject
	private Logger log;

	@Inject
	private UserRepository userRepo;

	@Inject
	private PasswordCredentialsRepository passwordCredentialRepo;

	/**
	 * Find a user by email
	 * 
	 * @param email The email of the user to find
	 * @return The user.
	 */
	public User findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	/**
	 * Check if the password given in parameter is the same as the password of the
	 * user given in parameter.
	 * 
	 * @param username The usernmae of the user to compare password with
	 * @param password The password to check
	 * @return If the password is the same, the user, null otherwise
	 */
	public User checkPasswordCredentials(String username, String password) {
		log.debug("Entering checkPasswordCredentials(username: {})", username);

		User user = findByEmail(username);
		return checkPasswordCredentials(user, password);
	}

	/**
	 * Check if the password given in parameter is the same as the password of the
	 * user given in parameter.
	 * 
	 * @param user     The user to compare password with
	 * @param password The password to check
	 * @return If the password is the same, the user, null otherwise
	 */
	public User checkPasswordCredentials(User user, String password) {
		log.debug("Entering checkPasswordCredentials(user: {})", user);
		String encryptedPassword = encryptPassword(password);

		if (user != null) {
			if (user.getPasswordCredentials() == null || !encryptedPassword.equals(user.getPasswordCredentials().getPassword())) {
				log.debug("Wrong password !");
				user = null;
			}
		}
		log.debug("Leaving checkPasswordCredentials() -> {}", user);
		return user;
	}

	/**
	 * Update the password of the user given in parameter
	 * 
	 * @param user     The user we want to update the password
	 * @param password The new password
	 * @return The updated user.
	 */
	public User updatePassword(User user, String password) {
		log.debug("Entering updatePassword(user: {})", user);
		String encryptedPassword = encryptPassword(password);
		user.getPasswordCredentials().setPassword(encryptedPassword);
		passwordCredentialRepo.merge(user.getPasswordCredentials());

		log.debug("Leaving updatePassword() -> {}", user);
		return user;
	}

	/**
	 * Encrypt a string with the password encryption algorithm (Sha256)
	 * 
	 * @param password The string to encrypt
	 * @return The encrypted string
	 */
	private String encryptPassword(String password) {
		return DigestUtils.sha256Hex(password);
	}

	@Override
	protected GenericRepostiory<User, Long> getEntityRepo() {
		return userRepo;
	}

	@Override
	protected Logger getLogger() {
		return log;
	}

}
