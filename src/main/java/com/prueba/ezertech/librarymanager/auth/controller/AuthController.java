package com.prueba.ezertech.librarymanager.auth.controller;

import com.prueba.ezertech.librarymanager.auth.dto.AuthResponse;
import com.prueba.ezertech.librarymanager.auth.dto.LoginRequest;
import com.prueba.ezertech.librarymanager.auth.service.AuthService;
import com.prueba.ezertech.librarymanager.common.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Login con JWT")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
        summary = "Autenticarse",
        description = "Retorna un JWT si las credenciales son válidas"
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @RequestBody LoginRequest request
    ) {
        try {
            AuthResponse response = authService.login(
                    request.getUsername(),
                    request.getPassword()
            );
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (ResponseStatusException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ApiResponse.fail(ex.getReason()));
        }
    }
}
