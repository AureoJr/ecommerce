package io.github.aureojr.core.catalog.repository;

import io.github.aureojr.core.catalog.domain.Category;
import io.github.aureojr.core.catalog.domain.CategoryImpl;
import io.github.aureojr.core.catalog.domain.Product;
import io.github.aureojr.infrastructure.persistence.PersistenceUnit;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author @aureojr
 * @since 03/01/17.
 */
@Repository("productRepository")
public class ProductRepositoryImpl implements ProductRepository {

    @Resource(name = "persistenceUnit")
    private PersistenceUnit<Category> categoryPU;


    @Override
    public List<Product> getProductsByCategory(Category category) {
        return null;
    }

    @Override
    public void addProduct(Product product) {

    }

    @Override
    public Product updateProduct(Product product) {
        return null;
    }

    @Override
    public void addCategory(Category category) {

    }

    @Override
    public void disableCategory(Category category) {

    }

    @Override
    public void disableProduct(Category category) {

    }

    @Override
    public Product getProductById(long productID) {
        return null;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryPU.findAll(CategoryImpl.class);
    }
}
