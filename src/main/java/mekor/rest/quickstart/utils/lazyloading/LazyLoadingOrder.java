/**
 * 
 */
package mekor.rest.quickstart.utils.lazyloading;

import mekor.rest.quickstart.utils.gson.GsonUtils;

/**
 * A order for performing LazyLoading queries.<br />
 * Contains a key on which to perform the order and the
 * {@link LazyLoadingOrderOperator} to perform
 * 
 * @author mekor
 *
 */
public class LazyLoadingOrder {

	/**
	 * The key on which to perform the order
	 */
	private String key;

	/**
	 * The operator to apply
	 */
	private LazyLoadingOrderOperator operator;

	public LazyLoadingOrder(String key, LazyLoadingOrderOperator operator) {
		super();
		this.key = key;
		this.operator = operator;
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
	public LazyLoadingOrderOperator getOperator() {
		return operator;
	}

	/**
	 * @see {@link #operator}
	 */
	public void setOperator(LazyLoadingOrderOperator operator) {
		this.operator = operator;
	}

}
