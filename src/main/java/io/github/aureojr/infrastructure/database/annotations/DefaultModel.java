package io.github.aureojr.infrastructure.database.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author @aureojr
 * @since 29/12/16.
 */
@Retention(value = RUNTIME)
@Target(value = TYPE)
public @interface DefaultModel {

    String name() default  "";
}
