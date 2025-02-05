package com.nit.config;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    // Secret key for signing JWTs (Ensure this key is the same used for both signing and verification)
    private final String secretKey = "7L9do4fT56lbw9tTtQMEZyqIt3nYTjOrS0hctOa1iNo=";

    // Generate JWT token
    public String generateToken(String username) {
        // Set token expiration time (e.g., 10 minutes from now)
    	//long expirationTime = 1000 * 60 *60* 10;  // 10 hrs expiry
    	//long expirationTime = 1000 * 60 * 10;  // 10 minutes expiry
    	long expirationTime = 1000 * 60 * 1;  // 1 minutes expiry
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))  // Set expiration time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // Sign the token with the secret key
                .compact();
    }
    
 // Generate a new access token using refresh token
    public String refreshToken(String refreshToken) {
        String username = extractUsername(refreshToken);
        if (validateToken(refreshToken, username)) {
            // Create a new access token
            return generateToken(username);
        }
        return null;  // If refresh token is invalid or expired, return null
    }

    // Extract the username from JWT token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extract the claims (payload) from the JWT token
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)  // Use the same secret key to validate the token
                .parseClaimsJws(token)
                .getBody();
    }

    // Validate the JWT token by checking if the username matches and the token is not expired
    public boolean validateToken(String token, String username) {
        String tokenUsername = extractUsername(token);  // Extract username from the token
        return (username.equals(tokenUsername) && !isTokenExpired(token));  // Validate username and expiration
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());  // Compare expiration time with current time
    }

    // Extract the expiration date from the JWT token (if needed)
    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();  // Return the expiration date of the token
    }
}
