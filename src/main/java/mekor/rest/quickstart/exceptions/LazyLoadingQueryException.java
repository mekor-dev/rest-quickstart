/**
 * 
 */
package mekor.rest.quickstart.exceptions;

import mekor.rest.quickstart.utils.lazyloading.LazyLoadingConfiguration;

/**
 * Thrown when the queryParams given by the client are invalid when constructing
 * a LazyLoadingConfiguration with
 * {@link LazyLoadingConfiguration#LazyLoadingConfiguration(javax.ws.rs.core.MultivaluedMap)}
 * 
 * @author mekor
 *
 */
public class LazyLoadingQueryException extends RuntimeException {

	private static final long serialVersionUID = 6290018931803490153L;

	public LazyLoadingQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param message Message of the exception
	 */
	public LazyLoadingQueryException(String message) {
		super(message);
	}

}
