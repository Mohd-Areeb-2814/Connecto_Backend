package com.fb.user.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";



    // Extract username from the JWT token

    public String extractUsername(String token) {

        return extractClaims(token).getSubject();

    }



    // Extract Claims from the JWT token

    private Claims extractClaims(String token) {

        return Jwts.parserBuilder().setSigningKey(SECRET).build().parseClaimsJws(token).getBody();

    }



    public void validateToken(final String token) {

        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);

    }



    // Updated to include both email and username in the token

    public String generateToken(String username, String email) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("username", username); // Add username to claims

        // claims.put("email", email); // Add email to claims



        // Set 'sub' as email (as it's commonly used for the subject in JWT)

        return createToken(claims, email); // Use email as the subject (sub)

    }



    private String createToken(Map<String, Object> claims, String email) {

        return Jwts.builder()

                .setClaims(claims)

                .setSubject(email) // 'sub' as email

                .setIssuedAt(new Date(System.currentTimeMillis()))

                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 min expiration

                .signWith(getSignKey(), SignatureAlgorithm.HS256)

                .compact();

    }





    private Key getSignKey() {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET);

        return Keys.hmacShaKeyFor(keyBytes);

    }

}


