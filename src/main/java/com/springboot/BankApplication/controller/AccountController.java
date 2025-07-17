package com.springboot.BankApplication.controller;

import com.springboot.BankApplication.dto.AccountRequestDto;
import com.springboot.BankApplication.dto.AccountResponseDto;
import com.springboot.BankApplication.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto requestDto) {
        return ResponseEntity.ok(accountService.createAccount(requestDto));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> getAccountDetails(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getAccountDetailsById(accountId));
    }

    @GetMapping("/AllAccounts")
    public ResponseEntity<List<AccountResponseDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PutMapping("/deposit/{accountId}")
    public ResponseEntity<AccountResponseDto> depositAmount(@PathVariable Long accountId, @RequestParam Double amount) {
       return ResponseEntity.ok(accountService.depositAmount(accountId,amount));
    }

    @PutMapping("/withdraw/{accountId}")
    public ResponseEntity<AccountResponseDto> withDrawAmount(@PathVariable Long accountId, @RequestParam Double amount) {
        return ResponseEntity.ok(accountService.withDrawAmount(accountId,amount));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteUser(accountId);
        return ResponseEntity.noContent().build();
    }
}
