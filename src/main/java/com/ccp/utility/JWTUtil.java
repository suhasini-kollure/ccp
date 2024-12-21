package com.ccp.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class JWTUtil {

    private static final String SECRET_KEY = System.getenv("SECRET_KEY");
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    private SecretKey signingKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    //Generate Token
    public String generateToken(String customerId) {
        return Jwts.builder()
                .subject(customerId)
                .issuer("Cards and Payment System")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                .signWith(signingKey())
                .compact();
    }

    //Read Token
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Read subject/customerId
    public String getTokenUsername(String token) {
        return getClaims(token).getSubject();
    }

    //Get Exp date
    public Date getExpDate(String token) {
        return getClaims(token).getExpiration();
    }

    //Validate token Exp date
    public boolean isTokenExpired(String token) {
        return getExpDate(token).before(new Date(System.currentTimeMillis()));
    }

    // Blacklist token after logout
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    // Check if token is blacklisted
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    // Validate username passed in token and username from database, and also token expDate
    public boolean validateToken(String token, String customerId) {
        String tokenUserName = getTokenUsername(token);
        return (customerId.equals(tokenUserName) && !isTokenExpired(token) && !isTokenBlacklisted(token));
    }
}
