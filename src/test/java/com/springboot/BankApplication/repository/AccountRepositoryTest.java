package com.springboot.BankApplication.repository;

import com.springboot.BankApplication.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountRepositoryTest {

    @Mock
    private AccountRepository accountRepository;

    private Account existingAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        existingAccount = new Account();
        existingAccount.setAccountNumber(1L);
        existingAccount.setAccountHolderName("Jack");
        existingAccount.setAccountBalance(1000d);
    }

    @Test
    void saveAccountTest() {
        Account newAccount = new Account();
        newAccount.setAccountHolderName("Jane");
        newAccount.setAccountBalance(2000d);

        when(accountRepository.save(newAccount)).thenReturn(newAccount);

        Account saved = accountRepository.save(newAccount);

        assertEquals("Jane", saved.getAccountHolderName());
        assertEquals(2000d, saved.getAccountBalance());
    }

    @Test
    void findByIdTest() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));

        Optional<Account> optionalAccount = accountRepository.findById(1L);

        assertTrue(optionalAccount.isPresent());
        assertEquals("Jack", optionalAccount.get().getAccountHolderName());
        assertEquals(1000d, optionalAccount.get().getAccountBalance());
    }
}
