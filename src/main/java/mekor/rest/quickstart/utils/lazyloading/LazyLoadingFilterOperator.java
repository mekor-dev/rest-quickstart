/**
 * 
 */
package mekor.rest.quickstart.utils.lazyloading;

/**
 * An operator for filters in LazyLoading queries. Those are default supported
 * filters that don't have to be implemented as custom filters in the
 * repositories.
 * 
 * For more info, see the LazyLoading page of the application wiki
 * 
 * @author mekor
 *
 */
public enum LazyLoadingFilterOperator {
	EQUALS("eq"),
	NOT_EQUALS("neq"),
	CONTAINS("cts"),
	LOWER_THAN("lt"),
	NULL_OR_LOWER_THAN("nullOrLt"),
	LOWER_THAN_EQUALS("lte"),
	NULL_OR_LOWER_THAN_EQUALS("nullOrLte"),
	GREATER_THAN("gt"),
	NULL_OR_GREATER_THAN("nullOrGt"),
	GREATER_THAN_EQUALS("gte"),
	NULL_OR_GREATER_THAN_EQUALS("nullOrGte"),
	IN("in"),
	NOT_IN("nin"),
	BETWEEN("btw"),
	NOT_BETWEEN("nbtw"),
	NULL("null"),
	NOT_NULL("notNull");

	/**
	 * The operator has it must be in the HTTP query parameter.
	 */
	private String operatorInQuery;

	private LazyLoadingFilterOperator(String operatorInQuery) {
		this.operatorInQuery = operatorInQuery;
	}

	/**
	 * Return an operator corresponding to the operatorInQuery given in parameter
	 * 
	 * @param operatorInQuery The operator parsed in the HTTP query parameter
	 * @return The {@link LazyLoadingFilterOperator} corresponding to the
	 *         operatorInQuery
	 */
	public static LazyLoadingFilterOperator getByQuery(String operatorInQuery) {
		for (LazyLoadingFilterOperator operator : LazyLoadingFilterOperator.values()) {
			if (operator.getOperatorInQuery().equals(operatorInQuery)) {
				return operator;
			}
		}
		return null;
	}

	/**
	 * @see {@link #operatorInQuery}
	 */
	public String getOperatorInQuery() {
		return operatorInQuery;
	}

	/**
	 * @see {@link #operatorInQuery}
	 */
	public void setOperatorInQuery(String operatorInQuery) {
		this.operatorInQuery = operatorInQuery;
	}

}
