package io.github.aureojr.core.profile.domain;

import io.github.aureojr.core.infrastructure.database.annotations.DefaultModel;
import io.github.aureojr.core.infrastructure.database.annotations.TransientField;

import java.util.Locale;

/**
 * @author @aureojr
 * @since 24/12/16.
 */
@DefaultModel
public interface Address {

    String getId();

    void setId(String id);

    String getAddresName();

    void setAddresName(String addresName);

    @TransientField
    Locale getLocale();

    void setLocale(Locale locale);

    String getCityProvice();

    void setCityProvice(String cityProvice);

    String getZipCode();

    void setZipCode(String zipCode);

    String getAddresLine();

    void setAddresLine(String addresLine);
}
