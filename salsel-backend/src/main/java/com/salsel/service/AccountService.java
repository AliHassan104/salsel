package com.salsel.service;

import com.salsel.dto.AccountDto;

import java.util.List;

public interface AccountService {
    AccountDto save(AccountDto accountDto);
    List<AccountDto> getAll(Boolean status);
    AccountDto findById(Long id);
    void deleteById(Long id);
    void setToActiveById(Long id);
    AccountDto update(Long id, AccountDto accountDto);
}
