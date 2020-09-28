package mekor.rest.quickstart.repositories;

import java.util.Locale;

import javax.enterprise.context.RequestScoped;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.SingleResultType;

import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import mekor.rest.quickstart.model.entities.user.QUser;
import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingFilter;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingOrder;

/**
 * Repository for the {@link User} entity
 * 
 * @author mekor
 *
 */
@Repository
@RequestScoped
public abstract class UserRepository extends GenericRepostiory<User, Long> {

	/**
	 * Filter used for test purpose
	 */
	public static final String FILTER_CUSTOM_TEST = "customFilterTest";

	/**
	 * Order used for test purpose
	 */
	public static final String ORDER_CUSTOM_TEST = "customOrderTest";

	/**
	 * Find a user by Email
	 * 
	 * @param email The email of the user to find
	 * @return The user.
	 */
	@Query(value = "SELECT u FROM User u WHERE u.email = :email AND u.deleted = false", singleResult = SingleResultType.OPTIONAL)
	public abstract User findByEmail(@QueryParam("email") String email);

	@Override
	protected JPAQuery<User> handleFilter(LazyLoadingFilter filter, JPAQuery<User> query, Locale locale,
			PathBuilder<Object> pathBuilder, MutableInt nbAliases) {
		if (FILTER_CUSTOM_TEST.equals(filter.getKey())) {
			int value = Integer.parseInt((String) filter.getValue());
			return query.where(QUser.user.nickname.length().eq(value));
		}
		return super.handleFilter(filter, query, locale, pathBuilder, nbAliases);
	}

	@Override
	protected JPAQuery<User> handleOrder(LazyLoadingOrder order, JPAQuery<User> query, Locale locale,
			PathBuilder<Object> pathBuilder, MutableInt nbAliases) {
		if (ORDER_CUSTOM_TEST.equals(order.getKey())) {
			switch (order.getOperator()) {
			case ASCENDANT:
				return query.orderBy(QUser.user.nickname.length().asc());
			case DESCENDANT:
				return query.orderBy(QUser.user.nickname.length().desc());
			}
		}
		return super.handleOrder(order, query, locale, pathBuilder, nbAliases);
	}

	@Override
	public EntityPathBase<User> getQBean() {
		return QUser.user;
	}

}
