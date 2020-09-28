/**
 * 
 */
package mekor.rest.quickstart.utils.lazyloading;

import java.util.Arrays;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.StringTemplate;

/**
 * Convert {@link LazyLoadingFilter} or {@link LazyLoadingOrder} to QueryDSL
 * predicates.
 * 
 * @author mekor
 *
 */
@ApplicationScoped
public class LazyLoadingConfigurationToDSL {

	@Inject
	private Logger log;

	/**
	 * Convert a {@link LazyLoadingFilter} to QueryDSL predicate
	 * 
	 * @param filter      The filter to convert
	 * @param pathBuilder The path builder to use to construct the predicate
	 * @return The QueryDSL predicate corresponding to the filter.
	 */
	public Predicate getFilterPredicate(LazyLoadingFilter filter, PathBuilder<Object> pathBuilder) {
		log.debug("Entering getFilterPredicate(filter: {}, pathBuilder: {})", filter, pathBuilder);

		Predicate predicate = null;

		String attribute = getLastAttribute(filter.getKey());

		if (filter.getOperator() != null) {
			switch (filter.getOperator()) {
			case GREATER_THAN: {
				if (filter.getValue() instanceof Double) {
					predicate = pathBuilder.getNumber(attribute, Double.class).gt((Double) filter.getValue());
				}
				else if (filter.getValue() instanceof Long) {
					predicate = pathBuilder.getNumber(attribute, Long.class).gt((Long) filter.getValue());
				}
				else if (filter.getValue() instanceof Date) {
					predicate = pathBuilder.getDate(attribute, Date.class).gt((Date) filter.getValue());
				}
				else if (filter.getValue() instanceof String) {
					predicate = pathBuilder.getString(attribute).gt((String) filter.getValue());
				}
				break;
			}
			case NULL_OR_GREATER_THAN: {
				if (filter.getValue() instanceof Double) {
					NumberPath<Double> path = pathBuilder.getNumber(attribute, Double.class);
					predicate = path.isNull().or(path.gt((Double) filter.getValue()));
				}
				else if (filter.getValue() instanceof Long) {
					NumberPath<Long> path = pathBuilder.getNumber(attribute, Long.class);
					predicate = path.isNull().or(path.gt((Long) filter.getValue()));
				}
				else if (filter.getValue() instanceof Date) {
					DatePath<Date> path = pathBuilder.getDate(attribute, Date.class);
					predicate = path.isNull().or(path.gt((Date) filter.getValue()));
				}
				else if (filter.getValue() instanceof String) {
					StringPath path = pathBuilder.getString(attribute);
					predicate = path.isNull().or(path.gt((String) filter.getValue()));
				}
				break;
			}
			case GREATER_THAN_EQUALS: {
				if (filter.getValue() instanceof Double) {
					predicate = pathBuilder.getNumber(attribute, Double.class).goe((Double) filter.getValue());
				}
				else if (filter.getValue() instanceof Long) {
					predicate = pathBuilder.getNumber(attribute, Long.class).goe((Long) filter.getValue());
				}
				else if (filter.getValue() instanceof Date) {
					predicate = pathBuilder.getDate(attribute, Date.class).goe((Date) filter.getValue());
				}
				else if (filter.getValue() instanceof String) {
					predicate = pathBuilder.getString(attribute).goe((String) filter.getValue());
				}
				break;
			}
			case NULL_OR_GREATER_THAN_EQUALS: {
				if (filter.getValue() instanceof Double) {
					NumberPath<Double> path = pathBuilder.getNumber(attribute, Double.class);
					predicate = path.isNull().or(path.goe((Double) filter.getValue()));
				}
				else if (filter.getValue() instanceof Long) {
					NumberPath<Long> path = pathBuilder.getNumber(attribute, Long.class);
					predicate = path.isNull().or(path.goe((Long) filter.getValue()));
				}
				else if (filter.getValue() instanceof Date) {
					DatePath<Date> path = pathBuilder.getDate(attribute, Date.class);
					predicate = path.isNull().or(path.goe((Date) filter.getValue()));
				}
				else if (filter.getValue() instanceof String) {
					StringPath path = pathBuilder.getString(attribute);
					predicate = path.isNull().or(path.goe((String) filter.getValue()));
				}
				break;
			}
			case LOWER_THAN: {
				if (filter.getValue() instanceof Double) {
					predicate = pathBuilder.getNumber(attribute, Double.class).lt((Double) filter.getValue());
				}
				else if (filter.getValue() instanceof Long) {
					predicate = pathBuilder.getNumber(attribute, Long.class).lt((Long) filter.getValue());
				}
				else if (filter.getValue() instanceof Date) {
					predicate = pathBuilder.getDate(attribute, Date.class).lt((Date) filter.getValue());
				}
				else if (filter.getValue() instanceof String) {
					predicate = pathBuilder.getString(attribute).lt((String) filter.getValue());
				}
				break;
			}
			case NULL_OR_LOWER_THAN: {
				if (filter.getValue() instanceof Double) {
					NumberPath<Double> path = pathBuilder.getNumber(attribute, Double.class);
					predicate = path.isNull().or(path.lt((Double) filter.getValue()));
				}
				else if (filter.getValue() instanceof Long) {
					NumberPath<Long> path = pathBuilder.getNumber(attribute, Long.class);
					predicate = path.isNull().or(path.lt((Long) filter.getValue()));
				}
				else if (filter.getValue() instanceof Date) {
					DatePath<Date> path = pathBuilder.getDate(attribute, Date.class);
					predicate = path.isNull().or(path.lt((Date) filter.getValue()));
				}
				else if (filter.getValue() instanceof String) {
					StringPath path = pathBuilder.getString(attribute);
					predicate = path.isNull().or(path.lt((String) filter.getValue()));
				}
				break;
			}
			case LOWER_THAN_EQUALS: {
				if (filter.getValue() instanceof Double) {
					predicate = pathBuilder.getNumber(attribute, Double.class).loe((Double) filter.getValue());
				}
				else if (filter.getValue() instanceof Long) {
					predicate = pathBuilder.getNumber(attribute, Long.class).loe((Long) filter.getValue());
				}
				else if (filter.getValue() instanceof Date) {
					predicate = pathBuilder.getDate(attribute, Date.class).loe((Date) filter.getValue());
				}
				else if (filter.getValue() instanceof String) {
					predicate = pathBuilder.getString(attribute).loe((String) filter.getValue());
				}
				break;
			}
			case NULL_OR_LOWER_THAN_EQUALS: {
				if (filter.getValue() instanceof Double) {
					NumberPath<Double> path = pathBuilder.getNumber(attribute, Double.class);
					predicate = path.isNull().or(path.loe((Double) filter.getValue()));
				}
				else if (filter.getValue() instanceof Long) {
					NumberPath<Long> path = pathBuilder.getNumber(attribute, Long.class);
					predicate = path.isNull().or(path.loe((Long) filter.getValue()));
				}
				else if (filter.getValue() instanceof Date) {
					DatePath<Date> path = pathBuilder.getDate(attribute, Date.class);
					predicate = path.isNull().or(path.loe((Date) filter.getValue()));
				}
				else if (filter.getValue() instanceof String) {
					StringPath path = pathBuilder.getString(attribute);
					predicate = path.isNull().or(path.loe((String) filter.getValue()));
				}
				break;
			}
			case NOT_EQUALS: {
				if (filter.getValue() instanceof Double) {
					NumberPath<Double> path = pathBuilder.getNumber(attribute, Double.class);
					predicate = path.isNull().or(path.ne((Double) filter.getValue()));
				}
				else if (filter.getValue() instanceof Long) {
					NumberPath<Long> path = pathBuilder.getNumber(attribute, Long.class);
					predicate = path.isNull().or(path.ne((Long) filter.getValue()));
				}
				else if (filter.getValue() instanceof Date) {
					DatePath<Date> path = pathBuilder.getDate(attribute, Date.class);
					predicate = path.isNull().or(path.ne((Date) filter.getValue()));
				}
				else if (filter.getValue() instanceof Boolean) {
					BooleanPath path = pathBuilder.getBoolean(attribute);
					predicate = path.isNull().or(path.ne((Boolean) filter.getValue()));
				}
				else if (filter.getValue() instanceof String) {
					StringTemplate stringCast = Expressions.stringTemplate("cast ({0} as string)", pathBuilder.get(attribute, Object.class));
					predicate = stringCast.ne((String) filter.getValue());
				}
				break;
			}
			case EQUALS: {
				if (filter.getValue() instanceof Double) {
					predicate = pathBuilder.getNumber(attribute, Double.class).eq((Double) filter.getValue());
				}
				else if (filter.getValue() instanceof Long) {
					predicate = pathBuilder.getNumber(attribute, Long.class).eq((Long) filter.getValue());
				}
				else if (filter.getValue() instanceof Date) {
					predicate = pathBuilder.getDate(attribute, Date.class).eq((Date) filter.getValue());
				}
				else if (filter.getValue() instanceof Boolean) {
					predicate = pathBuilder.getBoolean(attribute).eq((Boolean) filter.getValue());
				}
				else if (filter.getValue() instanceof String) {
					StringTemplate stringCast = Expressions.stringTemplate("cast ({0} as string)", pathBuilder.get(attribute, Object.class));
					predicate = stringCast.like((String) filter.getValue());
				}
				break;
			}
			case IN: {
				if (filter.getValue() instanceof Double) {
					Double[] values = Arrays.copyOf(filter.getValues(), filter.getValues().length, Double[].class);
					predicate = pathBuilder.getNumber(attribute, Double.class).in(values);
				}
				else if (filter.getValue() instanceof Long) {
					Long[] values = Arrays.copyOf(filter.getValues(), filter.getValues().length, Long[].class);
					predicate = pathBuilder.getNumber(attribute, Long.class).in(values);
				}
				else if (filter.getValue() instanceof Date) {
					Date[] values = Arrays.copyOf(filter.getValues(), filter.getValues().length, Date[].class);
					predicate = pathBuilder.getDate(attribute, Date.class).in(values);
				}
				else if (filter.getValue() instanceof String) {
					String[] values = Arrays.copyOf(filter.getValues(), filter.getValues().length, String[].class);
					StringTemplate stringCast = Expressions.stringTemplate("cast ({0} as string)", pathBuilder.get(attribute, Object.class));
					predicate = stringCast.in(values);
				}
				break;
			}
			case NOT_IN: {
				if (filter.getValue() instanceof Double) {
					NumberPath<Double> path = pathBuilder.getNumber(attribute, Double.class);
					Double[] values = Arrays.copyOf(filter.getValues(), filter.getValues().length, Double[].class);
					predicate = path.isNull().or(path.notIn(values));
				}
				else if (filter.getValue() instanceof Long) {
					NumberPath<Long> path = pathBuilder.getNumber(attribute, Long.class);
					Long[] values = Arrays.copyOf(filter.getValues(), filter.getValues().length, Long[].class);
					predicate = path.isNull().or(path.notIn(values));
				}
				else if (filter.getValue() instanceof Date) {
					DatePath<Date> path = pathBuilder.getDate(attribute, Date.class);
					Date[] values = Arrays.copyOf(filter.getValues(), filter.getValues().length, Date[].class);
					predicate = path.isNull().or(path.notIn(values));
				}
				else if (filter.getValue() instanceof String) {
					String[] values = Arrays.copyOf(filter.getValues(), filter.getValues().length, String[].class);
					StringTemplate stringCast = Expressions.stringTemplate("cast ({0} as string)", pathBuilder.get(attribute, Object.class));
					predicate = stringCast.isNull().or(stringCast.notIn(values));
				}
				break;
			}
			case BETWEEN: {
				if (filter.getValue() instanceof Double) {
					predicate = (pathBuilder.getNumber(attribute, Double.class)).between((Double) filter.getValues()[0], (Double) filter.getValues()[1]);
				}
				else if (filter.getValue() instanceof Long) {
					predicate = (pathBuilder.getNumber(attribute, Long.class)).between((Long) filter.getValues()[0], (Long) filter.getValues()[1]);
				}
				else if (filter.getValue() instanceof Date) {
					predicate = (pathBuilder.getDate(attribute, Date.class)).between((Date) filter.getValues()[0], (Date) filter.getValues()[1]);
				}
				else if (filter.getValue() instanceof String) {
					predicate = (pathBuilder.getString(attribute)).between((String) filter.getValues()[0], (String) filter.getValues()[1]);
				}
				break;
			}
			case NOT_BETWEEN: {
				if (filter.getValue() instanceof Double) {
					NumberPath<Double> path = pathBuilder.getNumber(attribute, Double.class);
					predicate = path.isNull().or(path.notBetween((Double) filter.getValues()[0], (Double) filter.getValues()[1]));
				}
				else if (filter.getValue() instanceof Long) {
					NumberPath<Long> path = pathBuilder.getNumber(attribute, Long.class);
					predicate = path.isNull().or(path.notBetween((Long) filter.getValues()[0], (Long) filter.getValues()[1]));
				}
				else if (filter.getValue() instanceof Date) {
					DatePath<Date> path = pathBuilder.getDate(attribute, Date.class);
					predicate = path.isNull().or(path.notBetween((Date) filter.getValues()[0], (Date) filter.getValues()[1]));
				}
				else if (filter.getValue() instanceof String) {
					StringPath path = pathBuilder.getString(attribute);
					predicate = path.isNull().or(path.notBetween((String) filter.getValues()[0], (String) filter.getValues()[1]));
				}
				break;
			}
			case CONTAINS: {
				predicate = (pathBuilder.getString(attribute)).contains(filter.getValue().toString());
				break;
			}
			case NULL: {
				predicate = pathBuilder.get(attribute).isNull();
				break;
			}
			case NOT_NULL: {
				predicate = pathBuilder.get(attribute).isNotNull();
				break;
			}
			}
		}
		else {
			if (filter.getValue() instanceof Double) {
				predicate = (pathBuilder.getNumber(attribute, Double.class)).eq((Double) filter.getValue());
			}
			if (filter.getValue() instanceof Long) {
				predicate = (pathBuilder.getNumber(attribute, Long.class)).eq((Long) filter.getValue());
			}
			else if (filter.getValue() instanceof Date) {
				predicate = (pathBuilder.getDate(attribute, Date.class)).eq((Date) filter.getValue());
			}
			else if (filter.getValue() instanceof Boolean) {
				predicate = (pathBuilder.getBoolean(attribute)).eq((Boolean) filter.getValue());
			}
			else if (filter.getValue() instanceof String) {
				StringTemplate stringCast = Expressions.stringTemplate("cast ({0} as string)", pathBuilder.get(attribute, Object.class));
				predicate = stringCast.like((String) filter.getValue());
			}
		}
		String type = filter.getValues() != null ? filter.getValue().getClass().getSimpleName() : "null";
		log.debug("Leaving getFilterPredicate() -> {} ({})", predicate, type);
		return predicate;
	}

