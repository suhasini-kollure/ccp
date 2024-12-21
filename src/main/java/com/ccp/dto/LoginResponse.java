package com.ccp.dto;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;
    private String message;
}
