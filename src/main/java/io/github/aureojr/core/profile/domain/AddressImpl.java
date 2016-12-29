package io.github.aureojr.core.profile.domain;

import java.util.Locale;

/**
 * @author @aureojr
 * @since 26/12/16.
 */
public class AddressImpl implements Address {

    private String id;

    private String addresName;

    private Locale locale;

    private String cityProvice;

    private String zipCode;

    private String addresLine;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddresName() {
        return addresName;
    }

    public void setAddresName(String addresName) {
        this.addresName = addresName;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getCityProvice() {
        return cityProvice;
    }

    public void setCityProvice(String cityProvice) {
        this.cityProvice = cityProvice;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddresLine() {
        return addresLine;
    }

    public void setAddresLine(String addresLine) {
        this.addresLine = addresLine;
    }
}
