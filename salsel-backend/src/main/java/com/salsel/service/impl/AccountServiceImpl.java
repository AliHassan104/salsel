package com.salsel.service.impl;

import com.salsel.dto.AccountDto;
import com.salsel.dto.CustomUserDetail;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Account;
import com.salsel.model.Country;
import com.salsel.model.User;
import com.salsel.repository.AccountRepository;
import com.salsel.repository.CountryRepository;
import com.salsel.repository.UserRepository;
import com.salsel.service.AccountService;
import com.salsel.service.BucketService;
import com.salsel.utils.EmailUtils;
import com.salsel.utils.HelperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final HelperUtils helperUtils;
    private final EmailUtils emailUtils;
    private final BucketService bucketService;
    private final CountryRepository countryRepository;
    private static final Logger logger = LoggerFactory.getLogger(bucketServiceImpl.class);


    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository, HelperUtils helperUtils, EmailUtils emailUtils, BucketService bucketService, CountryRepository countryRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.helperUtils = helperUtils;
        this.emailUtils = emailUtils;
        this.bucketService = bucketService;
        this.countryRepository = countryRepository;
    }

    @Override
    @Transactional
    public AccountDto save(AccountDto accountDto, MultipartFile pdf) {

        Account account = toEntity(accountDto);
        account.setStatus(true);
        account.setCreatedAt(LocalDate.now());

        Account latestAccount = accountRepository.findAccountByLatestId();

        if(latestAccount != null){
            // Retrieve the country code from the latest Account
            Country country = countryRepository.findCountryByName(latestAccount.getCounty());
            Integer countryCode = country.getCode();

            // Extract the accountNumber and remove the country code
            String accountNumberStr = String.valueOf(latestAccount.getAccountNumber());
            String accountNumberWithoutCountryCode = accountNumberStr.substring(String.valueOf(countryCode).length());

            // Retrieve the new country code and prepend it to the account Number
            Integer newCountryCode = countryRepository.findCountryCodeByCountryName(account.getCounty());
            String code = String.valueOf(newCountryCode);
            Long newAccNum = Long.parseLong(code + accountNumberWithoutCountryCode);

            newAccNum++;
            account.setAccountNumber(newAccNum);
        }
        else{
            String firstAccountNumber = "0000001";
            Integer countryCode = countryRepository.findCountryCodeByCountryName(accountDto.getCounty());
            String concatenatedValue = countryCode.toString() + firstAccountNumber;
            Long combinedValue = Long.parseLong(concatenatedValue);
            account.setAccountNumber(combinedValue);
        }

        Account createdAccount;
        Optional<User> existingUser = userRepository.findByEmail(account.getEmail());
        if (!existingUser.isPresent()) {
            User user = new User();
            user.setEmail(account.getEmail());
            user.setCreatedAt(LocalDate.now());
            user.setCountry(account.getCounty());
            user.setCity(account.getCity());
            user.setName(account.getCustName());
            user.setStatus(true);

            // If there are existing users, adjust the employee ID to include the country code
            User latestUser = userRepository.findUserByLatestId();

            if (latestUser != null) {

                // Retrieve the country code from the latest user
                Country country = countryRepository.findCountryByName(latestUser.getCountry());
                Integer countryCode = country.getCode();

                // Extract the employee ID and remove the country code
                String empIdStr = String.valueOf(latestUser.getEmployeeId());
                String empIdWithoutCountryCode = empIdStr.substring(String.valueOf(countryCode).length());

                // Retrieve the new country code and prepend it to the employee ID
                Integer newCountryCode = countryRepository.findCountryCodeByCountryName(user.getCountry());
                String code = String.valueOf(newCountryCode);
                Long newEmpId = Long.parseLong(code + empIdWithoutCountryCode);

                newEmpId++;
                user.setEmployeeId(newEmpId);
            } else {
                String firstEmployeeId = "0001";
                Integer countryCode = countryRepository.findCountryCodeByCountryName(user.getCountry());
                String concatenatedValue = countryCode.toString() + firstEmployeeId;
                Long combinedValue = Long.parseLong(concatenatedValue);
                user.setEmployeeId(combinedValue);
            }
            User createdUser = userRepository.save(user);
        }
        createdAccount = accountRepository.save(account);

        // Save PDF to S3 bucket
        if (pdf != null && !pdf.isEmpty()) {
            String folderName = "Account_" + createdAccount.getId();
            String savedPdfUrl = helperUtils.saveAccountPdfToS3(pdf, folderName);
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
    public List<Map<String,String>> getAccountNumberWithCustomerName(Boolean status) {
        List<Account> accounts = accountRepository.findAllInDesOrderByIdAndStatus(status);
        List<Map<String, String>> accountNumberCustomerNameList = accounts.stream()
                .map(account -> {
                    Map<String, String> accountInfo = new HashMap<>();
                    accountInfo.put("accountNumber", String.valueOf(account.getAccountNumber()));
                    accountInfo.put("customerName", account.getCustomerName());
                    return accountInfo;
                })
                .collect(Collectors.toList());
        return accountNumberCustomerNameList;
    }
    @Override
    public List<AccountDto> getAccountsByLoggedInUser(Boolean status) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetail) {
            String email = ((CustomUserDetail) principal).getEmail();
            User user = userRepository.findByEmailAndStatusIsTrue(email)
                    .orElseThrow(() -> new RecordNotFoundException("User not found"));

            List<Account> accountList = accountRepository.findAllInDesOrderByEmailAndStatus(status,user.getEmail());
            List<AccountDto> accountDtoList = new ArrayList<>();

            for (Account account : accountList) {
                AccountDto accountDto = toDto(account);
                accountDtoList.add(accountDto);
            }
            return accountDtoList;
        }
        return null;
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
            String savedPdfUrl = helperUtils.saveAccountPdfToS3(pdf, folderName);
            existingAccount.setAccountUrl(savedPdfUrl);
            logger.info("PDF is uploaded to S3 in folder '{}'.", folderName);
        }

        Account updatedAccount = accountRepository.save(existingAccount);
        return toDto(updatedAccount);
    }

    @Override
    public LocalDate getMinCreatedAt() {
        return accountRepository.findMinCreatedAt();
    }

    @Override
    public LocalDate getMaxCreatedAt() {
        return accountRepository.findMaxCreatedAt();
    }

    @Override
    public Map<String, Long> getStatusCounts() {
        Map<String, Long> statusCounts = new HashMap<>();

        // Add logic to get counts for true status
        statusCounts.put("active", accountRepository.countByStatus(true));

        // Add logic to get counts for false status
        statusCounts.put("inactive", accountRepository.countByStatus(false));

        return statusCounts;
    }

    @Override
    public Map<String, Long> getStatusCountsBasedOnLoggedInUser() {
        Map<String, Long> statusCounts = new HashMap<>();

        // Get the logged-in user's email
        String loggedInUserEmail = getLoggedInUserEmail();

        // Add logic to get counts based on different AWB statuses for the logged-in user
        statusCounts.put("active", accountRepository.countByStatusAndEmail(true,  loggedInUserEmail));
        statusCounts.put("inactive", accountRepository.countByStatusAndEmail(false,  loggedInUserEmail));

        return statusCounts;
    }

    private String getLoggedInUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetail) {
            return ((CustomUserDetail) principal).getEmail();
        }
        return null;
    }

    @Override
    public List<AccountDto> getAccountsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return accountRepository.findAllByCreatedAtBetween(startDate, endDate)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public AccountDto toDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .createdAt(account.getCreatedAt())
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
                .createdAt(accountDto.getCreatedAt())
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
