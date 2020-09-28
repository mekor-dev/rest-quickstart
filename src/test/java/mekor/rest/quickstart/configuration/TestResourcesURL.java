package mekor.rest.quickstart.configuration;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.common.collect.ImmutableMap;

import mekor.rest.quickstart.api.AccessTokenAPI;
import mekor.rest.quickstart.api.FileEntityAPI;
import mekor.rest.quickstart.api.ForgottenPasswordAPI;
import mekor.rest.quickstart.api.HelloWorldAPI;
import mekor.rest.quickstart.api.SSEAPI;
import mekor.rest.quickstart.configuration.AppConfig;

/**
 * Holds Root URLs of API resources to be used in client calls.
 * 
 * @author mekor
 *
 */
@ApplicationScoped
public class TestResourcesURL {

	@Inject
	private AppConfig appConfig;

	private Map<Class<?>, String> map = ImmutableMap.<Class<?>, String>builder()
			.put(AccessTokenAPI.class, "/access_tokens")
			.put(ForgottenPasswordAPI.class, "/forgotten_password")
			.put(HelloWorldAPI.class, "/status")
			.put(FileEntityAPI.class, "/files")
			.put(SSEAPI.class, "/sse")
			.build();

	public String get(Class<?> clazz) {
		String relativePath = map.get(clazz);
		if (relativePath == null) {
			throw new RuntimeException("No path specified in TestResourcesUrl for class " + clazz);
		}
		return appConfig.getApiRoot() + map.get(clazz);
	}
}
