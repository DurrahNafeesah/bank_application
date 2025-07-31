package com.springboot.BankApplication.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountRequestDto implements Serializable {
    private  String accountHolderName;
    private  Double accountBalance;
}
