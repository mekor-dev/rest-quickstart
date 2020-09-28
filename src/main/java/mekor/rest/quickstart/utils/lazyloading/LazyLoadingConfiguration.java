/**
 * 
 */
package mekor.rest.quickstart.utils.lazyloading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.MultivaluedMap;

import mekor.rest.quickstart.exceptions.LazyLoadingQueryException;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;
import mekor.rest.quickstart.utils.gson.GsonUtils;

/**
 * Configuration for performing a LazyLoading query ( @see
 * {@link GenericRepostiory} ). <br />
 * This configuration contains offset, limit, locale, a list of filters and a
 * list of orders to apply when constructing the query. <br />
 * This configuration can be created from a HTTP request headers with
 * {@link #LazyLoadingConfiguration(MultivaluedMap)}
 * 
 * @author mekor
 *
 */
public class LazyLoadingConfiguration {

	/**
	 * Limit to set if no limit is set. The only way to find all entities is to set
	 * the limit to 0.
	 */
	public static final long DEFAULT_LIMIT = 10L;

	/**
	 * The key of the headerOnly query parameter (in URI). Used to fetch no data and
	 * only count data.
	 */
	public static final String PARAM_HEADER_ONLY = "headerOnly";

	/**
	 * The key of the select query parameter (in URI) (used in specific API, not
	 * here).
	 */
	public static final String PARAM_SELECT = "select";

	/**
	 * The key of the deleted query parameter (in URI)
	 */
	public static final String PARAM_FETCH_DELETED = "deleted";

	/**
	 * The key of the offset query parameter (in URI)
	 */
	public static final String PARAM_OFFSET = "offset";

	/**
	 * The key of the limit query parameter (in URI)
	 */
	public static final String PARAM_LIMIT = "limit";

	/**
	 * The key of the filter query parameter (in URI)
	 */
	public static final String PARAM_FILTER = "filter";

	/**
	 * The key of the order query parameter (in URI)
	 */
	public static final String PARAM_ORDER = "order";

	/**
	 * The separator between the key and operator (for filter)
	 */
	public static final String SEP_KEY_OPERATOR = "=";

	/**
	 * The separator between the operator and the value (for filter)
	 */
	public static final String SEP_OPERATOR_VALUE = ":";

	/**
	 * The separator between each values in a filter with multiple values
	 */
	public static final String SEP_VALUES = ",";

	/**
	 * The separator between each filter
	 */
	public static final String SEP_FILTERS = "!";

	/**
	 * The separator between each order
	 */
	public static final String SEP_ORDERS = "!";

	/**
	 * The separator between the key and the order operator
	 */
	public static final String SEP_KEY_ORDER = ":";

	/*
	 * List of filters that must be applied during the construction of the query
	 */
	private List<LazyLoadingFilter> filters = new ArrayList<>();

	/*
	 * List of orders that must be applied during the construction of the query
	 */
	private List<LazyLoadingOrder> orders = new ArrayList<>();

	/**
	 * True if we want to count entities only (no data in response).
	 */
	private boolean headerOnly = false;

	/**
	 * True if we want to fetch that have deleted flag to true
	 */
	private boolean fetchDeleted = false;

	/**
	 * The offset that must be applied during the construction of the query
	 */
	private long offset = 0;

	/**
	 * The limit that must be applied during the construction of the query
	 */
	private long limit = DEFAULT_LIMIT;

	/**
	 * The locale that is used during the construction of the query
	 */
	private Locale locale;

	/**
	 * Create an empty configuration
	 * 
	 * @param locale       locale to be used during the construction of the query
	 * @param fetchDeleted true if we want to fetch deleted elements by default.
	 */
	public LazyLoadingConfiguration(Locale locale, boolean fetchDeleted) {
		this.locale = locale;
		this.fetchDeleted = fetchDeleted;
	}

