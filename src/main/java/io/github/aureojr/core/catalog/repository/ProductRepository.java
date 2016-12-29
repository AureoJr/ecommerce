package io.github.aureojr.core.catalog.repository;

import io.github.aureojr.core.catalog.domain.Category;
import io.github.aureojr.core.catalog.domain.Product;

import java.util.List;

/**
 * @author @aureojr
 * @since 29/12/16.
 */
public interface ProductRepository {

    List<Product> getProductsByCategory(Category category);

    void addProduct(Product product);

    Product updateProduct(Product product);

    void addCategory(Category category);

    void disableCategory(Category category);

    void disableProduct(Category category);

    Product getProductById(long productID);

    List<Category> getAllCategories();

}
