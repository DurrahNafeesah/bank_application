package com.springboot.BankApplication.controller;

import com.springboot.BankApplication.dto.AccountRequestDto;
import com.springboot.BankApplication.dto.AccountResponseDto;
import com.springboot.BankApplication.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@Tag(name="Account APIs",description = "creat,get,update(withdraw,deposit),delete account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    @Operation(summary = "create the account")
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto requestDto) {
        return ResponseEntity.ok(accountService.createAccount(requestDto));
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get account by Id")
    public ResponseEntity<AccountResponseDto> getAccountDetails(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getAccountDetailsById(accountId));
    }

    @GetMapping("/AllAccounts")
    @Operation(summary = "get all account")
    public ResponseEntity<List<AccountResponseDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PutMapping("/deposit/{accountId}")
    @Operation(summary = "deposit the amount by account ID")
    public ResponseEntity<AccountResponseDto> depositAmount(@PathVariable Long accountId, @RequestParam Double amount) {
       return ResponseEntity.ok(accountService.depositAmount(accountId,amount));
    }

    @PutMapping("/withdraw/{accountId}")
    @Operation(summary = "withdraw the amount by account ID")
    public ResponseEntity<AccountResponseDto> withDrawAmount(@PathVariable Long accountId, @RequestParam Double amount) {
        return ResponseEntity.ok(accountService.withDrawAmount(accountId,amount));
    }

    @DeleteMapping("/{accountId}")
    @Operation(summary = "delete the account by ID")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteUser(accountId);
        return ResponseEntity.noContent().build();
    }
}
