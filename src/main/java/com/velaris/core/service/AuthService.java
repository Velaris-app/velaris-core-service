package com.velaris.core.service;

import com.velaris.api.model.JwtResponse;
import com.velaris.api.model.LoginRequest;
import com.velaris.api.model.RegisterRequest;
import com.velaris.core.entity.UserEntity;
import com.velaris.core.repository.UserRepository;
import com.velaris.shared.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional(readOnly = true)
    public JwtResponse authenticate(LoginRequest loginRequest) {
        var identifier = loginRequest.getEmail().trim().toLowerCase();
        var user = userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByUsername(identifier))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtProvider.generateToken(user.getUsername());
        return new JwtResponse().token(token);
    }

    @Transactional
    public JwtResponse register(RegisterRequest registerRequest) {
        String username = registerRequest.getUsername().trim();
        String email = registerRequest.getEmail().trim().toLowerCase();

        validateRegistration(username, email);

        UserEntity user = UserEntity.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        userRepository.save(user);

        String token = jwtProvider.generateToken(username);
        return new JwtResponse().token(token);
    }

    private void validateRegistration(String username, String email) {
        userRepository.findByUsernameOrEmail(username, email).ifPresent(existing -> {
            if (existing.getUsername().equalsIgnoreCase(username)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
            }
            if (existing.getEmail().equalsIgnoreCase(email)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
            }
        });
    }
}