	/**
	 * Create a configuration from HTTP query parameters sent by the client.
	 * <p>
	 * <strong>exemple of query parameters string: </strong><br />
	 * ?offset=10&limit=15&locale=fr&filter=firstname=eq:paul!age=gte:18&order=lastname:asc
	 * </p>
	 * For more info, see the LazyLoading page of the project wiki.
	 * 
	 * @param queryParams  HTTP query parameters
	 * @param locale       locale to be used during the construction of the query
	 * @param fetchDeleted true if we want to fetch deleted elements by default.
	 * @param isAdmin      true if the user making the request is an admin.
	 * @throws LazyLoadingQueryException thrown if the query parameters are invalid.
	 */
	public LazyLoadingConfiguration(MultivaluedMap<String, String> queryParams, Locale locale, boolean fetchDeleted, boolean isAdmin) throws LazyLoadingQueryException {
		this(locale, fetchDeleted);
		parseOffset(queryParams.getFirst(PARAM_OFFSET));
		parseLimit(queryParams.getFirst(PARAM_LIMIT));
		parseFilterQuery(queryParams.getFirst(PARAM_FILTER));
		parseOrderQuery(queryParams.getFirst(PARAM_ORDER));
		parseHeaderOnly(queryParams.getFirst(PARAM_HEADER_ONLY));
		if (isAdmin) {
			parseDeleted(queryParams.getFirst(PARAM_FETCH_DELETED));
		}
	}

	/**
	 * Add a filter to the configuration
	 * 
	 * @param Tey      the key on which to apply the filter
	 * @param operator The operator to apply
	 * @param values   The value(s) of the filter
	 */
	public void addFilter(String key, LazyLoadingFilterOperator operator, String... values) {
		filters.add(new LazyLoadingFilter(key, operator, values));
	}

	/**
	 * Add a filter to the configuration
	 * 
	 * @param Tey      the key on which to apply the filter
	 * @param operator The operator to apply
	 * @param values   The value(s) of the filter
	 */
	public void addFilter(String key, LazyLoadingFilterOperator operator, Double... values) {
		filters.add(new LazyLoadingFilter(key, operator, values));
	}

	/**
	 * Add a filter to the configuration
	 * 
	 * @param Tey      the key on which to apply the filter
	 * @param operator The operator to apply
	 * @param values   The value(s) of the filter
	 */
	public void addFilter(String key, LazyLoadingFilterOperator operator, Long... values) {
		filters.add(new LazyLoadingFilter(key, operator, values));
	}

	/**
	 * Add a filter to the configuration
	 * 
	 * @param Tey      the key on which to apply the filter
	 * @param operator The operator to apply
	 * @param values   The value(s) of the filter
	 */
	public void addFilter(String key, LazyLoadingFilterOperator operator, Boolean... values) {
		filters.add(new LazyLoadingFilter(key, operator, values));
	}

	/**
	 * Add a filter to the configuration
	 * 
	 * @param Tey      the key on which to apply the filter
	 * @param operator The operator to apply
	 * @param values   The value(s) of the filter
	 */
	public void addFilter(String key, LazyLoadingFilterOperator operator, Date... values) {
		filters.add(new LazyLoadingFilter(key, operator, values));
	}

	/**
	 * Add a IS_NULL filter to the configuration
	 * 
	 * @param key the key on which to apply the filter
	 */
	public void isNull(String key) {
		filters.add(new LazyLoadingFilter(key, LazyLoadingFilterOperator.NULL, null));
	}

	/**
	 * Add a IS_NOT_NULL filter to the configuration
	 * 
	 * @param key the key on which to apply the filter
	 */
	public void isNotNull(String key) {
		filters.add(new LazyLoadingFilter(key, LazyLoadingFilterOperator.NOT_NULL, null));
	}

	/**
	 * Add a custom filter the the configuration
	 * 
	 * @param key   The custom key on which to apply the filter (stored as static
	 *              variable in the repository)
	 * @param value The value of the filter
	 */
	public void addFilterCustom(String key, String value) {
		filters.add(new LazyLoadingFilter(key, null, new String[] { value }));
	}

	/**
	 * Add an order to the configuration
	 * 
	 * @param key      The key on which to apply the order
	 * @param operator ASC or DESC
	 */
	public void addOrder(String key, LazyLoadingOrderOperator operator) {
		orders.add(new LazyLoadingOrder(key, operator));
	}

	/**
	 * Add an order if there is no order already set
	 * 
	 * @param key      The key on which to apply the order
	 * @param operator ASC or DESC
	 */
	public void addOrderIfNone(String key, LazyLoadingOrderOperator operator) {
		if (orders.isEmpty()) {
			addOrder(key, operator);
		}
	}

