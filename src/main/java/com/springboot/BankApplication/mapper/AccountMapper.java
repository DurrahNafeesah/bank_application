package com.springboot.BankApplication.mapper;

import com.springboot.BankApplication.dto.AccountRequestDto;
import com.springboot.BankApplication.dto.AccountResponseDto;
import com.springboot.BankApplication.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
    public interface AccountMapper {
        Account toEntity(AccountRequestDto dto);
        AccountResponseDto toResponseDto(Account entity);
    }


