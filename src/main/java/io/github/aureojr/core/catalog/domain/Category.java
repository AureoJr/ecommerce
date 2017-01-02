package io.github.aureojr.core.catalog.domain;

/**
 * @author @aureojr
 * @since 29/12/16.
 */
public interface Category {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    long getId();

    void setId(long id);

    String getName();

    void setName(String name);
}
