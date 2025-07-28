package com.springboot.BankApplication.service;

import com.springboot.BankApplication.dto.AccountRequestDto;
import com.springboot.BankApplication.dto.AccountResponseDto;
import com.springboot.BankApplication.entity.Account;
import com.springboot.BankApplication.exception.ResourceNotFoundException;
import com.springboot.BankApplication.mapper.AccountMapper;
import com.springboot.BankApplication.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImp implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public AccountResponseDto createAccount(AccountRequestDto requestDto) {
        Account account = accountMapper.toEntity(requestDto);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toResponseDto(savedAccount);
    }

    @Override
    @Cacheable("customers")
    public AccountResponseDto getAccountDetailsById(Long accountNumber) {
        System.out.println("Fetching from DB - not cache");
        return accountRepository.findById(accountNumber)
                .map(accountMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountNumber));
    }

    @Override
    @Cacheable("customers")
    public List<AccountResponseDto> getAllAccounts() {
        System.out.println("Fetching from DB - not cache");
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("No accounts found.");
        }
        return accounts.stream()
                .map(accountMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @CachePut(value="customers",key="#accountNumber")
    public AccountResponseDto depositAmount(Long accountNumber, Double amount) {
        System.out.println("Updating cache for account: " + accountNumber);
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountNumber));
        account.setAccountBalance(account.getAccountBalance() + amount);
        return accountMapper.toResponseDto(accountRepository.save(account));
    }

    @Override
    @CachePut(value="customers",key="#accountNumber")
    public AccountResponseDto withDrawAmount(Long accountNumber, Double amount) {
        System.out.println("Updating cache for account: " + accountNumber);
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountNumber));

        if (account.getAccountBalance() < amount) {
            throw new RuntimeException("Insufficient balance in account ID: " + accountNumber);
        }

        account.setAccountBalance(account.getAccountBalance() - amount);
        return accountMapper.toResponseDto(accountRepository.save(account));
    }

    @Override
    @CacheEvict(value="customers",allEntries = true)
    public void deleteUser(Long accountNumber) {
        System.out.println("Cache cleared after account deletion: " + accountNumber);
        if (!accountRepository.existsById(accountNumber)) {
            throw new ResourceNotFoundException("Account not found for deletion with ID: " + accountNumber);
        }
        accountRepository.deleteById(accountNumber);
    }
}
