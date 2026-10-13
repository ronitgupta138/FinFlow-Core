package com.financeflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeflow.dto.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityAndJwtIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Protected endpoint must reject unauthenticated requests")
    void testProtectedEndpoint_WithoutToken_Forbidden() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Protected endpoint must reject malformed Bearer token")
    void testProtectedEndpoint_WithMalformedToken_Forbidden() throws Exception {
        mockMvc.perform(get("/api/transactions")
                        .header("Authorization", "Bearer invalid.jwt.token.payload"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Registration must reject duplicate email address")
    void testRegister_DuplicateEmail_BadRequest() throws Exception {
        String email = "dup." + UUID.randomUUID() + "@corp.io";
        RegisterRequest req = new RegisterRequest();
        req.setEmail(email);
        req.setPassword("Password123!");
        req.setFullName("Duplicate User");

        // 1. Initial registration
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // 2. Second registration with identical email
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Registration must reject invalid email format")
    void testRegister_InvalidEmail_BadRequest() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("not-an-email");
        req.setPassword("Password123!");
        req.setFullName("Bad Email User");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Public health endpoint must be accessible without authentication")
    void testPublicHealthEndpoint_PermitAll_Success() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
