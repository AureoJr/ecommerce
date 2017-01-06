package io.github.aureojr.core.catalog.domain;

import java.util.List;

/**
 * @author @aureojr
 * @since 29/12/16.
 */
public interface Product {

    Boolean isEnabled();

    void setEnabled(Boolean enabled);

    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    Integer getQuantityAvailable();

    void setQuantityAvailable(Integer quantityAvailable);

    String getSize();

    void setSize(String size);

    String getColor();

    void setColor(String color);

    String getWeight();

    void setWeight(String weight);

    String getDescription();

    void setDescription(String description);

    Double getPrice();

    void setPrice(Double price);

    Category getMainCategory();

    void setMainCategory(Category mainCategory);

    List<Category> getCategories();

    void setCategories(List<Category> categories);

    void updateStock(Product byId);
}
