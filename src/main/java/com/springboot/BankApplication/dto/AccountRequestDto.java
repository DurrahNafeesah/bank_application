package com.springboot.BankApplication.dto;

import lombok.Data;

@Data
public class AccountRequestDto {
    private  String accountHolderName;
    private  Double accountBalance;
}
