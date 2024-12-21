package com.ccp.service;

import com.ccp.dto.UpdateCustomer;
import com.ccp.model.Customer;
import com.ccp.repository.CustomerRepository;
import jakarta.ws.rs.NotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        if (customerRepository.existsById(customer.getCustomerId())) {
            log.error("Customer already exists with customerId : {}", customer.getCustomerId());
            throw new DataIntegrityViolationException(
                    String.format("Customer already exists with customerId : %s", customer.getCustomerId()));
        }
        if (customerRepository.existsByEmail(customer.getEmail())) {
            log.error("Customer already exists with email : {}", customer.getEmail());
            throw new DataIntegrityViolationException(
                    String.format("Customer already exists with email : %s", customer.getEmail()));
        }
        if (customerRepository.existsByPhoneNo(customer.getPhoneNo())) {
            log.error("Customer already exists with phone number : {}", customer.getPhoneNo());
            throw new DataIntegrityViolationException(
                    String.format("Customer already exists with phone number : %s", customer.getPhoneNo()));
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Password encoded and new customer is saved in DB");
        return savedCustomer;
    }

    @Override
    public Customer getCustomerById(String customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            log.info("Customer found with customerId : {}", customer.getCustomerId());
            return customer;
        } else {
            log.error("Customer not found with customerId : {}", customerId);
            throw new NotFoundException(String.format("Customer not found with customerId : %s", customerId));
        }
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            log.info("Customer found with email : {}", email);
            return customer;
        } else {
            log.error("Customer not found with email {}", email);
            throw new NotFoundException(String.format("Customer not found with email : %s", email));
        }
    }

    @Override
    public Customer getCustomerByPhoneNo(String phoneNo) {
        Optional<Customer> optionalCustomer = customerRepository.findByPhoneNo(phoneNo);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            log.info("Customer found with phoneNo : {}", phoneNo);
            return customer;
        } else {
            log.error("Customer not found with phoneNo : {}", phoneNo);
            throw new NotFoundException(String.format("Customer not found with email : %s", phoneNo));
        }
    }

    @Override
    public Customer updateCustomer(String customerId, UpdateCustomer updateCustomer) {
        Customer customer = getCustomerById(customerId);
        if (updateCustomer.getName() != null) {
            customer.setName(updateCustomer.getName());
        }
        if (updateCustomer.getEmail() != null) {
            customer.setEmail(updateCustomer.getEmail());
        }
        if (updateCustomer.getAge() != null) {
            customer.setAge(updateCustomer.getAge());
        }
        if (updateCustomer.getPhoneNo() != null) {
            customer.setPhoneNo(updateCustomer.getPhoneNo());
        }
        if (updateCustomer.getPassword() != null) {
            if (passwordEncoder.matches(updateCustomer.getPassword(), customer.getPassword())) {
                throw new IllegalArgumentException("Please enter different password!");
            }
            customer.setPassword(passwordEncoder.encode(updateCustomer.getPassword()));
        }
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer updated for customerId : {}", customer.getCustomerId());
        return updatedCustomer;
    }

    @Override
    public String deleteCustomer(String customerId) {
        Customer customer = getCustomerById(customerId);
        customerRepository.deleteById(customer.getCustomerId());
        log.info("Customer deleted with customerId : {}", customerId);
        return String.format("Customer deleted with customerId : %s", customerId);
    }

    @Override
    public UserDetails loadUserByUsername(String customerId) throws UsernameNotFoundException {
        Customer customer = getCustomerById(customerId);

        Set<String> roles = new HashSet<>();
        roles.add("USER");
        roles.add("ADMIN");

        return new User(
                customerId,
                customer.getPassword(),
                roles.stream().map(SimpleGrantedAuthority::new).toList());
    }
}
