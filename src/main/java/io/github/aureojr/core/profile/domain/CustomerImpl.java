package io.github.aureojr.core.profile.domain;

/**
 *
 * @author @aureojr
 * @since 24/12/16.
 */
public class CustomerImpl implements Customer{

    private long id;

    private  String name;

    private Address address;

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
