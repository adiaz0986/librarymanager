package com.prueba.ezertech.librarymanager.security.jwt;

import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;

public class TokenJwtConfig {

    // ⚠️ En producción esto DEBE venir de application.yml
    public static final SecretKey SECRET_KEY =
            Jwts.SIG.HS256.key().build();

    public static final long EXPIRATION = 1000 * 60 * 60; // 1 hora

    public static final String PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";

    private TokenJwtConfig() {
        // evita instanciación
    }
}
