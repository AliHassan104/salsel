package com.salsel.service;

import com.salsel.dto.AccountDto;
import com.salsel.dto.TicketDto;
import com.salsel.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AccountService {
    AccountDto save(AccountDto accountDto, MultipartFile pdf);
    List<AccountDto> getAll(Boolean status);
    List<Map<String,String>> getAccountNumberWithCustomerName(Boolean status);
    List<AccountDto> getAccountsByLoggedInUser(Boolean status);
    AccountDto findById(Long id);
    void deleteById(Long id);
    void setToActiveById(Long id);
    AccountDto update(Long id, AccountDto accountDto,  MultipartFile pdf);
    List<AccountDto> getAccountsBetweenDates(LocalDate startDate, LocalDate endDate);

    LocalDate getMinCreatedAt();
    LocalDate getMaxCreatedAt();

    Map<String, Long> getStatusCounts();

    Map<String, Long> getStatusCountsBasedOnLoggedInUser();

    List<Long> getAccountNumbersByStatus(Boolean status);


}
