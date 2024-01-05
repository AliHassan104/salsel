package com.salsel.service.impl;

import com.salsel.dto.AccountDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.exception.UserAlreadyExistAuthenticationException;
import com.salsel.model.Account;
import com.salsel.model.User;
import com.salsel.repository.AccountRepository;
import com.salsel.repository.UserRepository;
import com.salsel.service.AccountService;
import com.salsel.service.BucketService;
import com.salsel.utils.EmailUtils;
import com.salsel.utils.HelperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final HelperUtils helperUtils;
    private final EmailUtils emailUtils;
    private final BucketService bucketService;
    private static final Logger logger = LoggerFactory.getLogger(bucketServiceImpl.class);


    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository, HelperUtils helperUtils, EmailUtils emailUtils, BucketService bucketService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.helperUtils = helperUtils;
        this.emailUtils = emailUtils;
        this.bucketService = bucketService;
    }

    @Override
    @Transactional
    public AccountDto save(AccountDto accountDto, MultipartFile pdf) {
        Account account = toEntity(accountDto);
        account.setStatus(true);

        Optional<User> existingUser = userRepository.findByEmail(account.getEmail());
        if(existingUser.isPresent()){
            throw new UserAlreadyExistAuthenticationException("User Already Exist");
        }

        String password = helperUtils.generateResetPassword();
        User user = new User();
        user.setEmail(account.getEmail());
        user.setPassword(password);
        User createdUser = userRepository.save(user);
        emailUtils.sendWelcomeEmail(createdUser, password);
        Account createdAccount = accountRepository.save(account);

        // Save PDF to S3 bucket
        if (pdf != null && !pdf.isEmpty()) {
            String folderName = "Account_" + createdAccount.getId();
            String savedPdfUrl = helperUtils.savePdfToS3(pdf, folderName);
            createdAccount.setAccountUrl(savedPdfUrl);
            logger.info("PDF is uploaded to S3 in folder '{}'.", folderName);
        }
        return toDto(accountRepository.save(createdAccount));
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
    public AccountDto update(Long id, AccountDto accountDto, MultipartFile pdf) {
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

        if (pdf != null && !pdf.isEmpty()) {
            String folderKey = "Account/Account_" + id;
            bucketService.deleteFilesStartingWith(folderKey,"Agreement_");
            String folderName = "Account_" + existingAccount.getId();
            String savedPdfUrl = helperUtils.savePdfToS3(pdf, folderName);
            existingAccount.setAccountUrl(savedPdfUrl);
            logger.info("PDF is uploaded to S3 in folder '{}'.", folderName);
        }

        Account updatedAccount = accountRepository.save(existingAccount);
        return toDto(updatedAccount);
    }

    public AccountDto toDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .accountType(account.getAccountType())
                .email(account.getEmail())
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
                .accountUrl(account.getAccountUrl())
                .status(account.getStatus())
                .build();
    }

    public Account toEntity(AccountDto accountDto) {
        return Account.builder()
                .id(accountDto.getId())
                .email(accountDto.getEmail())
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
                .accountUrl(accountDto.getAccountUrl())
                .status(accountDto.getStatus())
                .build();
    }

}
