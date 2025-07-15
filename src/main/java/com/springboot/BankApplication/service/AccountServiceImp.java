package com.springboot.BankApplication.service;

import com.springboot.BankApplication.entity.Account;
import com.springboot.BankApplication.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImp implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountDetails(Long accountNumber) {
        return accountRepository.findById(accountNumber).orElse(null);
    }

    @Override
    public List<Account> getAllAcount() {
        return accountRepository.findAll();
    }

    @Override
    public Account depositAmount(Long accountNumber, Double amount) {
        Optional<Account> optionalAccount = accountRepository.findById(accountNumber);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setAccount_balance(account.getAccount_balance() + amount);
            return accountRepository.save(account);
        }
        return null;
    }

    @Override
    public Account withDrawAmount(Long accountNumber, Double amount) {
        Optional<Account> optionalAccount = accountRepository.findById(accountNumber);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (account.getAccount_balance() >= amount) {
                account.setAccount_balance(account.getAccount_balance() - amount);
                return accountRepository.save(account);
            }
        }
        return null;
    }

    @Override
    public void deletUser(Long accountNumber) {
        accountRepository.deleteById(accountNumber);
    }
}
