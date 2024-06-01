package com.derysuwandi.restapisimplebanking.controller;

import com.derysuwandi.restapisimplebanking.dto.request.TransactionRequest;
import com.derysuwandi.restapisimplebanking.dto.response.*;
import com.derysuwandi.restapisimplebanking.service.transaction.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TransactionService transactionService;

    @Test
    public void givenTransactionRequest_whenDeposit_ReturnTransactionResponse() throws Exception {
        String accountName = "Koi";
        Integer accountNumber = 123456;
        double saldo = 50000;
        Long balance = 25000L;

        TransactionRequest request = new TransactionRequest();
        request.setBalance(balance);

        TransactionResponse expectedResponse = TransactionResponse.builder()
                .accountName(accountName)
                .accountNumber(accountNumber)
                .saldo(saldo)
                .build();

        // when
        when(transactionService.deposit(request)).thenReturn(expectedResponse);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("0000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.accountNumber").value(accountNumber))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.accountName").value(accountName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.saldo").isNumber());
    }


    @Test
    public void givenTransactionRequest_whenWithdraw_ReturnTransactionResponse() throws Exception {
        String accountName = "Koi";
        Integer accountNumber = 123456;
        double saldo = 50000;
        Long balance = 25000L;

        TransactionRequest request = new TransactionRequest();
        request.setBalance(balance);

        TransactionResponse expectedResponse = TransactionResponse.builder()
                .accountName(accountName)
                .accountNumber(accountNumber)
                .saldo(saldo)
                .build();

        // when
        when(transactionService.withdraw(request)).thenReturn(expectedResponse);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("0000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.accountNumber").value(accountNumber))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.accountName").value(accountName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.saldo").isNumber());
    }

}
