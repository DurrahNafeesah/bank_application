package com.springboot.BankApplication.service;

import com.springboot.BankApplication.dto.AccountRequestDto;
import com.springboot.BankApplication.dto.AccountResponseDto;
import com.springboot.BankApplication.entity.Account;
import com.springboot.BankApplication.exception.ResourceNotFoundException;
import com.springboot.BankApplication.mapper.AccountMapper;
import com.springboot.BankApplication.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AccountServiceImpTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    AutoCloseable autoCloseable;
    Account existingAccount;

    @InjectMocks
    private AccountServiceImp accountServiceImp;

    AccountResponseDto responseDto;
    AccountRequestDto requestDto;
    Account accountEntity;
    Account savedAccount;
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        existingAccount = new Account(1L, "Jack", 1000d);

        responseDto = new AccountResponseDto();
        responseDto.setAccountNumber(1L);
        responseDto.setAccountHolderName("Jack");
        responseDto.setAccountBalance(1000d);

        requestDto = new AccountRequestDto();
        requestDto.setAccountHolderName("Jack");
        requestDto.setAccountBalance(1000d);

        accountEntity = new Account(null, "Jack", 1000d);
        savedAccount = new Account(1L, "Jack", 1000d);
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void createAccount() {
        when(accountMapper.toEntity(requestDto)).thenReturn(accountEntity);
        when(accountRepository.save(accountEntity)).thenReturn(savedAccount);
        when(accountMapper.toResponseDto(savedAccount)).thenReturn(responseDto);

        AccountResponseDto actual=accountServiceImp.createAccount(requestDto);

        assertThat(actual.getAccountHolderName()).isEqualTo("Jack");
        assertThat(actual.getAccountNumber()).isEqualTo(1L);
        assertThat(actual.getAccountBalance()).isEqualTo(1000d);
    }

    @Test
    void getAccountDetailsById() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));

        when(accountMapper.toResponseDto(existingAccount)).thenReturn(responseDto);

        AccountResponseDto actual = accountServiceImp.getAccountDetailsById(1L);

        assertThat(actual.getAccountHolderName()).isEqualTo("Jack");
        assertThat(actual.getAccountBalance()).isEqualTo(1000d);
        assertThat(actual.getAccountNumber()).isEqualTo(1L);
    }


    @Test
    void getAllAccounts() {
        when(accountRepository.findAll()).thenReturn(Collections.singletonList(existingAccount));
        when(accountMapper.toResponseDto(existingAccount)).thenReturn(responseDto);

        List<AccountResponseDto> actualList = accountServiceImp.getAllAccounts();

        assertThat(actualList).hasSize(1);
        assertThat(actualList.get(0).getAccountHolderName()).isEqualTo("Jack");
        assertThat(actualList.get(0).getAccountBalance()).isEqualTo(1000d);
        assertThat(actualList.get(0).getAccountNumber()).isEqualTo(1L);
    }

    @Test
    void depositAmount() {
        Long accountNumber=1L;
        double amount=5000d;

        when(accountRepository.findById(accountNumber)).thenReturn(Optional.ofNullable(existingAccount));

        Account updatedAccount=new Account(1l, "Jack", existingAccount.getAccountBalance() + amount);
        when(accountRepository.save(existingAccount)).thenReturn(updatedAccount);

        responseDto.setAccountBalance(6000d);

        when(accountMapper.toResponseDto(updatedAccount)).thenReturn(responseDto);

        AccountResponseDto actual=accountServiceImp.depositAmount(accountNumber,amount);

        assertThat(actual.getAccountHolderName()).isEqualTo("Jack");
        assertThat(actual.getAccountBalance()).isEqualTo(6000d);
        assertThat(actual.getAccountNumber()).isEqualTo(1L);

    }

    @Test
    void withDrawAmount() {
        Long accountNumber=1L;
        double amount=500d;

        when(accountRepository.findById(accountNumber)).thenReturn(Optional.ofNullable(existingAccount));

        Account updatedAccount=new Account(1l, "Jack", existingAccount.getAccountBalance() - amount);
        when(accountRepository.save(existingAccount)).thenReturn(updatedAccount);

        responseDto.setAccountBalance(500d);

        when(accountMapper.toResponseDto(updatedAccount)).thenReturn(responseDto);

        AccountResponseDto actual=accountServiceImp.withDrawAmount(accountNumber,amount);

        assertThat(actual.getAccountHolderName()).isEqualTo("Jack");
        assertThat(actual.getAccountBalance()).isEqualTo(500d);
        assertThat(actual.getAccountNumber()).isEqualTo(1L);
    }

    @Test
    void deleteUser_whenAccountNumberExist() {
        Long accountNumber=1L;

        when(accountRepository.existsById(accountNumber)).thenReturn(true);

        accountServiceImp.deleteUser(accountNumber);

        verify(accountRepository).deleteById(accountNumber);


    }
    @Test
    void deleteUser_whenAccountNumberDoesNotExist() {
        Long accountNumber=1L;

        when(accountRepository.existsById(accountNumber)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,()->{
            accountServiceImp.deleteUser(accountNumber);
        });


        verify(accountRepository,never()).deleteById(accountNumber);


    }
}