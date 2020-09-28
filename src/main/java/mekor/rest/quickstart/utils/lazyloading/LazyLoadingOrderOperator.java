/**
 * 
 */
package mekor.rest.quickstart.utils.lazyloading;

/**
 * An operator for orders in LazyLoading queries.
 * 
 * For more info, see the LazyLoading page of the application wiki
 * 
 * @author mekor
 *
 */
public enum LazyLoadingOrderOperator {
	ASCENDANT("asc"),
	DESCENDANT("desc");

	/**
	 * The operator has it must be in the HTTP query parameter.
	 */
	private String operatorInQuery;

	private LazyLoadingOrderOperator(String operatorInQuery) {
		this.operatorInQuery = operatorInQuery;
	}

	/**
	 * Return an operator corresponding to the operatorInQuery given in parameter
	 * 
	 * @param operatorInQuery The operator parsed in the HTTP query parameter
	 * @return The {@link LazyLoadingOrderOperator} corresponding to the
	 *         operatorInQuery
	 */
	public static LazyLoadingOrderOperator getByQuery(String operatorInQuery) {
		for (LazyLoadingOrderOperator operator : LazyLoadingOrderOperator.values()) {
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
