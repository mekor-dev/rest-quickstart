/**
 * 
 */
package mekor.rest.quickstart.filters;

import java.util.Date;
import java.util.Locale;
import java.util.StringJoiner;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import mekor.rest.quickstart.configuration.ArquillianTest;
import mekor.rest.quickstart.exceptions.LazyLoadingQueryException;
import mekor.rest.quickstart.exceptions.LocalizedStringInitException;
import mekor.rest.quickstart.model.entities.heroes.Hero;
import mekor.rest.quickstart.model.entities.heroes.QHero;
import mekor.rest.quickstart.model.entities.heroes.QUserHero;
import mekor.rest.quickstart.model.entities.heroes.UserHero;
import mekor.rest.quickstart.model.entities.user.QUser;
import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.repositories.UserRepository;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;
import mekor.rest.quickstart.services.HeroService;
import mekor.rest.quickstart.services.UserHeroService;
import mekor.rest.quickstart.services.UserService;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingConfiguration;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingFilterOperator;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingOrderOperator;
import mekor.rest.quickstart.utils.lazyloading.LazyLoadingResult;

/**
 * @author mekor
 *
 */
@RunWith(Arquillian.class)
public class TestLazyLoading extends ArquillianTest {

	@Inject
	private UserService userService;

	@Inject
	private HeroService heroService;

	@Inject
	private UserHeroService userHeroService;

