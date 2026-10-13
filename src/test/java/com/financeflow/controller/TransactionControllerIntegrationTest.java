package com.financeflow.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeflow.dto.CategoryRequest;
import com.financeflow.dto.RegisterRequest;
import com.financeflow.dto.TransactionRequest;
import com.financeflow.entity.TransactionType;
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
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;
    private Long categoryId;

    @BeforeEach
    void setUp() throws Exception {
        String uniqueEmail = "user." + UUID.randomUUID() + "@test.io";
        RegisterRequest regReq = new RegisterRequest();
        regReq.setEmail(uniqueEmail);
        regReq.setPassword("Password123!");
        regReq.setFullName("Tx Test User");

        MvcResult regRes = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regReq)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(regRes.getResponse().getContentAsString());
        userToken = root.path("data").path("token").asText();

        // Create default category
        CategoryRequest catReq = new CategoryRequest();
        catReq.setName("Living");
        MvcResult catRes = mockMvc.perform(post("/api/categories")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(catReq)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode catRoot = objectMapper.readTree(catRes.getResponse().getContentAsString());
        categoryId = catRoot.path("data").path("id").asLong();
    }

    @Test
    @DisplayName("Should successfully create transaction with valid payload")
    void testCreateTransaction_Success() throws Exception {
        TransactionRequest req = new TransactionRequest();
        req.setAmount(BigDecimal.valueOf(120.50));
        req.setType(TransactionType.EXPENSE);
        req.setDescription("Monthly Internet Bill");
        req.setTransactionDate(LocalDate.now());
        req.setCategoryId(categoryId);

        mockMvc.perform(post("/api/transactions")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.amount").value(120.50))
                .andExpect(jsonPath("$.data.description").value("Monthly Internet Bill"));
    }

    @Test
    @DisplayName("Should reject transaction with negative or zero amount")
    void testCreateTransaction_InvalidAmount_BadRequest() throws Exception {
        TransactionRequest req = new TransactionRequest();
        req.setAmount(BigDecimal.valueOf(-15.00));
        req.setType(TransactionType.EXPENSE);
        req.setDescription("Invalid Transaction");
        req.setTransactionDate(LocalDate.now());
        req.setCategoryId(categoryId);

        mockMvc.perform(post("/api/transactions")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Should reject transaction when category does not exist")
    void testCreateTransaction_NonexistentCategory_NotFound() throws Exception {
        TransactionRequest req = new TransactionRequest();
        req.setAmount(BigDecimal.valueOf(50.00));
        req.setType(TransactionType.EXPENSE);
        req.setDescription("Nonexistent category");
        req.setTransactionDate(LocalDate.now());
        req.setCategoryId(999999L);

        mockMvc.perform(post("/api/transactions")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should fetch all transactions with date range filter")
    void testGetAllTransactions_WithDateFilter_Success() throws Exception {
        TransactionRequest req = new TransactionRequest();
        req.setAmount(BigDecimal.valueOf(80.00));
        req.setType(TransactionType.EXPENSE);
        req.setDescription("Electricity");
        req.setTransactionDate(LocalDate.now());
        req.setCategoryId(categoryId);

        mockMvc.perform(post("/api/transactions")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/transactions")
                        .header("Authorization", "Bearer " + userToken)
                        .param("startDate", LocalDate.now().minusDays(1).toString())
                        .param("endDate", LocalDate.now().plusDays(1).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("Should return 404 when querying nonexistent transaction ID")
    void testGetTransactionById_NotFound() throws Exception {
        mockMvc.perform(get("/api/transactions/999999")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should delete existing transaction successfully")
    void testDeleteTransaction_Success() throws Exception {
        TransactionRequest req = new TransactionRequest();
        req.setAmount(BigDecimal.valueOf(25.00));
        req.setType(TransactionType.EXPENSE);
        req.setDescription("Coffee");
        req.setTransactionDate(LocalDate.now());
        req.setCategoryId(categoryId);

        MvcResult res = mockMvc.perform(post("/api/transactions")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(res.getResponse().getContentAsString());
        long txId = root.path("data").path("id").asLong();

        mockMvc.perform(delete("/api/transactions/" + txId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
