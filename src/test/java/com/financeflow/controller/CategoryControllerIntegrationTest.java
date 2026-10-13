package com.financeflow.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeflow.dto.CategoryRequest;
import com.financeflow.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;

    @BeforeEach
    void setUp() throws Exception {
        String uniqueEmail = "user." + UUID.randomUUID() + "@test.io";
        RegisterRequest regReq = new RegisterRequest();
        regReq.setEmail(uniqueEmail);
        regReq.setPassword("Password123!");
        regReq.setFullName("Test User");

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regReq)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        userToken = root.path("data").path("token").asText();
    }

    @Test
    @DisplayName("Should successfully create category with valid request")
    void testCreateCategory_Success() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Groceries");
        request.setColor("#10b981");
        request.setMonthlyBudget(BigDecimal.valueOf(400.00));

        mockMvc.perform(post("/api/categories")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Groceries"))
                .andExpect(jsonPath("$.data.color").value("#10b981"));
    }

    @Test
    @DisplayName("Should reject duplicate category name for the same user")
    void testCreateCategory_DuplicateName_BadRequest() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Utilities");
        request.setColor("#3b82f6");

        // 1. First creation
        mockMvc.perform(post("/api/categories")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // 2. Duplicate attempt
        mockMvc.perform(post("/api/categories")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Should reject blank category name with 400 Bad Request")
    void testCreateCategory_BlankName_BadRequest() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("");

        mockMvc.perform(post("/api/categories")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Should retrieve all categories belonging to authenticated user")
    void testGetAllCategories_Success() throws Exception {
        CategoryRequest c1 = new CategoryRequest();
        c1.setName("Transport");
        mockMvc.perform(post("/api/categories")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c1)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/categories")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("Should return 404 when querying nonexistent category ID")
    void testGetCategoryById_NotFound() throws Exception {
        mockMvc.perform(get("/api/categories/999999")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should successfully delete category by ID")
    void testDeleteCategory_Success() throws Exception {
        CategoryRequest c = new CategoryRequest();
        c.setName("Temporary");
        MvcResult res = mockMvc.perform(post("/api/categories")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(res.getResponse().getContentAsString());
        long categoryId = root.path("data").path("id").asLong();

        mockMvc.perform(delete("/api/categories/" + categoryId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