	@Test
	public void testEmpty() throws LazyLoadingQueryException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		config.addFilter(QUser.user.id.getMetadata().getName(), LazyLoadingFilterOperator.EQUALS, 0L);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(0, result.getResult().size());
	}

	@Test
	public void testFetchDeleted() throws LazyLoadingQueryException {
		// Fetch no deleted
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		config.setLimit(0L);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(26, result.getResult().size());

		// Fetch deleted from code
		params = new MultivaluedHashMap<>();
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, true, false);
		config.setLimit(0L);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(29, result.getResult().size());

		// Fetch deleted from params (not an admin, so no deleted fetched)
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_FETCH_DELETED, "true");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		config.setLimit(0L);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(26, result.getResult().size());

		// Fetch deleted from params
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_FETCH_DELETED, "true");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, true);
		config.setLimit(0L);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(29, result.getResult().size());
	}

	@Test
	public void testLimit() throws LazyLoadingQueryException {
		// Limit
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "5");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(5, result.getResult().size());
		Assert.assertEquals(5, result.getPaginationInfo().getLimit());
		Assert.assertEquals(6, result.getPaginationInfo().getNbPages());
		Assert.assertEquals(26, result.getPaginationInfo().getTotalCount());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(5);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(5, result.getResult().size());
		Assert.assertEquals(5, result.getPaginationInfo().getLimit());
		Assert.assertEquals(6, result.getPaginationInfo().getNbPages());
		Assert.assertEquals(26, result.getPaginationInfo().getTotalCount());

		// No limit
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(26, result.getResult().size());
		Assert.assertEquals(0, result.getPaginationInfo().getLimit());
		Assert.assertEquals(1, result.getPaginationInfo().getNbPages());
		Assert.assertEquals(26, result.getPaginationInfo().getTotalCount());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(26, result.getResult().size());
		Assert.assertEquals(0, result.getPaginationInfo().getLimit());
		Assert.assertEquals(1, result.getPaginationInfo().getNbPages());
		Assert.assertEquals(26, result.getPaginationInfo().getTotalCount());
	}

	@Test
	public void testHeaderOnly() throws LazyLoadingQueryException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_HEADER_ONLY, "true");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(0, result.getResult().size());
		Assert.assertEquals(10, result.getPaginationInfo().getLimit());
		Assert.assertEquals(3, result.getPaginationInfo().getNbPages());
		Assert.assertEquals(26, result.getPaginationInfo().getTotalCount());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setHeaderOnly(true);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(0, result.getResult().size());
		Assert.assertEquals(10, result.getPaginationInfo().getLimit());
		Assert.assertEquals(3, result.getPaginationInfo().getNbPages());
		Assert.assertEquals(26, result.getPaginationInfo().getTotalCount());

	}

	@Test
	public void testOffset() throws LazyLoadingQueryException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_OFFSET, "20");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(6, result.getResult().size());
		Assert.assertEquals("Stan", result.getResult().get(0).getNickname());
		Assert.assertEquals(10, result.getPaginationInfo().getLimit());
		Assert.assertEquals(3, result.getPaginationInfo().getNbPages());
		Assert.assertEquals(26, result.getPaginationInfo().getTotalCount());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setOffset(20);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(6, result.getResult().size());
		Assert.assertEquals("Stan", result.getResult().get(0).getNickname());
		Assert.assertEquals(10, result.getPaginationInfo().getLimit());
		Assert.assertEquals(3, result.getPaginationInfo().getNbPages());
		Assert.assertEquals(26, result.getPaginationInfo().getTotalCount());
	}

	@Test
	public void testOffsetAndLimit() throws LazyLoadingQueryException {

		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_OFFSET, "10");
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "3");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(3, result.getResult().size());
		Assert.assertEquals("Thomas", result.getResult().get(0).getNickname());
		Assert.assertEquals(3, result.getPaginationInfo().getLimit());
		Assert.assertEquals(9, result.getPaginationInfo().getNbPages());
		Assert.assertEquals(26, result.getPaginationInfo().getTotalCount());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setOffset(10);
		config.setLimit(3);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(3, result.getResult().size());
		Assert.assertEquals("Thomas", result.getResult().get(0).getNickname());
		Assert.assertEquals(3, result.getPaginationInfo().getLimit());
		Assert.assertEquals(9, result.getPaginationInfo().getNbPages());
		Assert.assertEquals(26, result.getPaginationInfo().getTotalCount());
	}

	@Test
	public void testOrderAsc() throws LazyLoadingQueryException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_ORDER, "nickname:asc");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals("Alberto", result.getResult().get(0).getNickname());
		Assert.assertEquals("Ann", result.getResult().get(1).getNickname());
		Assert.assertEquals("Ann", result.getResult().get(2).getNickname());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.addOrder(QUser.user.nickname.getMetadata().getName(), LazyLoadingOrderOperator.ASCENDANT);
		result = userService.findLazyLoading(config);

		Assert.assertEquals("Alberto", result.getResult().get(0).getNickname());
		Assert.assertEquals("Ann", result.getResult().get(1).getNickname());
		Assert.assertEquals("Ann", result.getResult().get(2).getNickname());
	}

	@Test
	public void testOrderDesc() throws LazyLoadingQueryException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_ORDER, "nickname:desc");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals("Thomas", result.getResult().get(0).getNickname());
		Assert.assertEquals("Thomas", result.getResult().get(1).getNickname());
		Assert.assertEquals("Stan", result.getResult().get(2).getNickname());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.addOrder(QUser.user.nickname.getMetadata().getName(), LazyLoadingOrderOperator.DESCENDANT);
		result = userService.findLazyLoading(config);

		Assert.assertEquals("Thomas", result.getResult().get(0).getNickname());
		Assert.assertEquals("Thomas", result.getResult().get(1).getNickname());
		Assert.assertEquals("Stan", result.getResult().get(2).getNickname());
	}

	@Test
	public void testOrderMultiple() throws LazyLoadingQueryException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_ORDER, "nickname:asc!email:desc");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals("Alberto", result.getResult().get(0).getNickname());
		Assert.assertEquals("Ann", result.getResult().get(1).getNickname());
		Assert.assertEquals("ann.landers@mail.com", result.getResult().get(1).getEmail());
		Assert.assertEquals("Ann", result.getResult().get(2).getNickname());
		Assert.assertEquals("ann.hannessey@mail.com", result.getResult().get(2).getEmail());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.addOrder(QUser.user.nickname.getMetadata().getName(), LazyLoadingOrderOperator.ASCENDANT);
		config.addOrder(QUser.user.email.getMetadata().getName(), LazyLoadingOrderOperator.DESCENDANT);
		result = userService.findLazyLoading(config);

		Assert.assertEquals("Alberto", result.getResult().get(0).getNickname());
		Assert.assertEquals("Ann", result.getResult().get(1).getNickname());
		Assert.assertEquals("ann.landers@mail.com", result.getResult().get(1).getEmail());
		Assert.assertEquals("Ann", result.getResult().get(2).getNickname());
		Assert.assertEquals("ann.hannessey@mail.com", result.getResult().get(2).getEmail());
	}

	@Test
	public void testFilterSimple() throws LazyLoadingQueryException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "nickname=%en%");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(3, result.getResult().size());

		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "nickname=en");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(0, result.getResult().size());

		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "nickname=henry");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilterCustom(QUser.user.nickname.getMetadata().getName(), "%en%");
		result = userService.findLazyLoading(config);

		Assert.assertEquals(3, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilterCustom(QUser.user.nickname.getMetadata().getName(), "en");
		result = userService.findLazyLoading(config);

		Assert.assertEquals(0, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilterCustom(QUser.user.nickname.getMetadata().getName(), "henry");
		result = userService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
	}

	@Test
	public void testFilterEq() throws LazyLoadingQueryException {
		// String
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "nickname=eq:ann");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(2, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.nickname.getMetadata().getName(), LazyLoadingFilterOperator.EQUALS, "ann");
		result = userService.findLazyLoading(config);

		Assert.assertEquals(2, result.getResult().size());

		// Boolean
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "deleted=eq:false");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, true, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(26, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, true);
		config.setLimit(0);
		config.addFilter(QUser.user.deleted.getMetadata().getName(), LazyLoadingFilterOperator.EQUALS, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(26, result.getResult().size());

		// Date
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "creationDate=eq:date(1548284400000)");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(2, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.creationDate.getMetadata().getName(), LazyLoadingFilterOperator.EQUALS, new Date(1548284400000l));
		result = userService.findLazyLoading(config);

		Assert.assertEquals(2, result.getResult().size());

		// Number
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "id=eq:5");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals(Long.valueOf(5), result.getResult().get(0).getId());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.id.getMetadata().getName(), LazyLoadingFilterOperator.EQUALS, 5l);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals(Long.valueOf(5), result.getResult().get(0).getId());

		// Enums
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "configuration.privacy=eq:PUBLIC");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(21, resultCampaign.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		String privacyKey = new StringJoiner(".")
				.add(QUser.user.configuration.getMetadata().getName())
				.add(QUser.user.configuration.privacy.getMetadata().getName())
				.toString();
		config.addFilter(privacyKey, LazyLoadingFilterOperator.EQUALS, "PUBLIC");
		resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(21, resultCampaign.getResult().size());
	}

	@Test
	public void testFilterNotEq() throws LazyLoadingQueryException {
		// String
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "nickname=neq:ann");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(24, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.nickname.getMetadata().getName(), LazyLoadingFilterOperator.NOT_EQUALS, "ann");
		result = userService.findLazyLoading(config);

		Assert.assertEquals(24, result.getResult().size());

		// Boolean
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "deleted=neq:false");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, true, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(3, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, true);
		config.setLimit(0);
		config.addFilter(QUser.user.deleted.getMetadata().getName(), LazyLoadingFilterOperator.NOT_EQUALS, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(3, result.getResult().size());

		// Date
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "creationDate=neq:date(1548284400000)");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(24, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.creationDate.getMetadata().getName(), LazyLoadingFilterOperator.NOT_EQUALS, new Date(1548284400000l));
		result = userService.findLazyLoading(config);

		Assert.assertEquals(24, result.getResult().size());

		// Number
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "id=neq:5");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(25, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.id.getMetadata().getName(), LazyLoadingFilterOperator.NOT_EQUALS, 5l);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(25, result.getResult().size());

		// Enums
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "configuration.privacy=neq:PUBLIC");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(5, resultCampaign.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		String privacyKey = new StringJoiner(".")
				.add(QUser.user.configuration.getMetadata().getName())
				.add(QUser.user.configuration.privacy.getMetadata().getName())
				.toString();
		config.addFilter(privacyKey, LazyLoadingFilterOperator.NOT_EQUALS, "PUBLIC");
		resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(5, resultCampaign.getResult().size());
	}

	@Test
	public void testFilterContains() throws LazyLoadingQueryException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "nickname=cts:en");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(3, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.nickname.getMetadata().getName(), LazyLoadingFilterOperator.CONTAINS, "en");
		result = userService.findLazyLoading(config);

		Assert.assertEquals(3, result.getResult().size());
	}

	@Test
	public void testFilterLowerThan() throws LazyLoadingQueryException {
		// Date
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "creationDate=lt:date(1548284400000)");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> resultUser = userService.findLazyLoading(config);

		Assert.assertEquals(10, resultUser.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.creationDate.getMetadata().getName(), LazyLoadingFilterOperator.LOWER_THAN, new Date(1548284400000l));
		resultUser = userService.findLazyLoading(config);

		Assert.assertEquals(10, resultUser.getResult().size());

		// Number
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "id=lt:6");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(5, resultCampaign.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.id.getMetadata().getName(), LazyLoadingFilterOperator.LOWER_THAN, 6L);
		resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(5, resultCampaign.getResult().size());
	}

	@Test
	public void testFilterNullOrLowerThan() throws LazyLoadingQueryException {
		// Date
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "creationDate=nullOrLt:date(1548284400000)");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(13, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.creationDate.getMetadata().getName(), LazyLoadingFilterOperator.NULL_OR_LOWER_THAN, new Date(1548284400000l));
		result = userService.findLazyLoading(config);

		Assert.assertEquals(13, result.getResult().size());

		// Number
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "power=nullOrLt:1000");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<UserHero> resultCampaign = userHeroService.findLazyLoading(config);

		Assert.assertEquals(2, resultCampaign.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUserHero.userHero.power.getMetadata().getName(), LazyLoadingFilterOperator.NULL_OR_LOWER_THAN, 1000L);
		resultCampaign = userHeroService.findLazyLoading(config);

		Assert.assertEquals(2, resultCampaign.getResult().size());
	}

	@Test
	public void testFilterLowerThanEq() throws LazyLoadingQueryException {
		// Date
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "creationDate=lte:date(1548284400000)");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(12, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.creationDate.getMetadata().getName(), LazyLoadingFilterOperator.LOWER_THAN_EQUALS, new Date(1548284400000l));
		result = userService.findLazyLoading(config);

		Assert.assertEquals(12, result.getResult().size());

		// Number
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "id=lte:6");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(6, resultCampaign.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.id.getMetadata().getName(), LazyLoadingFilterOperator.LOWER_THAN_EQUALS, 6L);
		resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(6, resultCampaign.getResult().size());
	}

	@Test
	public void testFilterNullOrLowerThanEq() throws LazyLoadingQueryException {
		// Date
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "creationDate=nullOrLte:date(1548284400000)");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(15, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.creationDate.getMetadata().getName(), LazyLoadingFilterOperator.NULL_OR_LOWER_THAN_EQUALS, new Date(1548284400000l));
		result = userService.findLazyLoading(config);

		Assert.assertEquals(15, result.getResult().size());

		// Number
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "power=nullOrLte:1000");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<UserHero> resultCampaign = userHeroService.findLazyLoading(config);

		Assert.assertEquals(3, resultCampaign.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUserHero.userHero.power.getMetadata().getName(), LazyLoadingFilterOperator.NULL_OR_LOWER_THAN_EQUALS, 1000L);
		resultCampaign = userHeroService.findLazyLoading(config);

		Assert.assertEquals(3, resultCampaign.getResult().size());
	}

	@Test
	public void testFilterGreaterThan() throws LazyLoadingQueryException {
		// Date
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "creationDate=gt:date(1548284400000)");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(11, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.creationDate.getMetadata().getName(), LazyLoadingFilterOperator.GREATER_THAN, new Date(1548284400000l));
		result = userService.findLazyLoading(config);

		Assert.assertEquals(11, result.getResult().size());

		// Number
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "id=gt:6");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(20, resultCampaign.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.id.getMetadata().getName(), LazyLoadingFilterOperator.GREATER_THAN, 6L);
		resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(20, resultCampaign.getResult().size());
	}

	@Test
	public void testFilterNullOrGreaterThan() throws LazyLoadingQueryException {
		// Date
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "creationDate=nullOrGt:date(1548284400000)");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(14, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.creationDate.getMetadata().getName(), LazyLoadingFilterOperator.NULL_OR_GREATER_THAN, new Date(1548284400000l));
		result = userService.findLazyLoading(config);

		Assert.assertEquals(14, result.getResult().size());

		// Number
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "power=nullOrGt:1000");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<UserHero> resultCampaign = userHeroService.findLazyLoading(config);

		Assert.assertEquals(3, resultCampaign.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUserHero.userHero.power.getMetadata().getName(), LazyLoadingFilterOperator.NULL_OR_GREATER_THAN, 1000L);
		resultCampaign = userHeroService.findLazyLoading(config);

		Assert.assertEquals(3, resultCampaign.getResult().size());
	}

	@Test
	public void testFilterGreaterThanEq() throws LazyLoadingQueryException {
		// Date
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "creationDate=gte:date(1548284400000)");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(13, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.creationDate.getMetadata().getName(), LazyLoadingFilterOperator.GREATER_THAN_EQUALS, new Date(1548284400000l));
		result = userService.findLazyLoading(config);

		Assert.assertEquals(13, result.getResult().size());

		// Number
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "id=gte:6");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(21, resultCampaign.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.id.getMetadata().getName(), LazyLoadingFilterOperator.GREATER_THAN_EQUALS, 6L);
		resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(21, resultCampaign.getResult().size());
	}

	@Test
	public void testFilterNullOrGreaterThanEq() throws LazyLoadingQueryException {
		// Date
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "creationDate=nullOrGte:date(1548284400000)");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(16, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.creationDate.getMetadata().getName(), LazyLoadingFilterOperator.NULL_OR_GREATER_THAN_EQUALS, new Date(1548284400000l));
		result = userService.findLazyLoading(config);

		Assert.assertEquals(16, result.getResult().size());

		// Number
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "power=nullOrGte:1000");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<UserHero> resultCampaign = userHeroService.findLazyLoading(config);

		Assert.assertEquals(4, resultCampaign.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUserHero.userHero.power.getMetadata().getName(), LazyLoadingFilterOperator.NULL_OR_GREATER_THAN_EQUALS, 1000L);
		resultCampaign = userHeroService.findLazyLoading(config);

		Assert.assertEquals(4, resultCampaign.getResult().size());
	}

	@Test
	public void testFilterBetween() throws LazyLoadingQueryException {
		// Date
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "creationDate=btw:date(1548284400000),date(1566169200000)");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(11, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.creationDate.getMetadata().getName(), LazyLoadingFilterOperator.BETWEEN, new Date(1548284400000l), new Date(1566169200000l));
		result = userService.findLazyLoading(config);

		Assert.assertEquals(11, result.getResult().size());

		// Number
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "id=btw:5,12");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(7, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.id.getMetadata().getName(), LazyLoadingFilterOperator.BETWEEN, 5L, 12L);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(7, result.getResult().size());
	}

	@Test
	public void testFilterNotBetween() throws LazyLoadingQueryException {
		// Date
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "creationDate=nbtw:date(1548284400000),date(1566169200000)");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(15, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.creationDate.getMetadata().getName(), LazyLoadingFilterOperator.NOT_BETWEEN, new Date(1548284400000l), new Date(1566169200000l));
		result = userService.findLazyLoading(config);

		Assert.assertEquals(15, result.getResult().size());

		// Number
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "id=nbtw:5,12");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(19, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.id.getMetadata().getName(), LazyLoadingFilterOperator.NOT_BETWEEN, 5L, 12L);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(19, result.getResult().size());
	}

	@Test
	public void testFilterIn() throws LazyLoadingQueryException {
		// String
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "nickname=in:ann,Thomas");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(4, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.nickname.getMetadata().getName(), LazyLoadingFilterOperator.IN, "ann", "Thomas");
		result = userService.findLazyLoading(config);

		Assert.assertEquals(4, result.getResult().size());

		// Date
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "creationDate=in:date(1548284400000),date(1566165600000)");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(3, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.creationDate.getMetadata().getName(), LazyLoadingFilterOperator.IN, new Date(1548284400000l), new Date(1566165600000l));
		result = userService.findLazyLoading(config);

		Assert.assertEquals(3, result.getResult().size());

		// Number
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "id=in:1,5,10,15");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(4, result.getResult().size());
		Assert.assertEquals(Long.valueOf(1), result.getResult().get(0).getId());
		Assert.assertEquals(Long.valueOf(5), result.getResult().get(1).getId());
		Assert.assertEquals(Long.valueOf(10), result.getResult().get(2).getId());
		Assert.assertEquals(Long.valueOf(15), result.getResult().get(3).getId());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.id.getMetadata().getName(), LazyLoadingFilterOperator.IN, 1l, 5l, 10l, 15l);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(4, result.getResult().size());
		Assert.assertEquals(Long.valueOf(1), result.getResult().get(0).getId());
		Assert.assertEquals(Long.valueOf(5), result.getResult().get(1).getId());
		Assert.assertEquals(Long.valueOf(10), result.getResult().get(2).getId());
		Assert.assertEquals(Long.valueOf(15), result.getResult().get(3).getId());

		// Enums
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "configuration.privacy=in:PUBLIC,ALLIANCE");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(26, resultCampaign.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		String privacyKey = new StringJoiner(".")
				.add(QUser.user.configuration.getMetadata().getName())
				.add(QUser.user.configuration.privacy.getMetadata().getName())
				.toString();
		config.addFilter(privacyKey, LazyLoadingFilterOperator.IN, "PUBLIC", "ALLIANCE");
		resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(26, resultCampaign.getResult().size());
	}

	@Test
	public void testFilterNotIn() throws LazyLoadingQueryException {
		// String
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "nickname=nin:ann,Thomas");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(22, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.nickname.getMetadata().getName(), LazyLoadingFilterOperator.NOT_IN, "ann", "Thomas");
		result = userService.findLazyLoading(config);

		Assert.assertEquals(22, result.getResult().size());

		// Date
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "creationDate=nin:date(1548284400000),date(1566165600000)");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(23, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.creationDate.getMetadata().getName(), LazyLoadingFilterOperator.NOT_IN, new Date(1548284400000l), new Date(1566165600000l));
		result = userService.findLazyLoading(config);

		Assert.assertEquals(23, result.getResult().size());

		// number
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "id=nin:1,5,10,15");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(22, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.id.getMetadata().getName(), LazyLoadingFilterOperator.NOT_IN, 1l, 5l, 10l, 15l);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(22, result.getResult().size());

		// Enums
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "configuration.privacy=nin:PUBLIC");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(5, resultCampaign.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		String privacyKey = new StringJoiner(".")
				.add(QUser.user.configuration.getMetadata().getName())
				.add(QUser.user.configuration.privacy.getMetadata().getName())
				.toString();
		config.addFilter(privacyKey, LazyLoadingFilterOperator.NOT_IN, "PUBLIC");
		resultCampaign = userService.findLazyLoading(config);

		Assert.assertEquals(5, resultCampaign.getResult().size());
	}

	@Test
	public void testFilterIsNull() throws LazyLoadingQueryException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "configuration.defaultLanguage=null");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(4, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		String languageKey = new StringJoiner(".")
				.add(QUser.user.configuration.getMetadata().getName())
				.add(QUser.user.configuration.defaultLanguage.getMetadata().getName())
				.toString();
		config.isNull(languageKey);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(4, result.getResult().size());
	}

	@Test
	public void testFilterIsNotNull() throws LazyLoadingQueryException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "configuration.defaultLanguage=notNull");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(22, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		String languageKey = new StringJoiner(".")
				.add(QUser.user.configuration.getMetadata().getName())
				.add(QUser.user.configuration.defaultLanguage.getMetadata().getName())
				.toString();
		config.isNotNull(languageKey);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(22, result.getResult().size());
	}

	@Test
	public void testFilterMultiple() throws LazyLoadingQueryException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "nickname=ann!creationDate=lt:date(1388534400000)");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QUser.user.nickname.getMetadata().getName(), null, "ann");
		config.addFilter(QUser.user.creationDate.getMetadata().getName(), LazyLoadingFilterOperator.LOWER_THAN, new Date(1388534400000l));
		result = userService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
	}

	@Test
	public void testFilterCustom() throws LazyLoadingQueryException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "customFilterTest=4");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(8, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilterCustom(UserRepository.FILTER_CUSTOM_TEST, "4");
		result = userService.findLazyLoading(config);

		Assert.assertEquals(8, result.getResult().size());
	}

	@Test
	public void testFilterCustomAndGeneric() throws LazyLoadingQueryException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "customFilterTest=4!configuration.defaultLanguage=notNull");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(7, result.getResult().size());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		String languageKey = new StringJoiner(".")
				.add(QUser.user.configuration.getMetadata().getName())
				.add(QUser.user.configuration.defaultLanguage.getMetadata().getName())
				.toString();
		config.addFilterCustom(UserRepository.FILTER_CUSTOM_TEST, "4");
		config.isNotNull(languageKey);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(7, result.getResult().size());
	}

	@Test
	public void testOrderCustom() throws LazyLoadingQueryException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_ORDER, "customOrderTest:asc");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals("Joe", result.getResult().get(0).getNickname());
		Assert.assertEquals("Ann", result.getResult().get(1).getNickname());
		Assert.assertEquals("Ann", result.getResult().get(2).getNickname());
		Assert.assertEquals("Fred", result.getResult().get(3).getNickname());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addOrder(UserRepository.ORDER_CUSTOM_TEST, LazyLoadingOrderOperator.ASCENDANT);
		result = userService.findLazyLoading(config);

		Assert.assertEquals("Joe", result.getResult().get(0).getNickname());
		Assert.assertEquals("Ann", result.getResult().get(1).getNickname());
		Assert.assertEquals("Ann", result.getResult().get(2).getNickname());
		Assert.assertEquals("Fred", result.getResult().get(3).getNickname());
	}

	@Test
	public void testOrderCustomAndGeneric() throws LazyLoadingQueryException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_ORDER, "customOrderTest:asc!nickname:asc");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals("Ann", result.getResult().get(0).getNickname());
		Assert.assertEquals("Ann", result.getResult().get(1).getNickname());
		Assert.assertEquals("Joe", result.getResult().get(2).getNickname());
		Assert.assertEquals("Duke", result.getResult().get(3).getNickname());

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addOrder(UserRepository.ORDER_CUSTOM_TEST, LazyLoadingOrderOperator.ASCENDANT);
		config.addOrder(QUser.user.nickname.getMetadata().getName(), LazyLoadingOrderOperator.ASCENDANT);
		result = userService.findLazyLoading(config);

		Assert.assertEquals("Ann", result.getResult().get(0).getNickname());
		Assert.assertEquals("Ann", result.getResult().get(1).getNickname());
		Assert.assertEquals("Joe", result.getResult().get(2).getNickname());
		Assert.assertEquals("Duke", result.getResult().get(3).getNickname());
	}

	@Test
	public void testOrderLocalizedStringFR() throws LazyLoadingQueryException, LocalizedStringInitException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_ORDER, "name.localizedText:asc");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<Hero> result = heroService.findLazyLoading(config);

		Assert.assertEquals("Capitaine Amérique", result.getResult().get(0).getName().getText(Locale.FRENCH));
		Assert.assertEquals("L'homme araignée", result.getResult().get(1).getName().getText(Locale.FRENCH));
		Assert.assertEquals("L'homme araignée 2", result.getResult().get(2).getName().getText(Locale.FRENCH));
		Assert.assertEquals("Lanterne verte", result.getResult().get(3).getName().getText(Locale.FRENCH));

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addOrder(QHero.hero.name.getMetadata().getName() + "." + GenericRepostiory.FIELD_LOCALIZED_TEXT, LazyLoadingOrderOperator.ASCENDANT);
		result = heroService.findLazyLoading(config);

		Assert.assertEquals("Capitaine Amérique", result.getResult().get(0).getName().getText(Locale.FRENCH));
		Assert.assertEquals("L'homme araignée", result.getResult().get(1).getName().getText(Locale.FRENCH));
		Assert.assertEquals("L'homme araignée 2", result.getResult().get(2).getName().getText(Locale.FRENCH));
		Assert.assertEquals("Lanterne verte", result.getResult().get(3).getName().getText(Locale.FRENCH));
	}

	@Test
	public void testOrderLocalizedStringEN() throws LazyLoadingQueryException, LocalizedStringInitException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_ORDER, "name.localizedText:desc");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.ENGLISH, false, false);
		LazyLoadingResult<Hero> result = heroService.findLazyLoading(config);

		Assert.assertEquals("Spiderman 2", result.getResult().get(0).getName().getText(Locale.ENGLISH));
		Assert.assertEquals("Spiderman", result.getResult().get(1).getName().getText(Locale.ENGLISH));
		Assert.assertEquals("Green Lantern", result.getResult().get(2).getName().getText(Locale.ENGLISH));
		Assert.assertEquals("Captain America", result.getResult().get(3).getName().getText(Locale.ENGLISH));

		config = new LazyLoadingConfiguration(Locale.ENGLISH, false);
		config.setLimit(0);
		config.addOrder(QHero.hero.name.getMetadata().getName() + "." + GenericRepostiory.FIELD_LOCALIZED_TEXT, LazyLoadingOrderOperator.DESCENDANT);
		result = heroService.findLazyLoading(config);

		Assert.assertEquals("Spiderman 2", result.getResult().get(0).getName().getText(Locale.ENGLISH));
		Assert.assertEquals("Spiderman", result.getResult().get(1).getName().getText(Locale.ENGLISH));
		Assert.assertEquals("Green Lantern", result.getResult().get(2).getName().getText(Locale.ENGLISH));
		Assert.assertEquals("Captain America", result.getResult().get(3).getName().getText(Locale.ENGLISH));
	}

	@Test
	public void testFilterLocalizedStringFR() throws LazyLoadingQueryException, LocalizedStringInitException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "name.localizedText=capitaine");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<Hero> result = heroService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals("Capitaine Amérique", result.getResult().get(0).getName().getText(Locale.FRENCH));

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilterCustom(QHero.hero.name.getMetadata().getName() + "." + GenericRepostiory.FIELD_LOCALIZED_TEXT, "capitaine");
		result = heroService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals("Capitaine Amérique", result.getResult().get(0).getName().getText(Locale.FRENCH));

		// Number no operator
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "name.localizedText=2");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		result = heroService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals("L'homme araignée 2", result.getResult().get(0).getName().getText(Locale.FRENCH));

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilterCustom(QHero.hero.name.getMetadata().getName() + "." + GenericRepostiory.FIELD_LOCALIZED_TEXT, "2");
		result = heroService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals("L'homme araignée 2", result.getResult().get(0).getName().getText(Locale.FRENCH));

		// Number and operator
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "name.localizedText=cts:2");
		config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		result = heroService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals("L'homme araignée 2", result.getResult().get(0).getName().getText(Locale.FRENCH));

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(QHero.hero.name.getMetadata().getName() + "." + GenericRepostiory.FIELD_LOCALIZED_TEXT, LazyLoadingFilterOperator.CONTAINS, "2");
		result = heroService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals("L'homme araignée 2", result.getResult().get(0).getName().getText(Locale.FRENCH));
	}

	@Test
	public void testFilterLocalizedStringEN() throws LazyLoadingQueryException, LocalizedStringInitException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "name.localizedText=captain");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.ENGLISH, false, false);
		LazyLoadingResult<Hero> result = heroService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals("Captain America", result.getResult().get(0).getName().getText(Locale.ENGLISH));

		config = new LazyLoadingConfiguration(Locale.ENGLISH, false);
		config.setLimit(0);
		config.addFilterCustom(QHero.hero.name.getMetadata().getName() + "." + GenericRepostiory.FIELD_LOCALIZED_TEXT, "captain");
		result = heroService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals("Captain America", result.getResult().get(0).getName().getText(Locale.ENGLISH));

		// Number no operator
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "name.localizedText=2");
		config = new LazyLoadingConfiguration(params, Locale.ENGLISH, false, false);
		result = heroService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals("Spiderman 2", result.getResult().get(0).getName().getText(Locale.ENGLISH));

		config = new LazyLoadingConfiguration(Locale.ENGLISH, false);
		config.setLimit(0);
		config.addFilterCustom(QHero.hero.name.getMetadata().getName() + "." + GenericRepostiory.FIELD_LOCALIZED_TEXT, "2");
		result = heroService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals("Spiderman 2", result.getResult().get(0).getName().getText(Locale.ENGLISH));

		// Number and operator
		params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "name.localizedText=cts:2");
		config = new LazyLoadingConfiguration(params, Locale.ENGLISH, false, false);
		result = heroService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals("Spiderman 2", result.getResult().get(0).getName().getText(Locale.ENGLISH));

		config = new LazyLoadingConfiguration(Locale.ENGLISH, false);
		config.setLimit(0);
		config.addFilter(QHero.hero.name.getMetadata().getName() + "." + GenericRepostiory.FIELD_LOCALIZED_TEXT, LazyLoadingFilterOperator.CONTAINS, "2");
		result = heroService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals("Spiderman 2", result.getResult().get(0).getName().getText(Locale.ENGLISH));
	}

	@Test
	public void testFilterJoin() throws LazyLoadingQueryException, LocalizedStringInitException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "userHeroes.hero.id=eq:1");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(2, result.getResult().size());

		String heroIDPath = new StringJoiner(".")
				.add(QUser.user.userHeroes.getMetadata().getName())
				.add(QUserHero.userHero.hero.getMetadata().getName())
				.add(QHero.hero.id.getMetadata().getName())
				.toString();

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilter(heroIDPath, LazyLoadingFilterOperator.EQUALS, 1L);
		result = userService.findLazyLoading(config);

		Assert.assertEquals(2, result.getResult().size());
	}

	@Test
	public void testFilterLocalizedStringJoin() throws LazyLoadingQueryException, LocalizedStringInitException {
		MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
		params.putSingle(LazyLoadingConfiguration.PARAM_LIMIT, "0");
		params.putSingle(LazyLoadingConfiguration.PARAM_FILTER, "userHeroes.hero.name.localizedText=Lanterne");
		LazyLoadingConfiguration config = new LazyLoadingConfiguration(params, Locale.FRENCH, false, false);
		LazyLoadingResult<User> result = userService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals(Long.valueOf(1), result.getResult().get(0).getId());

		String campaignTitlePath = new StringJoiner(".")
				.add(QUser.user.userHeroes.getMetadata().getName())
				.add(QUserHero.userHero.hero.getMetadata().getName())
				.add(QHero.hero.name.getMetadata().getName())
				.add(GenericRepostiory.FIELD_LOCALIZED_TEXT)
				.toString();

		config = new LazyLoadingConfiguration(Locale.FRENCH, false);
		config.setLimit(0);
		config.addFilterCustom(campaignTitlePath, "Lanterne");
		result = userService.findLazyLoading(config);

		Assert.assertEquals(1, result.getResult().size());
		Assert.assertEquals(Long.valueOf(1), result.getResult().get(0).getId());
	}

}
