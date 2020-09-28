/**
 * 
 */
package mekor.rest.quickstart.repositories;

import javax.enterprise.context.RequestScoped;

import org.apache.deltaspike.data.api.Repository;

import com.querydsl.core.types.dsl.EntityPathBase;

import mekor.rest.quickstart.model.entities.user.credentials.PasswordCredentials;
import mekor.rest.quickstart.model.entities.user.credentials.QPasswordCredentials;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * 
 * Repository for the {@link PasswordCredentials} entity
 * 
 * @author mekor
 *
 */
@Repository
@RequestScoped
public abstract class PasswordCredentialsRepository extends GenericRepostiory<PasswordCredentials, Long> {

	@Override
	protected EntityPathBase<PasswordCredentials> getQBean() {
		return QPasswordCredentials.passwordCredentials;
	}

}
