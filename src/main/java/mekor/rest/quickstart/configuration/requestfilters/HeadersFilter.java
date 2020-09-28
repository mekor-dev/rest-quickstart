/**
 * 
 */
package mekor.rest.quickstart.configuration.requestfilters;

import java.io.IOException;
import java.util.StringJoiner;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import com.google.common.net.HttpHeaders;

import mekor.rest.quickstart.api.utils.APIUtils;
import mekor.rest.quickstart.configuration.AppConfig;
import mekor.rest.quickstart.security.authentication.AccessTokenRefreshFilter;

/**
 * Adds AllowHeaders and ExposeHeaders in the response. <br />
 * Adds CORS headers in the response. <br />
 * Allowed domains are defined in the {@link AppConfig}
 * 
 * @author mekor
 *
 */

@Provider
public class HeadersFilter implements ContainerResponseFilter {

	private static final String ORIGIN_HEADER_NAME = "Origin";

	public static final String EXPOSE_HEADER = new StringJoiner(", ")
			.add(APIUtils.X_PAGE_COUNT)
			.add(APIUtils.X_PAGE_LIMIT)
			.add(APIUtils.X_TOTAL_COUNT)
			.add(AccessTokenRefreshFilter.ACCESS_TOKEN_HEADER)
			.toString();

	public static final String ALLOW_HEADERS = new StringJoiner(", ")
			.add(HttpHeaders.CONTENT_TYPE)
			.add(HttpHeaders.AUTHORIZATION)
			.add(HttpHeaders.ACCEPT)
			.add(HttpHeaders.ACCEPT_LANGUAGE)
			.toString();

	public static final String ALLOW_METHODS = "GET, POST, PUT, PATCH, DELETE, OPTIONS";

	@Inject
	private AppConfig appConfig;

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

		for (String domain : appConfig.getCorsDomains()) {
			String originDomain = requestContext.getHeaderString(ORIGIN_HEADER_NAME);
			if (domain.equals("*") || domain.equals(originDomain)) {
				responseContext.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, domain);
				responseContext.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOW_METHODS);
				responseContext.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, ALLOW_HEADERS);
				responseContext.getHeaders().add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, EXPOSE_HEADER);
				break;
			}
		}
	}

}