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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AnalyticsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;
    private Long categoryId;

    @BeforeEach
    void setUp() throws Exception {
        String email = "analytics." + UUID.randomUUID() + "@test.io";
        RegisterRequest regReq = new RegisterRequest();
        regReq.setEmail(email);
        regReq.setPassword("Password123!");
        regReq.setFullName("Analytics User");

        MvcResult regRes = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regReq)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(regRes.getResponse().getContentAsString());
        userToken = root.path("data").path("token").asText();

        CategoryRequest catReq = new CategoryRequest();
        catReq.setName("Salary & Freelance");
        catReq.setColor("#0891b2");
        MvcResult catRes = mockMvc.perform(post("/api/categories")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(catReq)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode catRoot = objectMapper.readTree(catRes.getResponse().getContentAsString());
        categoryId = catRoot.path("data").path("id").asLong();

        // Add 1 INCOME transaction
        TransactionRequest incReq = new TransactionRequest();
        incReq.setAmount(BigDecimal.valueOf(2000.00));
        incReq.setType(TransactionType.INCOME);
        incReq.setDescription("Project Stipend");
        incReq.setTransactionDate(LocalDate.now());
        incReq.setCategoryId(categoryId);

        mockMvc.perform(post("/api/transactions")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incReq)))
                .andExpect(status().isCreated());

        // Add 1 EXPENSE transaction
        TransactionRequest expReq = new TransactionRequest();
        expReq.setAmount(BigDecimal.valueOf(500.00));
        expReq.setType(TransactionType.EXPENSE);
        expReq.setDescription("Cloud Server");
        expReq.setTransactionDate(LocalDate.now());
        expReq.setCategoryId(categoryId);

        mockMvc.perform(post("/api/transactions")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expReq)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Monthly summary correctly aggregates income, expenses, and net savings")
    void testGetMonthlySummary_CalculatesAggregates() throws Exception {
        LocalDate now = LocalDate.now();
        mockMvc.perform(get("/api/analytics/monthly-summary")
                        .header("Authorization", "Bearer " + userToken)
                        .param("year", String.valueOf(now.getYear()))
                        .param("month", String.valueOf(now.getMonthValue())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalIncome").value(2000.00))
                .andExpect(jsonPath("$.data.totalExpense").value(500.00))
                .andExpect(jsonPath("$.data.netSavings").value(1500.00));
    }
}