	@Override
	public String toString() {
		return GsonUtils.toJson(this);
	}

	////////////////////////////////////////////////////////////////////
	///////////////////////////// PARSERS //////////////////////////////
	////////////////////////////////////////////////////////////////////

	private void parseHeaderOnly(String headerOnlyQuery) throws LazyLoadingQueryException {
		if (headerOnlyQuery != null) {
			try {
				this.headerOnly = Boolean.parseBoolean(headerOnlyQuery);
			}
			catch (NumberFormatException e) {
				throw new LazyLoadingQueryException("deleted parameter is invalid");
			}
		}
	}

	private void parseDeleted(String deletedQuery) throws LazyLoadingQueryException {
		if (deletedQuery != null) {
			try {
				this.fetchDeleted = Boolean.parseBoolean(deletedQuery);
			}
			catch (NumberFormatException e) {
				throw new LazyLoadingQueryException("deleted parameter is invalid");
			}
		}
	}

	private void parseOffset(String offsetQuery) throws LazyLoadingQueryException {
		if (offsetQuery != null) {
			try {
				this.offset = Long.parseLong(offsetQuery);
			}
			catch (NumberFormatException e) {
				throw new LazyLoadingQueryException("Limit parameter is invalid");
			}
		}
	}

	private void parseLimit(String limitQuery) throws LazyLoadingQueryException {
		if (limitQuery != null) {
			try {
				this.limit = Long.parseLong(limitQuery);
			}
			catch (NumberFormatException e) {
				throw new LazyLoadingQueryException("Limit parameter is invalid");
			}
		}
	}

	private void parseFilterQuery(String filterQuery) throws LazyLoadingQueryException {
		if (filterQuery != null) {
			for (String uniqueFilter : filterQuery.split(SEP_FILTERS)) {

				String[] keyOpSplit = uniqueFilter.split(SEP_KEY_OPERATOR);
				if (keyOpSplit.length != 2) {
					throw new LazyLoadingQueryException("Filter [" + uniqueFilter + "] must contain a key-operator separator (" + SEP_KEY_OPERATOR + ")");
				}

				String key = keyOpSplit[0];

				String[] opValueSplit = keyOpSplit[1].split(SEP_OPERATOR_VALUE);
				if (opValueSplit.length > 2) {
					throw new LazyLoadingQueryException("Filter [" + uniqueFilter + "] cannot have more than one operator-value separator (" + SEP_OPERATOR_VALUE + ")");
				}

				LazyLoadingFilterOperator operator = null;
				List<String> values = new ArrayList<>();

				if (opValueSplit.length == 1) {
					// NULL NOT_NULL
					operator = LazyLoadingFilterOperator.getByQuery(opValueSplit[0]);
					if (operator == LazyLoadingFilterOperator.NULL || operator == LazyLoadingFilterOperator.NOT_NULL) {
						filters.add(new LazyLoadingFilter(key, operator, null));
						continue;
					}
					// No operator (custom filter).
					else {
						if (operator != null) {
							throw new LazyLoadingQueryException("Filter [" + uniqueFilter + "] has no value");
						}
						filters.add(new LazyLoadingFilter(key, null, new String[] { opValueSplit[0] }));
						continue;
					}
				}
				// Normal query
				else {
					operator = LazyLoadingFilterOperator.getByQuery(opValueSplit[0]);
					if (operator == null) {
						throw new LazyLoadingQueryException("Filter [" + uniqueFilter + "] has invalid operator");
					}

					if (opValueSplit[1].contains(SEP_VALUES)) {
						String[] valuesSplit = opValueSplit[1].split(SEP_VALUES);
						values.addAll(Arrays.asList(valuesSplit));
					}
					else {
						values.add(opValueSplit[1]);
					}

				}

				List<Object> paramValues = new ArrayList<>();

				// LocalizedText (String only)
				if (key.endsWith(GenericRepostiory.FIELD_LOCALIZED_TEXT)) {
					filters.add(new LazyLoadingFilter(key, operator, values.toArray(new String[paramValues.size()])));
					continue;
				}

				// Numeric value
				try {
					for (String value : values) {
						paramValues.add(Long.parseLong(value));
					}
					filters.add(new LazyLoadingFilter(key, operator, paramValues.toArray(new Long[paramValues.size()])));
					continue;
				}
				catch (NumberFormatException e1) {
				}

				try {
					for (String value : values) {
						paramValues.add(Double.parseDouble(value));
					}
					filters.add(new LazyLoadingFilter(key, operator, paramValues.toArray(new Double[paramValues.size()])));
					continue;
				}
				catch (NumberFormatException e1) {
				}

				// Boolean, Date
				if (values.get(0).equals("true") || values.get(0).equals("false")) {
					for (String value : values) {
						paramValues.add(Boolean.parseBoolean(value));
					}
					filters.add(new LazyLoadingFilter(key, operator, paramValues.toArray(new Boolean[paramValues.size()])));
					continue;
				}
				else if (values.get(0).startsWith("date(") && values.get(0).endsWith(")")) {
					for (String value : values) {
						Long timestamp = Long.parseLong(value.substring(5, value.length() - 1));
						paramValues.add(new Date(timestamp));
					}

					filters.add(new LazyLoadingFilter(key, operator, paramValues.toArray(new Date[paramValues.size()])));
					continue;
				}
				// String
				else {
					filters.add(new LazyLoadingFilter(key, operator, values.toArray(new String[values.size()])));
					continue;
				}
			}
		}
	}

