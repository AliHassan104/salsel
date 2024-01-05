package com.salsel.controller;

import com.salsel.dto.AccountDto;
import com.salsel.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/accountt")
    @PreAuthorize("hasAuthority('CREATE_ACCOUNT') and hasAuthority('READ_ACCOUNT')")
    public ResponseEntity<AccountDto> createAccount(@RequestPart AccountDto accountDto,
                                                    @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(accountService.save(accountDto, file));
    }


    @GetMapping("/account")
    @PreAuthorize("hasAuthority('READ_ACCOUNT')")
    public ResponseEntity<List<AccountDto>> getAllAccounts(@RequestParam(value = "status") Boolean status) {
        List<AccountDto> accountDtoList = accountService.getAll(status);
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

    @PutMapping("/account/status/{id}")
    @PreAuthorize("hasAuthority('CREATE_ACCOUNT') and hasAuthority('READ_ACCOUNT')")
    public ResponseEntity<Void> updateAccountStatusToActive(@PathVariable Long id) {
        accountService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/account/{id}")
    @PreAuthorize("hasAuthority('CREATE_ACCOUNT') and hasAuthority('READ_ACCOUNT')")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id, @RequestPart AccountDto accountDto,
                                                    @RequestPart("file") MultipartFile file) {
        AccountDto updatedAccountDto = accountService.update(id, accountDto, file);
        return ResponseEntity.ok(updatedAccountDto);
    }
}
