/**
 * 
 */
package mekor.rest.quickstart.utils.lazyloading;

import mekor.rest.quickstart.utils.gson.GsonUtils;

/**
 * A filter for performing LazyLoading queries.<br />
 * Contains a key on which to perform the filter, the
 * {@link LazyLoadingFilterOperator} to perform and the values of the filter
 * 
 * @author mekor
 *
 */
public class LazyLoadingFilter {

	/**
	 * The key oon which to perform the filter
	 */
	private String key;

	/**
	 * The operator to apply
	 */
	private LazyLoadingFilterOperator operator;

	/**
	 * The values of the filter
	 */
	private Object[] values;

	public LazyLoadingFilter(String key, LazyLoadingFilterOperator operator, Object[] values) {
		super();
		this.key = key;
		this.operator = operator;
		this.values = values;
	}

	@Override
	public String toString() {
		return GsonUtils.toJson(this);
	}

	/**
	 * @see {@link #key}
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @see {@link #key}
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @see {@link #operator}
	 */
	public LazyLoadingFilterOperator getOperator() {
		return operator;
	}

	/**
	 * @see {@link #operator}
	 */
	public void setOperator(LazyLoadingFilterOperator operator) {
		this.operator = operator;
	}

	/**
	 * @see {@link #values}
	 */
	public Object[] getValues() {
		return values;
	}

	/**
	 * @see {@link #values}
	 */
	public void setValues(Object[] values) {
		this.values = values;
	}

	/**
	 * 
	 * @return the first entry of the {@link #values} tab. Useful if we know that
	 *         the tab has only one entry
	 */
	public Object getValue() {
		return values[0];
	}

}
