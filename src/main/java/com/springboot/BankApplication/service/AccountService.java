package com.springboot.BankApplication.service;

import com.springboot.BankApplication.dto.AccountRequestDto;
import com.springboot.BankApplication.dto.AccountResponseDto;

import java.util.List;

public interface AccountService {

    AccountResponseDto createAccount(AccountRequestDto requestDto);

    AccountResponseDto getAccountDetailsById(Long accountNumber) ;

    List<AccountResponseDto> getAllAccounts() ;

    AccountResponseDto depositAmount(Long accountNumber, Double amount) ;

    AccountResponseDto withDrawAmount(Long accountNumber, Double amount) ;

    void deleteUser(Long accountNumber) ;
}
