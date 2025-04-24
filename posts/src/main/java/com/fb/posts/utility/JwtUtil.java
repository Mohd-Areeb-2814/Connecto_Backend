package com.fb.posts.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

	// Extract username from the JWT token

	public String extractUsername(String token) {

		return extractClaims(token).getSubject();

	}

	// Extract Claims from the JWT token

	public Claims extractClaims(String token) {

		return Jwts.parserBuilder().setSigningKey(SECRET).build().parseClaimsJws(token).getBody();

	}

	public void validateToken(final String token) {
		Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}