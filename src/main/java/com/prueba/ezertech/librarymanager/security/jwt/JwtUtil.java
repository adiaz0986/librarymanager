package com.prueba.ezertech.librarymanager.security.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

    public String generateToken(String username, Long userId) {
        Date now = new Date();
        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + TokenJwtConfig.EXPIRATION))
                .signWith(TokenJwtConfig.SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(TokenJwtConfig.SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
