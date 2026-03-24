package com.kaamSet.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import com.kaamSet.backend.model.User;

@Service
public class JwtService {

    // 🔑 Secret key (later move to application.properties)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // ✅ GENERATE TOKEN WITH ROLE
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name()) // 🔑 role inside JWT
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(key, SignatureAlgorithm.HS256) // ✅ FIX HERE
                .compact();
    }

    // ✅ EXTRACT EMAIL
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ✅ EXTRACT ROLE
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // 🔧 GENERIC CLAIM EXTRACTOR
    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    // 🔧 PARSE TOKEN
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
