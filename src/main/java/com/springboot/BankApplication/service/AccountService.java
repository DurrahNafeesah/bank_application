package com.springboot.BankApplication.service;

import com.springboot.BankApplication.entity.Account;
import java.util.List;

public interface AccountService {

    Account createAccount(Account account);

    Account getAccountDetails(Long accountNumber);

    List<Account> getAllAcount();

    Account depositAmount(Long accountNumber, Double amount);

    Account withDrawAmount(Long accountNumber, Double amount);
}
