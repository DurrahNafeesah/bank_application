package com.springboot.BankApplication.dto;

import lombok.Data;

@Data
public class AccountResponseDto {
    private String accountNumber;
    private String accountHolderName;
    private Double accountBalance;
}
