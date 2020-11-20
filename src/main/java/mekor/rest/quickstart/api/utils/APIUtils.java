/**
 * 
 */
package mekor.rest.quickstart.api.utils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Status;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.ValidationException;

import org.slf4j.Logger;

import mekor.rest.quickstart.api.utils.error.APIError;
import mekor.rest.quickstart.api.utils.error.APIErrorException;
import mekor.rest.quickstart.configuration.AppConfig;
import mekor.rest.quickstart.model.dtos.utils.GenericDTO;
import mekor.rest.quickstart.model.entities.utils.GenericEntity;
import mekor.rest.quickstart.model.mappers.utils.GenericMapperToDTO;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingConfiguration;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingResult;

/**
 * Utility methods for API classes (response builders, headers management...)
 * 
 * @author mekor
 *
 */
@ApplicationScoped
public class APIUtils {

	@Resource
	private TransactionSynchronizationRegistry txRegistry;

	@Inject
	private AppConfig appConfig;

	@Inject
	private CurrentRequest currentRequest;

	@Inject
	private Logger log;

	public static final String X_TOTAL_COUNT = "X-Total-Count";
	public static final String X_PAGE_COUNT = "X-Page-Count";
	public static final String X_PAGE_LIMIT = "X-Page-Limit";

	public static final String DOC_PAGE_PREFIX = "resource_";
	public static final String DOC_PAGE_SUFFIX = ".html";

	/**
	 * Build a Response from the LazyLoadingResult given in parameter. <br />
	 * Set the status code, the body and the pagination headers in the response.
	 * 
	 * @param result The result of the LazyLoading query.
	 * @return The built response.
	 */
	public <E extends GenericEntity, DTO extends GenericDTO> ResponseBuilder buildLazyLoadingResponse(LazyLoadingResult<E> result, GenericMapperToDTO<E, DTO> mapper) {
		ResponseBuilder responseBuilder = Response.status(200).entity(mapper.entitiesToDTOs(result.getResult()));

		return responseBuilder
				.header(X_TOTAL_COUNT, result.getPaginationInfo().getTotalCount())
				.header(X_PAGE_LIMIT, result.getPaginationInfo().getLimit())
				.header(X_PAGE_COUNT, result.getPaginationInfo().getNbPages());
	}

	/**
	 * Build a Response from the LazyLoadingResult given in parameter. <br />
	 * Set the status code, the body and the pagination headers in the response.
	 * <br />
	 * If you want to map entities, use
	 * {@link #buildLazyLoadingResponse(LazyLoadingResult, GenericMapperToDTO)}
	 * 
	 * @param result The result of the LazyLoading query.
	 * @return The built response.
	 */
	public <T> ResponseBuilder buildLazyLoadingResponseWithoutMapping(LazyLoadingResult<T> result) {
		ResponseBuilder responseBuilder = Response.status(200).entity(result.getResult());

		return responseBuilder
				.header(X_TOTAL_COUNT, result.getPaginationInfo().getTotalCount())
				.header(X_PAGE_LIMIT, result.getPaginationInfo().getLimit())
				.header(X_PAGE_COUNT, result.getPaginationInfo().getNbPages());
	}

	/**
	 * Build a Response that represents an error. Provides the class documentation
	 * and gives exception information if there is an exception
	 * 
	 * @param statusCode   The status code to return in the Response
	 * @param errorMessage A message to inform the client about the error
	 * @param e            The exception that has been thrown
	 * @param apiClass     The API class in which this error occured
	 * @return The built response.
	 */
	public ResponseBuilder buildError(int statusCode, String errorMessage, Throwable e, Class<?> apiClass) {
		APIError apiError = new APIError();
		apiError.message = errorMessage;

		if (!appConfig.isProduction()) {
			if (e != null) {
				addErrorException(apiError, e);
			}
			if (apiClass != null) {
				apiError.documentation = appConfig.getApiDocsRoot() + "/" + DOC_PAGE_PREFIX + apiClass.getSimpleName() + DOC_PAGE_SUFFIX;
			}
			else {
				apiError.documentation = appConfig.getApiDocsRoot();
			}
		}

		// Rollback transaction
		if (statusCode >= 400) {
			try {
				if (txRegistry.getTransactionStatus() != Status.STATUS_NO_TRANSACTION) {
					txRegistry.setRollbackOnly();
					log.debug("Transaction set as rollbackOnly");
				}
				else {
					log.debug("No transaction to set as rollbackOnly");
				}
			}
			catch (Exception e2) {
				log.warn("Error setting the transaction to rollbackOnly", e2);
			}
		}

		return Response.status(statusCode).type(MediaType.APPLICATION_JSON).entity(apiError);
	}

