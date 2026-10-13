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
public class UserIsolationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String userAToken;
    private String userBToken;
    private Long userACategoryId;
    private Long userATransactionId;

    @BeforeEach
    void setUp() throws Exception {
        userAToken = registerUserAndGetToken("alice." + UUID.randomUUID() + "@corp.io");
        userBToken = registerUserAndGetToken("bob." + UUID.randomUUID() + "@corp.io");

        // User A creates Category
        CategoryRequest catReq = new CategoryRequest();
        catReq.setName("Alice Personal");
        MvcResult catRes = mockMvc.perform(post("/api/categories")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(catReq)))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode catNode = objectMapper.readTree(catRes.getResponse().getContentAsString());
        userACategoryId = catNode.path("data").path("id").asLong();

        // User A creates Transaction
        TransactionRequest txReq = new TransactionRequest();
        txReq.setAmount(BigDecimal.valueOf(99.00));
        txReq.setType(TransactionType.EXPENSE);
        txReq.setDescription("Alice Private Expense");
        txReq.setTransactionDate(LocalDate.now());
        txReq.setCategoryId(userACategoryId);

        MvcResult txRes = mockMvc.perform(post("/api/transactions")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(txReq)))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode txNode = objectMapper.readTree(txRes.getResponse().getContentAsString());
        userATransactionId = txNode.path("data").path("id").asLong();
    }

    private String registerUserAndGetToken(String email) throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setEmail(email);
        req.setPassword("Password123!");
        req.setFullName("User " + email);

        MvcResult res = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(res.getResponse().getContentAsString());
        return root.path("data").path("token").asText();
    }

    @Test
    @DisplayName("User B cannot fetch User A's transaction by ID (must return 404)")
    void testUserIsolation_UserBCannotFetchUserATransaction() throws Exception {
        mockMvc.perform(get("/api/transactions/" + userATransactionId)
                        .header("Authorization", "Bearer " + userBToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("User B cannot delete User A's transaction (must return 404)")
    void testUserIsolation_UserBCannotDeleteUserATransaction() throws Exception {
        mockMvc.perform(delete("/api/transactions/" + userATransactionId)
                        .header("Authorization", "Bearer " + userBToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        // Verify User A can still fetch the transaction (it was not deleted)
        mockMvc.perform(get("/api/transactions/" + userATransactionId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(userATransactionId));
    }

    @Test
    @DisplayName("User B cannot fetch User A's category by ID (must return 404)")
    void testUserIsolation_UserBCannotFetchUserACategory() throws Exception {
        mockMvc.perform(get("/api/categories/" + userACategoryId)
                        .header("Authorization", "Bearer " + userBToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("User B cannot delete User A's category (must return 404)")
    void testUserIsolation_UserBCannotDeleteUserACategory() throws Exception {
        mockMvc.perform(delete("/api/categories/" + userACategoryId)
                        .header("Authorization", "Bearer " + userBToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("User B cannot create transaction referencing User A's category ID")
    void testUserIsolation_UserBCannotUseUserACategoryForTransaction() throws Exception {
        TransactionRequest req = new TransactionRequest();
        req.setAmount(BigDecimal.valueOf(15.00));
        req.setType(TransactionType.EXPENSE);
        req.setDescription("Cross-Tenant Attempt");
        req.setTransactionDate(LocalDate.now());
        req.setCategoryId(userACategoryId); // Alice's category

        mockMvc.perform(post("/api/transactions")
                        .header("Authorization", "Bearer " + userBToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
