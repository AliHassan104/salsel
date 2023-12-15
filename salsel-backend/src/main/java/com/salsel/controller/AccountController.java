package com.salsel.controller;

import com.salsel.dto.AccountDto;
import com.salsel.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account")
    @PreAuthorize("hasAuthority('CREATE_ACCOUNT') and hasAuthority('READ_ACCOUNT')")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(accountService.save(accountDto));
    }

    @GetMapping("/account")
    @PreAuthorize("hasAuthority('READ_ACCOUNT')")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accountDtoList = accountService.getAll();
        return ResponseEntity.ok(accountDtoList);
    }

    @GetMapping("/account/{id}")
    @PreAuthorize("hasAuthority('READ_ACCOUNT')")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        AccountDto accountDto = accountService.findById(id);
        return ResponseEntity.ok(accountDto);
    }

    @DeleteMapping("/account/{id}")
    @PreAuthorize("hasAuthority('DELETE_ACCOUNT') and hasAuthority('READ_ACCOUNT')")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/account/{id}")
    @PreAuthorize("hasAuthority('CREATE_ACCOUNT') and hasAuthority('READ_ACCOUNT')")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        AccountDto updatedAccountDto = accountService.update(id, accountDto);
        return ResponseEntity.ok(updatedAccountDto);
    }
}