	/**
	 * Add the exception and its cause (recursively) to the apiError
	 * 
	 * @param apiError the apiError to update
	 * @param e        the exception to add.
	 */
	public void addErrorException(APIError apiError, Throwable e) {
		apiError.exception = new APIErrorException();
		apiError.exception.type = e.getClass().getSimpleName();
		apiError.exception.message = e.getMessage();

		APIErrorException errorException = apiError.exception;

		while (e.getCause() != null) {
			errorException.causedBy = new APIErrorException();
			errorException.type = e.getCause().getClass().getSimpleName();
			errorException.message = e.getCause().getMessage();

			errorException = errorException.causedBy;
			e = e.getCause();
		}
	}

	/**
	 * Return the class that handle the request of the given uriInfo.
	 * 
	 * @param uriInfo The uriInfo of a request
	 * @return The class that handle the request
	 */
	public Class<?> getRessourceClass(UriInfo uriInfo) {
		List<Object> resources = uriInfo.getMatchedResources();
		Class<?> apiClass = null;
		try {
			if (resources != null && !resources.isEmpty()) {
				apiClass = resources.get(0).getClass().getSuperclass();
			}
		}
		catch (Exception ex) {
		}
		return apiClass;
	}

	/**
	 * Allow to apply bean validation and throw the exception given in parameter if
	 * a validation error occurs <br />
	 * WARNING : The exception given in parameter MUST NOT have a cause
	 * 
	 * @param bean The bean to validate
	 * @param e    The exception to throw. The ValidationException will be added to
	 *             its cause
	 * @throws EX
	 */
	public <B extends Object, EX extends Exception> void validateBean(B bean, EX e) throws EX {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<B>> errors = validator.validate(bean, Default.class);

		if (errors.size() > 0) {
			for (ConstraintViolation<B> error : errors) {
				e.initCause(new ValidationException(error.getPropertyPath() + " " + error.getMessage()));
				throw e;
			}
		}
	}

	/**
	 * Check the queryparams to know if the query must fetch deleted entities.
	 * 
	 * @return true if the query must fetch deleted entities
	 */
	public boolean fetchDeleted(UriInfo uriInfo) {
		if (!currentRequest.isAdmin()) {
			return false;
		}
		String param = uriInfo.getQueryParameters().getFirst(LazyLoadingConfiguration.PARAM_FETCH_DELETED);
		if (param != null) {
			try {
				return Boolean.parseBoolean(param);
			}
			catch (Exception e) {
			}
		}
		return false;
	}

	/**
	 * Check if the annotation in parameter is on the class or the method of the
	 * called resource.
	 * 
	 * @param annotationClass Class to find
	 * @return true if the annotation is on the class or the method
	 */
	public boolean isAnnotationPresent(ResourceInfo resourceInfo, Class<? extends Annotation> annotationClass) {
		return resourceInfo.getResourceMethod().isAnnotationPresent(annotationClass) || resourceInfo.getResourceClass().isAnnotationPresent(annotationClass);
	}

	public <T extends Annotation> T getAnnotation(ResourceInfo resourceInfo, Class<T> annotationClass) {
		T res = resourceInfo.getResourceMethod().getAnnotation(annotationClass);
		if (res != null) {
			return res;
		}
		else {
			return resourceInfo.getResourceClass().getAnnotation(annotationClass);
		}
	}
}
