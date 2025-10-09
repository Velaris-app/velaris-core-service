package com.velaris.core.service;

import com.velaris.api.model.JwtResponse;
import com.velaris.api.model.LoginRequest;
import com.velaris.api.model.RegisterRequest;
import com.velaris.core.entity.UserEntity;
import com.velaris.core.repository.UserRepository;
import com.velaris.shared.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional(readOnly = true)
    public JwtResponse authenticate(LoginRequest loginRequest) {
        var user = userRepository.findByUsername(loginRequest.getEmail())
                .orElseThrow(() -> new AccessDeniedException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new AccessDeniedException("Invalid credentials");
        }

        String token = jwtProvider.generateToken(user.getUsername());
        return new JwtResponse().token(token);
    }

    @Transactional
    public JwtResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new AccessDeniedException("Username already taken");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AccessDeniedException("Email already in use");
        }

        var user = UserEntity.builder()
                    .username(registerRequest.getUsername())
                    .email(registerRequest.getEmail())
                    .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                    .build();
        userRepository.save(user);

        String token = jwtProvider.generateToken(registerRequest.getUsername());
        return new JwtResponse().token(token);
    }
}