	private void parseOrderQuery(String orderQuery) throws LazyLoadingQueryException {
		if (orderQuery != null) {
			for (String uniqueOrder : orderQuery.split(SEP_ORDERS)) {
				String[] keyOrderSplit = uniqueOrder.split(SEP_KEY_ORDER);
				if (keyOrderSplit.length != 2) {
					throw new LazyLoadingQueryException("Order [" + uniqueOrder + "] must have a key-order separator (" + SEP_KEY_ORDER + ")");
				}

				String key = keyOrderSplit[0];

				LazyLoadingOrderOperator operator = LazyLoadingOrderOperator.getByQuery(keyOrderSplit[1]);
				if (operator == null) {
					throw new LazyLoadingQueryException("Order [" + uniqueOrder + "] has invalid operator");
				}

				orders.add(new LazyLoadingOrder(key, operator));
			}
		}
	}

	////////////////////////////////////////////////////////////////////
	///////////////////////////// GET/SET //////////////////////////////
	////////////////////////////////////////////////////////////////////

	/**
	 * @see {@link #filters}
	 */
	public List<LazyLoadingFilter> getFilters() {
		return filters;
	}

	/**
	 * @see {@link #filters}
	 */
	public void setFilters(List<LazyLoadingFilter> filters) {
		this.filters = filters;
	}

	/**
	 * @see {@link #orders}
	 */
	public List<LazyLoadingOrder> getOrders() {
		return orders;
	}

	/**
	 * @see {@link #orders}
	 */
	public void setOrders(List<LazyLoadingOrder> orders) {
		this.orders = orders;
	}

	/**
	 * @see {@link #offset}
	 */
	public long getOffset() {
		return offset;
	}

	/**
	 * @see {@link #offset}
	 */
	public void setOffset(long offset) {
		this.offset = offset;
	}

	/**
	 * @see {@link #limit}
	 */
	public long getLimit() {
		return limit;
	}

	/**
	 * @see {@link #limit}
	 */
	public void setLimit(long limit) {
		this.limit = limit;
	}

	/**
	 * @see {@link #locale}
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @see {@link #locale}
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @see {@link #fetchDeleted}
	 */
	public boolean isFetchDeleted() {
		return fetchDeleted;
	}

	/**
	 * @see {@link #fetchDeleted}
	 */
	public void setFetchDeleted(boolean fetchDeleted) {
		this.fetchDeleted = fetchDeleted;
	}

	/**
	 * @see {@link #headerOnly}
	 */
	public boolean isHeaderOnly() {
		return headerOnly;
	}

	/**
	 * @see {@link #headerOnly}
	 */
	public void setHeaderOnly(boolean headerOnly) {
		this.headerOnly = headerOnly;
	}

}
