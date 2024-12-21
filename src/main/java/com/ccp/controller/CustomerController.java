package com.ccp.controller;

import com.ccp.dto.LoginRequest;
import com.ccp.dto.LoginResponse;
import com.ccp.dto.UpdateCustomer;
import com.ccp.model.Customer;
import com.ccp.service.CustomerService;
import com.ccp.utility.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("customer")
@Tag(name = "Customer Controller", description = "Controller for customer operations")
public class CustomerController {

    private final CustomerService customerService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public CustomerController(CustomerService customerService, JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Save Customer", description = "Endpoint to save a new customer")
    @PostMapping("/save")
    public ResponseEntity<Customer> saveCustomer(@Valid @RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveCustomer(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.OK);
    }

    @Operation(summary = "Login Customer", description = "Endpoint to login a customer")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getCustomerId(), loginRequest.getPassword()));
        LoginResponse loginResponse = new LoginResponse();
        if (authenticate.isAuthenticated()) {
            loginResponse.setToken(jwtUtil.generateToken(loginRequest.getCustomerId()));
            loginResponse.setMessage("Welcome, login successful");
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } else {
            loginResponse.setMessage("Unauthorized access");
            return new ResponseEntity<>(loginResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Get Customer by CustomerId", description = "Endpoint to get a existing customer by customerID")
    @GetMapping("/getById/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") String customerId) {
        Customer customerById = customerService.getCustomerById(customerId);
        return new ResponseEntity<>(customerById, HttpStatus.OK);
    }

    @Operation(summary = "Get Customer by Email", description = "Endpoint to get a existing customer by email")
    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable("email") String email) {
        Customer customerByEmail = customerService.getCustomerByEmail(email);
        return new ResponseEntity<>(customerByEmail, HttpStatus.OK);
    }

    @Operation(summary = "Get Customer by PhoneNo", description = "Endpoint to get a existing customer by phoneNo")
    @GetMapping("/getByPhoneNo/{phoneNo}")
    public ResponseEntity<Customer> getByPhoneNo(@PathVariable("phoneNo") String phoneNo) {
        Customer customerByPhoneNo = customerService.getCustomerByPhoneNo(phoneNo);
        return new ResponseEntity<>(customerByPhoneNo, HttpStatus.OK);
    }

    @Operation(summary = "Update Customer", description = "Endpoint to update a existing customer by customer id")
    @PatchMapping("/update/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("customerId") String customerId, @Valid @RequestBody UpdateCustomer updateCustomer) {
        Customer customer = customerService.updateCustomer(customerId, updateCustomer);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @Operation(summary = "Delete Customer", description = "Endpoint to delete a customer by customer id")
    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("customerId") String customerId) {
        String deletedCustomer = customerService.deleteCustomer(customerId);
        return new ResponseEntity<>(deletedCustomer, HttpStatus.OK);
    }

    @Operation(summary = "Logout Customer", description = "Endpoint to logout a customer")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader, Principal principal) {
        String token = authHeader.substring(7);
        jwtUtil.blacklistToken(token);
        return new ResponseEntity<>(String.format("%s, you have been logged out.", principal.getName()), HttpStatus.OK);
    }
}
