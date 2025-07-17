package com.springboot.BankApplication.service;

import com.springboot.BankApplication.dto.AccountRequestDto;
import com.springboot.BankApplication.dto.AccountResponseDto;
import com.springboot.BankApplication.entity.Account;
import com.springboot.BankApplication.exception.ResourceNotFoundException;
import com.springboot.BankApplication.mapper.AccountMapper;
import com.springboot.BankApplication.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImp implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public AccountResponseDto createAccount(AccountRequestDto requestDto) {
        Account account = accountMapper.toEntity(requestDto);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toResponseDto(savedAccount);
    }

    @Override
    public AccountResponseDto getAccountDetailsById(Long accountNumber) {
        return accountRepository.findById(accountNumber)
                .map(accountMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountNumber));
    }

    @Override
    public List<AccountResponseDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("No accounts found.");
        }
        return accounts.stream()
                .map(accountMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponseDto depositAmount(Long accountNumber, Double amount) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountNumber));
        account.setAccountBalance(account.getAccountBalance() + amount);
        return accountMapper.toResponseDto(accountRepository.save(account));
    }

    @Override
    public AccountResponseDto withDrawAmount(Long accountNumber, Double amount) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountNumber));

        if (account.getAccountBalance() < amount) {
            throw new RuntimeException("Insufficient balance in account ID: " + accountNumber);
        }

        account.setAccountBalance(account.getAccountBalance() - amount);
        return accountMapper.toResponseDto(accountRepository.save(account));
    }

    @Override
    public void deleteUser(Long accountNumber) {
        if (!accountRepository.existsById(accountNumber)) {
            throw new ResourceNotFoundException("Account not found for deletion with ID: " + accountNumber);
        }
        accountRepository.deleteById(accountNumber);
    }
}
