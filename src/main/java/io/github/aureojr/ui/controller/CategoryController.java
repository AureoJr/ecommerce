package io.github.aureojr.ui.controller;

import io.github.aureojr.core.catalog.repository.ProductRepository;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author @aureojr
 * @since 03/01/17.
 */
@RestController(value = "category")
public class CategoryController {

    @Resource(name = "productRepository")
    private ProductRepository productRepository;

    @RequestMapping(method = RequestMethod.GET, value = "categories", produces = MediaType.APPLICATION_JSON_VALUE)
    private Map<String,Object> getCategories(){
        Map<String,Object> model = new HashMap<>();
        model.put("categories", productRepository.getAllCategories());
        return model;
    }
}
