package io.github.aureojr.core.catalog.domain;

import java.util.List;

/**
 * @author @aureojr
 * @since 29/12/16.
 */
public interface Product {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    long getId();

    void setId(long id);

    String getName();

    void setName(String name);

    long getQuantityAvailable();

    void setQuantityAvailable(long quantityAvailable);

    String getSize();

    void setSize(String size);

    String getColor();

    void setColor(String color);

    String getWeight();

    void setWeight(String weight);

    String getDescription();

    void setDescription(String description);

    double getPrice();

    void setPrice(double price);

    Category getMainCategory();

    void setMainCategory(Category mainCategory);

    List<Category> getCategories();

    void setCategories(List<Category> categories);
}
