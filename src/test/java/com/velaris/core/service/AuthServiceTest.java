package com.velaris.core.service;

import com.velaris.api.model.GrantType;
import com.velaris.api.model.RegisterRequest;
import com.velaris.api.model.TokenRequest;
import com.velaris.api.model.TokenResponse;
import com.velaris.core.entity.UserEntity;
import com.velaris.core.entity.UserSessionEntity;
import com.velaris.core.repository.UserRepository;
import com.velaris.core.repository.UserSessionRepository;
import com.velaris.shared.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSessionRepository userSessionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AuthService authService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .email("test@example.com")
                .passwordHash("encoded_pass")
                .build();

        when(request.getHeader("User-Agent")).thenReturn("TestAgent");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
    }

    @Test
    void authenticateWithPassword_ShouldReturnTokenResponse_WhenValid() {
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setGrantType(GrantType.PASSWORD);
        tokenRequest.setUsername("test@example.com");
        tokenRequest.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded_pass")).thenReturn(true);

        when(jwtProvider.generateAccessToken(any())).thenReturn("access-token");
        when(jwtProvider.generateRefreshToken(any(), any())).thenReturn("refresh-token");
        when(jwtProvider.getExpiresInFromToken("access-token")).thenReturn(3600L);
        when(jwtProvider.getExpiresInFromToken("refresh-token")).thenReturn(86400L);

        when(userSessionRepository.save(any(UserSessionEntity.class))).thenAnswer(invocation -> {
            UserSessionEntity session = invocation.getArgument(0);
            return session.toBuilder().id(UUID.randomUUID()).build();
        });

        TokenResponse response = authService.authenticate(tokenRequest, request);

        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(response.getExpiresIn()).isEqualTo(3600L);
        assertThat(response.getRefreshExpiresIn()).isEqualTo(86400L);
        assertThat(response.getSessionState()).isNotNull();
        assertThat(response.getTokenType()).isEqualTo("Bearer");
    }

    @Test
    void register_ShouldSaveUserAndReturnTokenResponse() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("new@example.com");
        registerRequest.setPassword("pass");

        when(userRepository.findByUsernameOrEmail("newuser", "new@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encoded-pass");

        when(jwtProvider.generateAccessToken(any())).thenReturn("access-token");
        when(jwtProvider.generateRefreshToken(any(), any())).thenReturn("refresh-token");
        when(jwtProvider.getExpiresInFromToken("access-token")).thenReturn(3600L);
        when(jwtProvider.getExpiresInFromToken("refresh-token")).thenReturn(86400L);

        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity u = invocation.getArgument(0);
            return u.toBuilder().id(UUID.randomUUID()).build();
        });

        when(userSessionRepository.save(any(UserSessionEntity.class))).thenAnswer(invocation -> {
            UserSessionEntity s = invocation.getArgument(0);
            return s.toBuilder().id(UUID.randomUUID()).build();
        });

        TokenResponse response = authService.register(registerRequest, request);

        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(response.getSessionState()).isNotNull();
        assertThat(response.getTokenType()).isEqualTo("Bearer");

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getUsername()).isEqualTo("newuser");
        assertThat(userCaptor.getValue().getEmail()).isEqualTo("new@example.com");
        assertThat(userCaptor.getValue().getPasswordHash()).isEqualTo("encoded-pass");
    }
}