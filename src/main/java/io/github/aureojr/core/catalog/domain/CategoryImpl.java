package io.github.aureojr.core.catalog.domain;

import io.github.aureojr.infrastructure.database.annotations.DefaultModel;
import io.github.aureojr.infrastructure.database.annotations.ID;

/**
 * @author @aureojr
 * @since 29/12/16.
 */
@DefaultModel
public class CategoryImpl implements Category{

    @ID
    private long id;

    private String name;

    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
