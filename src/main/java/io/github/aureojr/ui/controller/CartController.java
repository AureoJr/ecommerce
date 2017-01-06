package io.github.aureojr.ui.controller;

import io.github.aureojr.core.catalog.domain.Product;
import io.github.aureojr.core.catalog.domain.ProductImpl;
import io.github.aureojr.core.catalog.repository.ProductRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author @aureojr
 * @since 05/01/17.
 */
@RestController
public class CartController {

    @Resource(name = "productRepository")
    private ProductRepository productRepository;

    @RequestMapping(value = "cart", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Object> addToCart(@RequestBody ProductImpl product, HttpSession session){

        createSession(product,session);

        return returnDefault(product.getName() + " adicionado com sucesso !");
    }

    @RequestMapping(value = "cart", method = RequestMethod.DELETE)
    public Map<String,Object> removeFromCart(Product product, HttpSession session){

        doRemove(product,session);

        return returnDefault(product.getName() + " removido com sucesso !");
    }

    @RequestMapping(value = "cart", method = RequestMethod.GET)
    public Map<String,Object> getCartProducts(HttpSession session){
        Map<String,Object> returnList = returnDefault("Produtos recuperados");
        returnList.put("products", retrieveCartProducts(session));
        return returnList;
    }

    @RequestMapping(value = "cart/checkout", method = RequestMethod.POST)
    public Map<String, Object> checkout( HttpSession session){
        if(!hasCustomerInSession(session)){
            return returnDefault("Finalize o cadastro");
        }

        productRepository.doCheckout(getCart(session));
        clearCart(session);

        return returnDefault("Pedido finalizado com sucesso!");
    }

    private void clearCart(HttpSession session) {
        session.setAttribute("cart", new ArrayList<>());
    }

    private boolean hasCustomerInSession(HttpSession  session) {
        return session.getAttribute("customer") != null;
    }

    private Map<String, Object> returnDefault(String message){
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("message", message);

        return returnMap;
    }

    private List<Product> getCart(HttpSession session){
        List<Product> products;
        if(session.getAttribute("cart") == null){
            products = new ArrayList<>();
            session.setAttribute("cart",products);
            return products;
        }
        return  (List<Product>) session.getAttribute("cart");

    }

    private List<Product> retrieveCartProducts(HttpSession session) {
        return getCart(session);
    }

    private void createSession(Product product, HttpSession session) {

        List<Product> products = getCart(session);
        products.add(product);
        session.setAttribute("cart",products);
    }

    private void doRemove(Product product, HttpSession session) {
        if(session.getAttribute("cart") != null){
            ((List<Product>)session.getAttribute("cart")).remove(product);
        }
    }
}
