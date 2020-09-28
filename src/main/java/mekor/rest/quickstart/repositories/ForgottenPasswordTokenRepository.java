package mekor.rest.quickstart.repositories;

import javax.enterprise.context.RequestScoped;

import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.SingleResultType;

import com.querydsl.core.types.dsl.EntityPathBase;

import mekor.rest.quickstart.model.entities.user.credentials.ForgottenPasswordToken;
import mekor.rest.quickstart.model.entities.user.credentials.QForgottenPasswordToken;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * Repository for the {@link ForgottenPasswordToken} entity
 * 
 * @author mekor
 *
 */
@RequestScoped
@Repository
public abstract class ForgottenPasswordTokenRepository extends GenericRepostiory<ForgottenPasswordToken, Long> {

	@Query(value = "SELECT t FROM ForgottenPasswordToken t WHERE t.user.id = :userID", singleResult = SingleResultType.OPTIONAL)
	public abstract ForgottenPasswordToken findByUser(@QueryParam("userID") Long userID);

	@Query(value = "SELECT t FROM ForgottenPasswordToken t WHERE t.token = :token", singleResult = SingleResultType.OPTIONAL)
	public abstract ForgottenPasswordToken findByToken(@QueryParam("token") String token);

	@Override
	protected EntityPathBase<ForgottenPasswordToken> getQBean() {
		return QForgottenPasswordToken.forgottenPasswordToken;
	}

}
