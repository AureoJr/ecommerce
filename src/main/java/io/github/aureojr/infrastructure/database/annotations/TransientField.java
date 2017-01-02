package io.github.aureojr.infrastructure.database.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author @aureojr
 * @since 29/12/16.
 */
@Retention(value = RUNTIME)
@Target(value = {FIELD,METHOD})
public @interface TransientField {
}
