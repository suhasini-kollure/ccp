package com.ccp.repository;

import com.ccp.model.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    boolean existsByEmail(String email);

    boolean existsByPhoneNo(String phoneNo);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhoneNo(String phoneNo);
}
