package io.github.aureojr.core.catalog.domain;

import io.github.aureojr.infrastructure.database.annotations.DefaultModel;
import io.github.aureojr.infrastructure.database.annotations.ID;
import io.github.aureojr.infrastructure.database.annotations.TransientField;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author @aureojr
 * @since 29/12/16.
 */
@DefaultModel(name = "product")
public class ProductImpl implements Product,Serializable{

    @ID(name = "id")
    private Long id;

    private String name;

    private Integer quantityAvailable;

    private String size;

    private String color;

    private String weight;

    private String description;

    private Double price;

    private Category mainCategory;

    @TransientField
    private List<Category> categories;

    private String defaultImage;

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    private Boolean enabled;

    public Boolean getEnabled() {
        return enabled;
    }

    public Boolean isChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @TransientField
    private Boolean checked;

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Category getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(Category mainCategory) {
        this.mainCategory = mainCategory;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public void updateStock(Product oldProduct) {
        if(Objects.equals(oldProduct.getQuantityAvailable(), quantityAvailable))
            setQuantityAvailable(quantityAvailable - 1);
        setQuantityAvailable((quantityAvailable - oldProduct.getQuantityAvailable()) * -1 );

        if(quantityAvailable <= 0 )
            setEnabled(false);

    }
}
