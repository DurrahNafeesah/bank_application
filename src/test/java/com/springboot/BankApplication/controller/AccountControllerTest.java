package com.springboot.BankApplication.controller;

import com.springboot.BankApplication.dto.AccountRequestDto;
import com.springboot.BankApplication.dto.AccountResponseDto;
import com.springboot.BankApplication.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest {
    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    AutoCloseable autoCloseable;
    AccountResponseDto responseDto1;
    AccountResponseDto responseDto2;
    List<AccountResponseDto> accounts;
    AccountRequestDto requestDto;
    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        mockMvc= MockMvcBuilders.standaloneSetup(accountController).build();

        responseDto1=new AccountResponseDto();
        responseDto1.setAccountNumber(1L);
        responseDto1.setAccountHolderName("Jack");
        responseDto1.setAccountBalance(1000D);

        responseDto2=new AccountResponseDto();
        responseDto2.setAccountNumber(2L);
        responseDto2.setAccountHolderName("Jhon");
        responseDto2.setAccountBalance(2000D);

        accounts= Arrays.asList(responseDto1,responseDto2);

        requestDto=new AccountRequestDto();
        requestDto.setAccountHolderName("Jack");
        requestDto.setAccountBalance(1000d);
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void createAccount() throws Exception{
        when(accountService.createAccount(requestDto)).thenReturn(responseDto1);

        this.mockMvc.perform(post("/account/create")
                .contentType("application/json")
                .content("""
                        {
                        "accountHolderName":"Jack",
                        "accountBalance":1000.0
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountHolderName").value("Jack"))
                .andExpect(jsonPath("$.accountBalance").value(1000.0));
    }

    @Test
    void getAccountDetails() throws Exception {
        when(accountService.getAccountDetailsById(1L)).thenReturn(responseDto1);

        this.mockMvc.perform(get("/account/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountHolderName").value("Jack"))
                .andExpect(jsonPath("$.accountNumber").value(1L))
                .andExpect(jsonPath("$.accountBalance").value(1000D));

    }

    @Test
    void getAllAccounts() throws Exception{
        when(accountService.getAllAccounts()).thenReturn(accounts);

        this.mockMvc.perform(get("/account/AllAccounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].accountHolderName").value("Jack"))
                .andExpect(jsonPath("$[1].accountHolderName").value("Jhon"));

    }

    @Test
    void depositAmount()throws Exception {
        double amount = 1000.0;
        Long accountNumber=1L;

        when(accountService.depositAmount(accountNumber,amount)).thenReturn(responseDto1);

        responseDto1.setAccountBalance(2000d);

        this.mockMvc.perform(put("/account/deposit/{accountNumber}",accountNumber)
                .param("amount",String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance").value(2000));
    }

    @Test
    void withDrawAmount()throws Exception {
        double amount = 500.0;
        Long accountNumber=1L;

        when(accountService.withDrawAmount(accountNumber,amount)).thenReturn(responseDto1);

        responseDto1.setAccountBalance(500d);

        this.mockMvc.perform(put("/account/withdraw/{accountNumber}",accountNumber)
                        .param("amount",String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance").value(500));
    }

    @Test
    void deleteAccount()throws Exception {
        this.mockMvc.perform(delete("/account/1"))
                .andExpect(status().isNoContent());
    }
}