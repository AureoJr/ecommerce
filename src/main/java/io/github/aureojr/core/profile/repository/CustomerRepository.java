package io.github.aureojr.core.profile.repository;

import io.github.aureojr.core.profile.domain.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author @aureojr
 * @since 29/12/16.
 */
@Repository("customerRepository")
public interface CustomerRepository {

    List<Customer> getAllCustomers();

    Customer getSessionCustomer(Integer sessionID);

    void createCustomer(Customer customer);


}
