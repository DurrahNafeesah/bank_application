package com.springboot.BankApplication.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountResponseDto implements Serializable {
    private String accountNumber;
    private String accountHolderName;
    private Double accountBalance;
}
