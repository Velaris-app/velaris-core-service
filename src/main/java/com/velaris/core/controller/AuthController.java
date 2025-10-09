package com.velaris.core.controller;

import com.velaris.api.AuthApi;
import com.velaris.api.model.JwtResponse;
import com.velaris.api.model.LoginRequest;
import com.velaris.api.model.RegisterRequest;
import com.velaris.core.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<JwtResponse> login(@Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(
                authService.authenticate(loginRequest)
        );
    }

    @Override
    public ResponseEntity<JwtResponse> register(@Valid RegisterRequest registerRequest) {
        return ResponseEntity.ok(
                authService.register(registerRequest)
        );
    }
}
