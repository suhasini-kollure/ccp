package com.ccp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @Pattern(regexp = "^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$", message = "Please enter a valid customerId.")
    private String customerId;

    @NotEmpty(message = "Password can't be null.")
    @Size(min = 4, message = "Password length must be at least 4 characters.")
    private String password;
}
