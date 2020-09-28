package mekor.rest.quickstart.utils.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import mekor.rest.quickstart.model.entities.utils.SQLConstants;

/**
 * Constraint for password in the application. Must have at least 8 caracters,
 * one numeric caracter and no spaces
 * 
 * @author pgavo
 *
 */
@Constraint(validatedBy = {})
@NotNull
@Length(min = 8, max = SQLConstants.VARCHAR_MAX_LENGTH)
@Pattern(regexp = "\\S*\\d+\\S*", message = "Must have at least one numeric caracter and no spaces")
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
	String message() default "Password must have at least 8 caracters, one numeric caracter and no spaces";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
