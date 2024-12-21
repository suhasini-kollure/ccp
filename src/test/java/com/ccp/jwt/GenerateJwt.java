package com.ccp.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.stereotype.Component;

@Component
public class GenerateJwt {

    public static void main(String[] args) {

        String secretKey =
                DatatypeConverter.printHexBinary(Jwts.SIG.HS256.key().build().getEncoded());

        System.out.println(secretKey);
    }
}
