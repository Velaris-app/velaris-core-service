package com.velaris.core.service;

import com.velaris.api.model.GrantType;
import com.velaris.api.model.RegisterRequest;
import com.velaris.api.model.TokenRequest;
import com.velaris.api.model.TokenResponse;
import com.velaris.core.entity.UserEntity;
import com.velaris.core.repository.jpa.JpaUserRepository;
import com.velaris.core.repository.jpa.JpaUserSessionRepository;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JpaUserRepository jpaUserRepository;

    @Mock
    private JpaUserSessionRepository jpaUserSessionRepository;

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

        when(jpaUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jpaUserRepository.findByUsername("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.matches("password", "encoded_pass")).thenReturn(true);
        when(jwtProvider.generateAccessToken(user.getId())).thenReturn("access-token");
        when(jwtProvider.generateRefreshToken(user.getId())).thenReturn("refresh-token");
        when(jwtProvider.getExpiresInFromToken("access-token")).thenReturn(3600L);
        when(jwtProvider.getExpiresInFromToken("refresh-token")).thenReturn(86400L);

        when(jpaUserSessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

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

        when(jpaUserRepository.findByUsernameOrEmail("newuser", "new@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encoded-pass");
        when(jwtProvider.generateAccessToken(any())).thenReturn("access-token");
        when(jwtProvider.generateRefreshToken(any())).thenReturn("refresh-token");
        when(jwtProvider.getExpiresInFromToken("access-token")).thenReturn(3600L);
        when(jwtProvider.getExpiresInFromToken("refresh-token")).thenReturn(86400L);
        when(jpaUserSessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        TokenResponse response = authService.register(registerRequest, request);

        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(response.getSessionState()).isNotNull();
        assertThat(response.getTokenType()).isEqualTo("Bearer");

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(jpaUserRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getUsername()).isEqualTo("newuser");
        assertThat(userCaptor.getValue().getEmail()).isEqualTo("new@example.com");
    }
}