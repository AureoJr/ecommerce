package io.github.aureojr.ui.controller;

import io.github.aureojr.core.catalog.repository.ProductRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
/**
 * @author @aureojr
 * @since 02/01/17.
 */
@RestController
public class HomeController {

    @Resource(name = "productRepository")
    private ProductRepository productRepository;

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "product/{categoryName}")
    public Map<String,Object> productsHome(@PathVariable(name = "categoryName") String categoryName) {
        Map<String,Object> model = new HashMap<>();
        model.put("id", UUID.randomUUID().toString());
        model.put("products", productRepository.getProductsByCategoryName(categoryName));
        return model;
    }
}
