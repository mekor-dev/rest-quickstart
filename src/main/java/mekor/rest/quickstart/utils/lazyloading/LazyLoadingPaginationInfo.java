/**
 * 
 */
package mekor.rest.quickstart.utils.lazyloading;

/**
 * Contains information about pagination when performing a LazyLoading query. An
 * instance of this object is joined in the {@link LazyLoadingResult} with the
 * entities list returned by the query. <br />
 * Contains the limit, the total count and the number of pages.
 * 
 * @author mekor
 *
 */
public class LazyLoadingPaginationInfo {

	/**
	 * The limit set in the query
	 */
	private long limit;

	/**
	 * The total count of entities found by the query (without limit and offset)
	 */
	private long totalCount;

	/**
	 * The number of pages that can be displayed with the query {@link #limit}
	 */
	private long nbPages;

	public LazyLoadingPaginationInfo(long limit, long totalCount, long nbPages) {
		super();
		this.limit = limit;
		this.totalCount = totalCount;
		this.nbPages = nbPages;
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
	 * @see {@link #totalCount}
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * @see {@link #totalCount}
	 */
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @see {@link #nbPages}
	 */
	public long getNbPages() {
		return nbPages;
	}

	/**
	 * @see {@link #nbPages}
	 */
	public void setNbPages(long nbPages) {
		this.nbPages = nbPages;
	}

}
