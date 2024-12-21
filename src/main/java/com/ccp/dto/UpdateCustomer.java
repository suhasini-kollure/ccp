package com.ccp.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCustomer {

    @Size(min = 2, max = 20, message = "name length must be between 2 and 20 characters")
    private String name;

    @Size(min = 2, max = 2)
    private String age;

    @Pattern(regexp = "^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$", message = "please enter valid email")
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = "phoneNo must contain exactly 10 digits")
    private String phoneNo;

    @Size(min = 5, max = 100, message = "address length must be between 10 and 100 characters")
    private String address;

    @Size(min = 4, message = "Password length must be at least 4 chars")
    private String password;
}
