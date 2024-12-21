package com.ccp.service;

import com.ccp.dto.UpdateCustomer;
import com.ccp.model.Customer;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomerService extends UserDetailsService {

    Customer saveCustomer(Customer customer);

    Customer getCustomerById(String customerId);

    Customer getCustomerByEmail(String email);

    Customer getCustomerByPhoneNo(String phoneNo);

    Customer updateCustomer(String customerId, UpdateCustomer updateCustomer);

    String deleteCustomer(String customerId);
}