	/**
	 * Convert a {@link LazyLoadingOrder} to QueryDSL predicate
	 * 
	 * @param order       The order to convert
	 * @param pathBuilder The path builder to use to construct the predicate
	 * @return The QueryDSL predicate corresponding to the order.
	 */
	public OrderSpecifier<?> getOrderSpecifier(LazyLoadingOrder order, PathBuilder<Object> pathBuilder) {
		log.debug("Entering getOrderSpecifier(order: {}, pathBuilder: {})", order, pathBuilder);
		OrderSpecifier<?> specifier = null;

		String attribute = getLastAttribute(order.getKey());

		switch (order.getOperator()) {
		case ASCENDANT:
			specifier = pathBuilder.getComparable(attribute, Comparable.class).asc();
			break;
		case DESCENDANT:
			specifier = pathBuilder.getComparable(attribute, Comparable.class).desc();
			break;
		}

		log.debug("Leaving getOrderSpecifier() -> {}", specifier);
		return specifier;
	}

	/**
	 * Get the last attribute of the key given in parameter. <br />
	 * <strong>ex: </strong> "user.child.firstname" -> "firstname"
	 * 
	 * @param key The key we want the last attribute from
	 * @return The last attribute of the key
	 */
	private String getLastAttribute(String key) {
		String[] keySplit = key.split("\\.");
		return keySplit[keySplit.length - 1];
	}
}