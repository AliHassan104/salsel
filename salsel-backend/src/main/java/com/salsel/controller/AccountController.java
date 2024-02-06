package com.salsel.controller;

import com.salsel.dto.AccountDto;
import com.salsel.service.AccountService;
import com.salsel.service.ExcelGenerationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.salsel.constants.ExcelConstants.ACCOUNT_TYPE;
import static com.salsel.constants.ExcelConstants.USER_TYPE;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;
    private final ExcelGenerationService excelGenerationService;

    public AccountController(AccountService accountService, ExcelGenerationService excelGenerationService) {
        this.accountService = accountService;
        this.excelGenerationService = excelGenerationService;
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

    @GetMapping("/account/logged-in-user")
    @PreAuthorize("hasAuthority('READ_ACCOUNT')")
    public ResponseEntity<List<AccountDto>> getAllAccountsByLoggedInUser(@RequestParam(value = "status") Boolean status) {
        List<AccountDto> accountDtoList = accountService.getAccountsByLoggedInUser(status);
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

    @GetMapping("/download-account-excel")
    public void downloadAccountsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=accounts.xlsx");

        List<AccountDto> accounts = accountService.getAccountsBetweenDates(startDate, endDate);
        List<Map<String, Object>> excelData = excelGenerationService.convertAccountsToExcelData(accounts);

        OutputStream outputStream = response.getOutputStream();
        excelGenerationService.createExcelFile(excelData, outputStream, ACCOUNT_TYPE);
        outputStream.close();
    }
}
