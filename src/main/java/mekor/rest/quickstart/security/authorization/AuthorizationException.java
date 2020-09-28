/**
 * 
 */
package mekor.rest.quickstart.security.authorization;

/**
 * Exception thrown by {@link AuthorizationService} if an authorization check
 * has failed. <br />
 * It is then handled by the {@link AuthorizationExceptionHandler} to return the
 * correct response (403).
 * 
 * @author mekor
 *
 */
public class AuthorizationException extends RuntimeException {

	private static final long serialVersionUID = -9085007881102821249L;

	/**
	 * Status to return in the HTTP response.
	 */
	private Integer status;

	public AuthorizationException(int status, String message) {
		super(message);
		this.status = status;
	}

	/**
	 * @see {@link #status}
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @see {@link #status}
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

}
