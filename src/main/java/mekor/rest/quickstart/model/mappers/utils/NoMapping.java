package mekor.rest.quickstart.model.mappers.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mapstruct.Qualifier;

/**
 * Annotate a mapper method with this so it will be ignored by annotation
 * processor.
 * 
 * @author mekor
 *
 */
@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface NoMapping {

}
