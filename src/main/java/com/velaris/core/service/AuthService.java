package com.velaris.core.service;

import com.velaris.api.model.*;
import com.velaris.core.entity.UserEntity;
import com.velaris.core.entity.UserSessionEntity;
import com.velaris.core.mapper.UserSessionMapper;
import com.velaris.core.repository.UserRepository;
import com.velaris.core.repository.UserSessionRepository;
import com.velaris.shared.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static com.velaris.shared.util.UserAgentUtil.parseDeviceAndOS;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final UserSessionMapper sessionMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenResponse authenticate(TokenRequest tokenRequest, HttpServletRequest request) {
        return switch (tokenRequest.getGrantType()) {
            case PASSWORD -> authenticateWithPassword(tokenRequest, request);
            case REFRESH_TOKEN -> authenticateWithRefreshToken(tokenRequest, request);
        };
    }

    private TokenResponse authenticateWithPassword(TokenRequest tokenRequest, HttpServletRequest request) {
        var identifier = tokenRequest.getUsername();
        var user = userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByUsername(identifier))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(tokenRequest.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return createSessionAndReturnTokens(user, request);
    }

    private TokenResponse authenticateWithRefreshToken(TokenRequest tokenRequest, HttpServletRequest request) {
        String jti = jwtProvider.getJtiFromRefreshToken(tokenRequest.getRefreshToken());
        UserSessionEntity oldSession = userSessionRepository.findById(UUID.fromString(jti))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        UserEntity userEntity = oldSession.getUser();
        userSessionRepository.deleteById(oldSession.getId());

        return createSessionAndReturnTokens(userEntity, request);
    }

    @Transactional
    public TokenResponse register(RegisterRequest registerRequest, HttpServletRequest request) {
        String username = registerRequest.getUsername();
        String email = registerRequest.getEmail().trim().toLowerCase();

        validateRegistration(username, email);

        UserEntity user = userRepository.save(
                UserEntity.builder()
                        .username(username)
                        .email(email)
                        .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                        .build()
        );

        return createSessionAndReturnTokens(user, request);
    }

    private TokenResponse createSessionAndReturnTokens(UserEntity user, HttpServletRequest request) {
        OffsetDateTime now = OffsetDateTime.now();

        // najpierw stwórz sesję z pustymi tokenami
        UserSessionEntity session = userSessionRepository.save(UserSessionEntity.builder()
                .user(user)
                .device(parseDeviceAndOS(request.getHeader("User-Agent")))
                .userAgent(request.getHeader("User-Agent"))
                .ipAddress(request.getRemoteAddr())
                .createdAt(now)
                .lastUsedAt(now)
                .expiresAt(now.plusSeconds(0))
                .build());

        String accessToken = jwtProvider.generateAccessToken(user.getId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId(), session.getId());

        session.setAccessToken(accessToken);
        session.setRefreshToken(refreshToken);
        session.setExpiresAt(now.plusSeconds(jwtProvider.getExpiresInFromToken(refreshToken)));
        userSessionRepository.save(session);

        return new TokenResponse()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtProvider.getExpiresInFromToken(accessToken))
                .refreshExpiresIn(jwtProvider.getExpiresInFromToken(refreshToken))
                .sessionState(session.getId())
                .tokenType("Bearer");
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

    @Transactional
    public void revokeSession(UUID sessionId) {
        userSessionRepository.deleteById(sessionId);
    }

    @Transactional
    public void revokeAllSessions(UUID userId) {
        userSessionRepository.deleteByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<SessionItem> getActiveSessions(UUID userId) {
        return userSessionRepository.findByUserId(userId).stream()
                .map(sessionMapper::toDto).toList();
    }
}