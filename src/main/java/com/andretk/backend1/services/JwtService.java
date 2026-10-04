package com.andretk.backend1.services;

import com.andretk.backend1.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String secret;

    public String generateAccessToken(User user){
        final long tokenExpiration = 600; // 1 day

        return generateToken(user, tokenExpiration);
    }

    public String generateRefreshToken(User user){
        final long tokenExpiration = 604800; //7d

        return generateToken(user, tokenExpiration);
    }

    private String generateToken(User user, long tokenExpiration){

        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public boolean validateToken(String token){
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException  e) {
            return false;
        }
    }

    public Long getIdFromToken(String token){
        return Long.valueOf(getClaims(token).getSubject());
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token).getPayload();
    }
}
