package io.github.aureojr.core.catalog.repository;

import io.github.aureojr.core.catalog.domain.Category;
import io.github.aureojr.core.catalog.domain.CategoryImpl;
import io.github.aureojr.core.catalog.domain.Product;
import io.github.aureojr.core.catalog.domain.ProductImpl;
import io.github.aureojr.infrastructure.persistence.PersistenceUnit;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author @aureojr
 * @since 03/01/17.
 */
@Repository("productRepository")
public class ProductRepositoryImpl implements ProductRepository {

    @Resource(name = "persistenceUnit")
    private PersistenceUnit<Category> categoryPU;

    @Resource(name = "persistenceUnit")
    private PersistenceUnit<Product> productPU;

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
    public Product getProductById(Object product) {

        return productPU.findById(product);

    }

    @Override
    public List<Category> getAllCategories() {
        return categoryPU.findAll(CategoryImpl.class);
    }

    @Override
    public List<Product> getProductsByCategoryName(String categoryName) {
        List<Category> categories = categoryPU.find(CategoryImpl.class, " LOWER(name) = LOWER('"+ categoryName +"')");
        List<Product> returnList = new ArrayList<>();
        categories.forEach( category ->
            returnList.addAll(
                productPU.find(ProductImpl.class, "mainCategory="+category.getId())
            )
        );
        return returnList;
    }

    @Override
    public  void doCheckout(List<Product> products){

        products.forEach(product -> {
                product.updateStock(productPU.findById(product));
                productPU.update(ProductImpl.class, product);
        });

    }
}
