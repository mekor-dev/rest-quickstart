/**
 * 
 */
package mekor.rest.quickstart.utils.lazyloading;

import java.util.List;

import mekor.rest.quickstart.repositories.utils.GenericRepostiory;
import mekor.rest.quickstart.utils.gson.GsonUtils;

/**
 * A result of a LazyLoading query ( @see {@link GenericRepostiory ). Contains
 * the list of entities returned by the result and the
 * {@link LazyLoadingPaginationInfo}.
 * 
 * @author mekor
 *
 */
public class LazyLoadingResult<E> {
	/**
	 * List of entities returned by the query
	 */
	private List<E> result;

	/**
	 * Information for pagination of results.
	 */
	private LazyLoadingPaginationInfo paginationInfo;

	public LazyLoadingResult() {
		super();
	}

	@Override
	public String toString() {
		return GsonUtils.toJson(this);
	}

	/**
	 * @see {@link #result}
	 */
	public List<E> getResult() {
		return result;
	}

	/**
	 * @see {@link #result}
	 */
	public void setResult(List<E> result) {
		this.result = result;
	}

	/**
	 * @see {@link #paginationInfo}
	 */
	public LazyLoadingPaginationInfo getPaginationInfo() {
		return paginationInfo;
	}

	/**
	 * @see {@link #paginationInfo}
	 */
	public void setPaginationInfo(LazyLoadingPaginationInfo paginationInfo) {
		this.paginationInfo = paginationInfo;
	}
}
