package io.github.aureojr.infrastructure.database.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author @aureojr
 * @since 30/12/16.
 */
@Retention(RUNTIME)
@Target(value = {METHOD, FIELD})
public @interface ID {

    String name() default "id";
}
