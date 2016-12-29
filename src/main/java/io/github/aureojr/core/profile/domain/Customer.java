package io.github.aureojr.core.profile.domain;

/**
 * @author @aureojr
 * @since 24/12/16.
 */
public interface Customer {

    long getId();

    void setId(long id);

    String getName();

    void setName(String name);

    Address getAddress();

    void setAddress(Address address);
}
