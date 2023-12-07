package com.salsel.service.impl;

import com.salsel.dto.AccountDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Account;
import com.salsel.repository.AccountRepository;
import com.salsel.service.AccountService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public AccountDto save(AccountDto accountDto) {
        Account account = toEntity(accountDto);
        account.setStatus(true);
        Account createdAccount = accountRepository.save(account);
        return toDto(createdAccount);
    }

    @Override
    public List<AccountDto> getAll(Boolean status) {
        List<Account> accounts = accountRepository.findAllInDesOrderByIdAndStatus(status);
        List<AccountDto> accountDtoList = new ArrayList<>();

        for (Account account : accounts) {
            AccountDto accountDto = toDto(account);
            accountDtoList.add(accountDto);
        }
        return accountDtoList;
    }

    @Override
    public AccountDto findById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Account not found for id => %d", id)));
        return toDto(account);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Account not found for id => %d", id)));
        accountRepository.setStatusInactive(account.getId());
    }

    @Override
    @Transactional
    public void setToActiveById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Account not found for id => %d", id)));
        accountRepository.setStatusActive(account.getId());
    }

    @Override
    @Transactional
    public AccountDto update(Long id, AccountDto accountDto) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Account not found for id => %d", id)));

        existingAccount.setAccountType(accountDto.getAccountType());
        existingAccount.setBusinessActivity(accountDto.getBusinessActivity());
        existingAccount.setAccountNumber(accountDto.getAccountNumber());
        existingAccount.setCustomerName(accountDto.getCustomerName());
        existingAccount.setProjectName(accountDto.getProjectName());
        existingAccount.setTradeLicenseNo(accountDto.getTradeLicenseNo());
        existingAccount.setTaxDocumentNo(accountDto.getTaxDocumentNo());
        existingAccount.setCounty(accountDto.getCounty());
        existingAccount.setCity(accountDto.getCity());
        existingAccount.setAddress(accountDto.getAddress());
        existingAccount.setCustName(accountDto.getCustName());
        existingAccount.setContactNumber(accountDto.getContactNumber());
        existingAccount.setBillingPocName(accountDto.getBillingPocName());
        existingAccount.setSalesAgentName(accountDto.getSalesAgentName());
        existingAccount.setSalesRegion(accountDto.getSalesRegion());
        existingAccount.setAccountStatus(accountDto.getAccountStatus());

        Account updatedAccount = accountRepository.save(existingAccount);
        return toDto(updatedAccount);
    }

    public AccountDto toDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .accountType(account.getAccountType())
                .businessActivity(account.getBusinessActivity())
                .accountNumber(account.getAccountNumber())
                .customerName(account.getCustomerName())
                .projectName(account.getProjectName())
                .tradeLicenseNo(account.getTradeLicenseNo())
                .taxDocumentNo(account.getTaxDocumentNo())
                .county(account.getCounty())
                .city(account.getCity())
                .address(account.getAddress())
                .custName(account.getCustName())
                .contactNumber(account.getContactNumber())
                .billingPocName(account.getBillingPocName())
                .salesAgentName(account.getSalesAgentName())
                .salesRegion(account.getSalesRegion())
                .status(account.getStatus())
                .accountStatus(account.getAccountStatus())
                .build();
    }

    public Account toEntity(AccountDto accountDto) {
        return Account.builder()
                .id(accountDto.getId())
                .accountType(accountDto.getAccountType())
                .businessActivity(accountDto.getBusinessActivity())
                .accountNumber(accountDto.getAccountNumber())
                .customerName(accountDto.getCustomerName())
                .projectName(accountDto.getProjectName())
                .tradeLicenseNo(accountDto.getTradeLicenseNo())
                .taxDocumentNo(accountDto.getTaxDocumentNo())
                .county(accountDto.getCounty())
                .city(accountDto.getCity())
                .address(accountDto.getAddress())
                .custName(accountDto.getCustName())
                .contactNumber(accountDto.getContactNumber())
                .billingPocName(accountDto.getBillingPocName())
                .salesAgentName(accountDto.getSalesAgentName())
                .salesRegion(accountDto.getSalesRegion())
                .status(accountDto.getStatus())
                .accountStatus(accountDto.getAccountStatus())
                .build();
    }

}
