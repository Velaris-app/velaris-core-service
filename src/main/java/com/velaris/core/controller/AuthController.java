package com.velaris.core.controller;

import com.velaris.api.AuthApi;
import com.velaris.api.model.*;
import com.velaris.core.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

import static com.velaris.shared.security.SecurityUtils.currentUserId;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final HttpServletRequest request;

    @Override
    public ResponseEntity<TokenResponse> getToken(@Valid TokenRequest tokenRequest) {
        TokenResponse response = authService.authenticate(tokenRequest, request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TokenResponse> registerUser(@Valid RegisterRequest registerRequest) {
        TokenResponse response = authService.register(registerRequest, request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteSession(UUID id) {
        authService.revokeSession(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> revokeSessions() {
        authService.revokeAllSessions(currentUserId());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<SessionItem>> getSessions() {
        List<SessionItem> sessions = authService.getActiveSessions(currentUserId());
        return ResponseEntity.ok(sessions);
    }
}