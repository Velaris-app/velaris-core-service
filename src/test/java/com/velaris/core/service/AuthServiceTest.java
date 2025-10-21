package com.velaris.core.service;

import com.velaris.api.model.JwtResponse;
import com.velaris.api.model.LoginRequest;
import com.velaris.api.model.RegisterRequest;
import com.velaris.core.entity.UserEntity;
import com.velaris.core.repository.UserRepository;
import com.velaris.shared.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .passwordHash("encoded_pass")
                .build();
    }

    @Test
    void authenticate_ShouldReturnJwt_WhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest()
                .email("test@example.com")
                .password("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded_pass")).thenReturn(true);
        when(jwtProvider.generateToken("testuser")).thenReturn("jwt-token");

        JwtResponse response = authService.authenticate(request);

        assertThat(response.getToken()).isEqualTo("jwt-token");
        verify(jwtProvider).generateToken("testuser");
    }

    @Test
    void authenticate_ShouldThrowUnauthorized_WhenPasswordInvalid() {
        LoginRequest request = new LoginRequest()
                .email("test@example.com")
                .password("wrong");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded_pass")).thenReturn(false);

        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("401")
                .hasMessageContaining("Invalid credentials");
    }

    @Test
    void authenticate_ShouldThrowUnauthorized_WhenUserNotFound() {
        LoginRequest request = new LoginRequest()
                .email("unknown@example.com")
                .password("pass");

        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("unknown@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("401")
                .hasMessageContaining("Invalid credentials");
    }

    @Test
    void register_ShouldCreateUserAndReturnToken() {
        RegisterRequest request = new RegisterRequest()
                .username("newuser")
                .email("new@example.com")
                .password("pass");

        when(userRepository.findByUsernameOrEmail("newuser", "new@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(jwtProvider.generateToken("newuser")).thenReturn("jwt-token");

        JwtResponse response = authService.register(request);

        assertThat(response.getToken()).isEqualTo("jwt-token");
        verify(userRepository).save(argThat(saved ->
                saved.getUsername().equals("newuser") &&
                        saved.getEmail().equals("new@example.com") &&
                        saved.getPasswordHash().equals("encoded")));
    }

    @Test
    void register_ShouldThrowConflict_WhenUsernameExists() {
        RegisterRequest request = new RegisterRequest()
                .username("testuser")
                .email("new@example.com")
                .password("pass");

        when(userRepository.findByUsernameOrEmail("testuser", "new@example.com"))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("409")
                .hasMessageContaining("Username already taken");
    }

    @Test
    void register_ShouldThrowConflict_WhenEmailExists() {
        RegisterRequest request = new RegisterRequest()
                .username("otheruser")
                .email("test@example.com")
                .password("pass");

        when(userRepository.findByUsernameOrEmail("otheruser", "test@example.com"))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("409")
                .hasMessageContaining("Email already in use");
    }
}