package com.derysuwandi.restapisimplebanking.controller;

import com.derysuwandi.restapisimplebanking.dto.request.AccountRegisterRequest;
import com.derysuwandi.restapisimplebanking.dto.request.LoginRequest;
import com.derysuwandi.restapisimplebanking.dto.response.AccountDetailResponse;
import com.derysuwandi.restapisimplebanking.dto.response.AccountRegisterResponse;
import com.derysuwandi.restapisimplebanking.dto.response.LoginResponse;
import com.derysuwandi.restapisimplebanking.dto.response.TransactionDetailResponse;
import com.derysuwandi.restapisimplebanking.service.account.AccountService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountService accountService;

    @Test
    public void givenLoginRequest_whenRegister_ReturnLoginResponse() throws Exception {
        String email = "test123@gmail.com";
        String password = "123123123";
        String confirmPassword = "123123123";
        String accountName = "Koi";
        Integer accountNumber = 123456;
        Integer balance = 500000;

        AccountRegisterRequest request = AccountRegisterRequest.builder()
                .accountName(accountName)
                .email(email)
                .password(password)
                .confirmPassword(confirmPassword)
                .build();

        AccountRegisterResponse expectedResponse = AccountRegisterResponse.builder()
                .accountName(accountName)
                .accountNumber(accountNumber)
                .email(email)
                .balance(balance)
                .build();

        // when
        when(accountService.accountRegister(request)).thenReturn(expectedResponse);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("0000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.accountNumber").value(accountNumber))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.accountName").value(accountName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.balance").isNumber());
    }

    @Test
    public void givenLoginRequest_whenLogin_ReturnLoginResponse() throws Exception {
        String email = "test@gmail.com";
        String password = "123123123";
        String token = "123123123";
        String accountName = "Koi";
        Integer accountNumber = 123456;

        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        LoginResponse loginResponse = LoginResponse.builder()
                .email(email)
                .accountNumber(accountNumber)
                .accountName(accountName)
                .token(token)
                .build();

        // when
        when(accountService.login(request)).thenReturn(loginResponse);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("0000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.accountNumber").value(accountNumber))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.accountName").value(accountName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").value(token));
    }

    @Test
    public void whenGetMyBalance_ReturnAccountDetailResponse() throws Exception {
        String accountName = "Koi";
        Integer accountNumber = 123456;
        Integer balance = 500000;
        List<TransactionDetailResponse> transactionList = new ArrayList<>();

        AccountDetailResponse accountDetailResponse = AccountDetailResponse.builder()
                .accountNumber(accountNumber)
                .accountName(accountName)
                .balance(balance)
                .transaction(transactionList)
                .build();

        // when
        when(accountService.accountDetail()).thenReturn(accountDetailResponse);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/account/my-balance")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", ""))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("0000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.accountNumber").value(accountNumber))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.accountName").value(accountName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.balance").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.transaction").isArray());
    }


}
