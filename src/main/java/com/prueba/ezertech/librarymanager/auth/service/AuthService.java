package com.prueba.ezertech.librarymanager.auth.service;

import com.prueba.ezertech.librarymanager.auth.dto.AuthResponse;
import com.prueba.ezertech.librarymanager.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse login(String username, String password) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario o contraseña incorrectos"
            );
        }

        String token = jwtUtil.generateToken(username, null);

        return new AuthResponse(token);
    }
}
