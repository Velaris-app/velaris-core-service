package com.velaris.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velaris.api.model.JwtResponse;
import com.velaris.api.model.LoginRequest;
import com.velaris.api.model.RegisterRequest;
import com.velaris.core.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password123");
        registerRequest.setEmail("testuser@example.com");

        String registerJson = objectMapper.writeValueAsString(registerRequest);

        String registerResponse = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtResponse jwtRegister = objectMapper.readValue(registerResponse, JwtResponse.class);
        assertThat(jwtRegister.getToken()).isNotEmpty();

        // 2️⃣ Login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("testuser@example.com");
        loginRequest.setPassword("password123");

        String loginJson = objectMapper.writeValueAsString(loginRequest);

        String loginResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtResponse jwtLogin = objectMapper.readValue(loginResponse, JwtResponse.class);
        assertThat(jwtLogin.getToken()).isNotEmpty();
    }
}