package io.github.aureojr.core.catalog.domain;

import io.github.aureojr.core.infrastructure.database.annotations.DefaultModel;

/**
 * @author @aureojr
 * @since 29/12/16.
 */
@DefaultModel
public interface Category {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    long getId();

    void setId(long id);

    String getName();

    void setName(String name);
}
