package com.velaris.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velaris.api.model.GrantType;
import com.velaris.api.model.RegisterRequest;
import com.velaris.api.model.TokenRequest;
import com.velaris.api.model.TokenResponse;
import com.velaris.core.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterAndLoginFlow() throws Exception {
        // 1️⃣ Register
        TokenResponse registerResp = registerUser("testuser", "testuser@example.com", "password123");

        assertThat(registerResp.getAccessToken()).isNotEmpty();
        assertThat(registerResp.getRefreshToken()).isNotEmpty();
        assertThat(registerResp.getSessionState()).isInstanceOf(UUID.class);

        // 2️⃣ Login
        TokenResponse loginResp = loginUser("testuser@example.com", "password123");

        assertThat(loginResp.getAccessToken()).isNotEmpty();
        assertThat(loginResp.getRefreshToken()).isNotEmpty();
        assertThat(loginResp.getSessionState()).isInstanceOf(UUID.class);
    }

    // Helper: register user
    private TokenResponse registerUser(String username, String email, String password) throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setEmail(email);
        request.setPassword(password);

        String responseJson = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(responseJson, TokenResponse.class);
    }

    // Helper: login user
    private TokenResponse loginUser(String usernameOrEmail, String password) throws Exception {
        TokenRequest request = new TokenRequest();
        request.setUsername(usernameOrEmail);
        request.setPassword(password);
        request.setGrantType(GrantType.PASSWORD);

        String responseJson = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(responseJson, TokenResponse.class);
    }

    @Test
    void testRegisterLoginAndRefreshFlow() throws Exception {
        // 1️⃣ Register
        TokenResponse registerResp = registerUser("testuser", "testuser@example.com", "password123");

        assertThat(registerResp.getAccessToken()).isNotEmpty();
        assertThat(registerResp.getRefreshToken()).isNotEmpty();

        // 2️⃣ Login
        TokenResponse loginResp = loginUser("testuser@example.com", "password123");

        assertThat(loginResp.getAccessToken()).isNotEmpty();
        assertThat(loginResp.getRefreshToken()).isNotEmpty();

        // 3️⃣ Refresh token using previous session
        TokenRequest refreshRequest = new TokenRequest();
        refreshRequest.setGrantType(GrantType.REFRESH_TOKEN);
        refreshRequest.setRefreshToken(loginResp.getRefreshToken());

        String refreshJson = objectMapper.writeValueAsString(refreshRequest);

        String refreshResponseJson = mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TokenResponse refreshResp = objectMapper.readValue(refreshResponseJson, TokenResponse.class);
        assertThat(refreshResp.getAccessToken()).isNotEmpty();
        assertThat(refreshResp.getRefreshToken()).isNotEmpty();
        assertThat(refreshResp.getSessionState()).isInstanceOf(UUID.class);
        assertThat(refreshResp.getSessionState()).isNotEqualTo(loginResp.getSessionState()); // nowa sesja
    }
}