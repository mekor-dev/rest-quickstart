package mekor.rest.quickstart.repositories.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.deltaspike.data.api.AbstractFullEntityRepository;
import org.slf4j.Logger;

import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;

import mekor.rest.quickstart.exceptions.LazyLoadingQueryException;
import mekor.rest.quickstart.model.entities.utils.GenericEntity;
import mekor.rest.quickstart.model.entities.utils.LocalizedString;
import mekor.rest.quickstart.model.entities.utils.QGenericEntity;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingConfiguration;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingConfigurationToDSL;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingFilter;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingOrder;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingPaginationInfo;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingResult;

/**
 * The generic repository with methods compatible with every entity.
 * 
 * @author mekor
 *
 * @param <E> The entity the Repository implementation manages
 */
public abstract class GenericRepostiory<E extends GenericEntity, PK extends Serializable>
		extends AbstractFullEntityRepository<E, PK> {

	/**
	 * Name of the field of a jsonText for {@link LocalizedString} support.
	 */
	public static final String FIELD_LOCALIZED_TEXT = "localizedText";

	@Inject
	private Logger log;

	@Inject
	private LazyLoadingConfigurationToDSL configToDSL;

	/**
	 * The pathbuilder of this entity. Used for findAllLazyLoading()
	 */
	protected PathBuilder<Object> pathBuilder;

	public GenericRepostiory() {
		super();
		pathBuilder = new PathBuilder<Object>(getQBean().getType(), getQBean().getMetadata());
	}

	/**
	 * Find an entity by ID without using cache.
	 * 
	 * @param id the ID of the entity to find
	 * @return The entity
	 */
	public E noCacheFindBy(PK id) {
		flush();
		clear();
		return findBy(id);
	}

	/**
	 * Create and execute a SELECT query using the LazyLoadingConfiguration given in
	 * parameter.
	 * 
	 * @param config The configuration for this query
	 * @return The entities list and PaginationInfo for the constructed query.
	 * @throws LazyLoadingQueryException
	 */
	public LazyLoadingResult<E> findAllLazyLoading(LazyLoadingConfiguration config) throws LazyLoadingQueryException {

		try {
			JPAQuery<E> query = allLazyLoadingQuery(config);
			return createLazyLoadingResult(config, query);
		}
		catch (Exception e) {
			throw new LazyLoadingQueryException("Error while creating or executing the LazyLoading Query (This can be a server error)", e);
		}
	}

	/**
	 * Create and execute a COUNT query using the LazyLoadingConfiguration given in
	 * parameter.
	 * 
	 * @param config The configuration for this query
	 * @return The number of entities found for the constructed query
	 * @throws LazyLoadingQueryException
	 */
	public Long countAllLazyLoading(LazyLoadingConfiguration config) throws LazyLoadingQueryException {

		try {
			JPAQuery<E> query = allLazyLoadingQuery(config);

			// Offset / Limit
			query.offset(config.getOffset());
			if (config.getLimit() > 0) {
				query.limit(config.getLimit());
			}
			return query.fetchCount();
		}
		catch (Exception e) {
			throw new LazyLoadingQueryException("Error while creating or executing the LazyLoading Count Query (This can be a server error)", e);
		}
	}

	/**
	 * Return the name of the entity managed by this repo
	 * 
	 * @return
	 */
	public String getEntityName() {
		return getQBean().getType().getSimpleName();
	}

	/**
	 * Construct a query using the LazyLoadingConfiguration given in parameter. The
	 * query return can then be used to perform a select / count / delete...
	 * 
	 * @param config The configuration for this query
	 * @return The constructed query
	 */
	protected JPAQuery<E> allLazyLoadingQuery(LazyLoadingConfiguration config) {
		log.debug("Entering allLazyLoadingQuery({})", config);

		MutableInt nbAliases = new MutableInt();
		JPAQuery<E> query = new JPAQuery<E>(entityManager());
		query.from(getQBean());
		query.distinct();
		if (!config.isFetchDeleted()) {
			query.where(pathBuilder.getBoolean(QGenericEntity.genericEntity.deleted.getMetadata().getName()).eq(false));
		}

		// FILTER
		for (LazyLoadingFilter filter : config.getFilters()) {
			handleFilter(filter, query, config.getLocale(), pathBuilder, nbAliases);
		}
		// ORDER
		for (LazyLoadingOrder order : config.getOrders()) {
			handleOrder(order, query, config.getLocale(), pathBuilder, nbAliases);
		}

		if (log.isDebugEnabled()) {
			log.debug("Leaving allLazyLoadingQuery() -> {}", query.toString().replaceAll("\n", " "));
		}
		return query;
	}

	/**
	 * Handle filters in query construction ( @see
	 * {@link #allLazyLoadingQuery(LazyLoadingConfiguration)} ). <br />
	 * Handle default behavior (see LazyLoading page in project wiki for more info)
	 * for every entities. <br />
	 * Can be overridden in a specific repository to handle custom filters. If a
	 * custom filter is given in parameter, the filter.getValue() from parameter
	 * will then always be a String.
	 * 
	 * @param filter      The filter to handle
	 * @param query       The query that is being constructed
	 * @param locale      The locale that should be used
	 * @param pathBuilder Path builder used in the GenericRepository to construct
	 *                    the query (in specific repository, use directly the
	 *                    QEntity reference)
	 * @param nbAliases   Counter to create aliases when joining entities in the
	 *                    query ( @see {@link #alias(MutableInt)} )
	 * @return The query passed in parameter updated with new conditions to handle
	 *         the filter.
	 */
	protected JPAQuery<E> handleFilter(LazyLoadingFilter filter, JPAQuery<E> query, Locale locale,
			PathBuilder<Object> pathBuilder, MutableInt nbAliases) {

		String[] keySplit = filter.getKey().split("\\.");
		int entityIndex = 0;
		PathBuilder<Object> entityPath = pathBuilder;
		String localizedAttributeName = null;
		while (entityIndex < keySplit.length - 1) {
			if (filter.getKey().endsWith(FIELD_LOCALIZED_TEXT) && entityIndex == keySplit.length - 2) {
				localizedAttributeName = keySplit[entityIndex];
				break;
			}
			String table = keySplit[entityIndex];
			PathBuilder<Object> tablePath = entityPath.get(table);
			entityPath = new PathBuilder<>(tablePath.getType(), alias(nbAliases));
			query.leftJoin(tablePath, entityPath);
			entityIndex++;
		}
		if (filter.getKey().endsWith(FIELD_LOCALIZED_TEXT)) {
			StringTemplate jsonValue = buildJsonLocalizedExpression(entityPath.getString(localizedAttributeName),
					locale);
			return query.where(jsonValue.contains((String) filter.getValue()));
		}
		return query.where(configToDSL.getFilterPredicate(filter, entityPath));
	}

	/**
	 * Handle orders in query construction ( @see
	 * {@link #allLazyLoadingQuery(LazyLoadingConfiguration)} ). <br />
	 * Handle default behavior (see LazyLoading page in project wiki for more info)
	 * for every entities. <br />
	 * Can be overridden in a specific repository to handle custom orders.
	 * 
	 * @param order       The order to handle
	 * @param query       The query that is being constructed
	 * @param locale      The locale that should be used
	 * @param pathBuilder Path builder used in the GenericRepository to construct
	 *                    the query (in specific repository, use directly the
	 *                    QEntity reference)
	 * @param nbAliases   Counter to create aliases when joining entities in the
	 *                    query ( @see {@link #alias(MutableInt)} )
	 * @return The query passed in parameter updated with new order by to handle the
	 *         order.
	 */
	protected JPAQuery<E> handleOrder(LazyLoadingOrder order, JPAQuery<E> query, Locale locale,
			PathBuilder<Object> pathBuilder, MutableInt nbAliases) {

		String[] keySplit = order.getKey().split("\\.");
		int entityIndex = 0;
		PathBuilder<Object> entityPath = pathBuilder;
		String localizedAttributeName = null;
		while (entityIndex < keySplit.length - 1) {
			if (order.getKey().endsWith(FIELD_LOCALIZED_TEXT) && entityIndex == keySplit.length - 2) {
				localizedAttributeName = keySplit[entityIndex];
				entityIndex++;
				break;
			}
			String table = keySplit[entityIndex];
			PathBuilder<Object> tablePath = entityPath.get(table);
			entityPath = new PathBuilder<>(tablePath.getType(), alias(nbAliases));
			query.leftJoin(tablePath, entityPath);
			entityIndex++;
		}
		if (order.getKey().endsWith(FIELD_LOCALIZED_TEXT)) {
			StringTemplate jsonValue = buildJsonLocalizedExpression(entityPath.getString(localizedAttributeName),
					locale);
			switch (order.getOperator()) {
			case ASCENDANT:
				return query.orderBy(jsonValue.asc());
			case DESCENDANT:
				return query.orderBy(jsonValue.desc());
			}
		}
		return query.orderBy(configToDSL.getOrderSpecifier(order, entityPath));

	}

	/**
	 * Process the query and config to create a {@link LazyLoadingResult}
	 * 
	 * @param config The config of the LazyLoading query
	 * @param query  The query
	 * @return The {@link LazyLoadingResult} for this query and config
	 */
	protected <T> LazyLoadingResult<T> createLazyLoadingResult(LazyLoadingConfiguration config, JPAQuery<T> query) {

		List<T> entities = null;

		long totalCount = query.fetchCount();

		// Offset / Limit
		query.offset(config.getOffset());
		if (config.getLimit() > 0) {
			query.limit(config.getLimit());
		}

		// Query
		LazyLoadingResult<T> result = new LazyLoadingResult<>();
		if (!config.isHeaderOnly()) {
			entities = query.fetch();
			result.setResult(entities);
		}
		else {
			result.setResult(new ArrayList<>());
		}

		// Pagination info
		long nbPages = countNbPages(totalCount, config.getLimit());
		result.setPaginationInfo(new LazyLoadingPaginationInfo(config.getLimit(), totalCount, nbPages));

		return result;

	}

	/**
	 * Count the number of page (used to set {@link LazyLoadingPaginationInfo})
	 * 
	 * @param total The total number of entity returned by the query (without offset
	 *              and limit)
	 * @param limit The limit set in the query
	 * @return The number of page we can display with this amount of entities and
	 *         this limit.
	 */
	protected long countNbPages(long total, long limit) {
		if (limit == 0) {
			return 1l;
		}

		long modRes = total % limit;

		total = total - modRes;

		long pageCount = total / limit;

		if (modRes != 0) {
			pageCount++;
		}

		return pageCount;
	}

	/**
	 * Build a StringTemplate that lead to the value of a {@link LocalizedString}
	 * for the locale given in parameter
	 * 
	 * @param attributePath The path to the attribute of type
	 *                      {@link LocalizedString} (containing the JSON string)
	 * @param locale        The locale of the text to get
	 * @return The StringTemplate that can be used in QueryDSL queries.
	 */
	protected StringTemplate buildJsonLocalizedExpression(StringPath attributePath, Locale locale) {
		return Expressions.stringTemplate("function('json_value', {0}, '$." + locale.getLanguage() + "')", attributePath);
	}

	/**
	 * Generate a new alias (alias1, alias2...) with the given parameter and
	 * increment nbAliases
	 * 
	 * @param nbAliases number of alias already generated in the query we want alias
	 *                  for.
	 * @return the alias string
	 */
	protected String alias(MutableInt nbAliases) {
		return "alias" + nbAliases.addAndGet(1);
	}

	/**
	 * @return The {@link QBean} of the entity managed by this repository.
	 */
	protected EntityPathBase<E> getQBean() {
		throw new UnsupportedOperationException(
				this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().indexOf('$'))
						+ " must override getQBean()");
	}
}
