package com.salsel.service.impl;

import com.salsel.dto.UserDto;
import com.salsel.exception.InvalidResetCodeException;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.exception.UserAlreadyExistAuthenticationException;
import com.salsel.model.Country;
import com.salsel.model.Otp;
import com.salsel.model.Role;
import com.salsel.model.User;
import com.salsel.repository.CountryRepository;
import com.salsel.repository.OtpRepository;
import com.salsel.repository.RoleRepository;
import com.salsel.repository.UserRepository;
import com.salsel.service.UserService;
import com.salsel.utils.EmailUtils;
import com.salsel.utils.HelperUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final OtpRepository otpRepository;
    private final CountryRepository countryRepository;
    private final HelperUtils helperUtils;
    private final EmailUtils emailUtils;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, OtpRepository otpRepository, CountryRepository countryRepository, HelperUtils helperUtils, EmailUtils emailUtils) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.otpRepository = otpRepository;
        this.countryRepository = countryRepository;
        this.helperUtils = helperUtils;
        this.emailUtils = emailUtils;
    }

    @Override
    @Transactional
    public UserDto registerUser(UserDto userdto) {
        User user = toEntity(userdto);
        user.setCreatedAt(LocalDate.now());

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistAuthenticationException("User Already Exist");
        }

        String password = helperUtils.generateResetPassword();
        user.setStatus(true);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        Set<Role> roleList = new HashSet<>();
        for (Role role : user.getRoles()) {
            roleRepository.findById(role.getId())
                    .orElseThrow(() -> new RecordNotFoundException("Role not found"));
            roleList.add(role);
        }
        user.setRoles(roleList);

        if(user.getEmployeeId() == null){
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
        }
        else{
            Optional<User> userExist = userRepository.findByEmployeeId(user.getEmployeeId());
            Long employeeId = user.getEmployeeId();

            while (userExist.isPresent()) {
                // If user with the same employeeId exists, increment employeeId by 1
                employeeId++;
                userExist = userRepository.findByEmployeeId(employeeId);
            }

            Country country = countryRepository.findCountryByName(user.getCountry());
            Integer countryCode = country.getCode();

            String empIdStr = String.valueOf(employeeId);
            String extractedCountryCode = empIdStr.substring(0, countryCode.toString().length());

            if (extractedCountryCode.equals(String.valueOf(countryCode))) {
                user.setEmployeeId(employeeId);
            } else {
                throw new RecordNotFoundException("Country code doesn't match with employeeId");
            }
        }



        userRepository.save(user);
//        emailUtils.sendWelcomeEmail(user, password);
        return toDto(user);
    }

    @Override
    @Transactional
    public UserDto registerRoleCustomerUser(UserDto userdto) {
        User user = toEntity(userdto);
        user.setCreatedAt(LocalDate.now());
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistAuthenticationException("User Already Exist");
        }

        user.setStatus(true);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> roleList = new HashSet<>();
        Role role = roleRepository.findByName("ROLE_WEB_USER")
                .orElseThrow(() -> new RecordNotFoundException("Role not found"));
        roleList.add(role);
        user.setRoles(roleList);

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

        userRepository.save(user);
        return toDto(user);
    }

    @Override
    @Transactional
    public String regeneratePassword(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User not found for id => %d", id)));

        String password = helperUtils.generateResetPassword();
        existingUser.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(existingUser);
        emailUtils.sendPasswordRegeneratedEmail(existingUser, password);
        return password;
    }

    @Override
    public List<UserDto> getUsersByRoleName(String roleName) {
        List<User> userList = userRepository.findAllByRoleName(roleName);
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : userList) {
            UserDto userDto = toDto(user);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    @Transactional
    public String changePassword(Long id, String currentPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        if (bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
            String encodedNewPassword = bCryptPasswordEncoder.encode(newPassword);

            userRepository.updatePassword(id, encodedNewPassword);

            return "Password updated successfully";
        } else {
            return "Current password is incorrect";
        }
    }


    @Override
    public List<UserDto> getAll(Boolean status) {
        List<User> userList = userRepository.findAllInDesOrderByIdAndStatus(status);
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : userList) {
            UserDto userDto = toDto(user);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    public UserDto findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return toDto(user);
        } else {
            throw new RecordNotFoundException(String.format("User not found for id => %d", id));
        }
    }

    @Override
    public UserDto findByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return toDto(user);
        } else {
            throw new RecordNotFoundException(String.format("User not found for email => %s", email));
        }
    }

    @Override
    public UserDto findByEmployeeId(Long employeeId) {
        Optional<User> optionalUser = userRepository.findByEmployeeId(employeeId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return toDto(user);
        } else {
            throw new RecordNotFoundException(String.format("Incorrect Username => %d", employeeId));
        }
    }


    @Override
    public UserDto findByName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User not found for name => %s", name)));
        return toDto(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User not found for id => %d", id)));
        userRepository.setStatusInactive(user.getId());
    }

    @Override
    @Transactional
    public void setToActiveById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User not found for id => %d", id)));
        userRepository.setStatusActive(user.getId());
    }

    @Override
    public LocalDate getMinCreatedAt() {
        return userRepository.findMinCreatedAt();
    }

    @Override
    public LocalDate getMaxCreatedAt() {
        return userRepository.findMaxCreatedAt();
    }

    @Override
    public User getLoggedInUser() {
        return helperUtils.getCurrentUser();
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        boolean rolesAssignedForFirstTime = false;
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User not found for id => %d", id)));

        // Check if the provided Email matches with any other user's email (excluding the current user)
        if (!existingUser.getEmail().equalsIgnoreCase(userDto.getEmail())) {
            Optional<User> alreadyExistUser = userRepository.findByEmail(userDto.getEmail());
            if (alreadyExistUser.isPresent()) {
                throw new UserAlreadyExistAuthenticationException("Email Already Exist");
            }
        }

        // Check if the provided employeeId matches with any other user's employeeId (excluding the current user)
        if (!existingUser.getEmployeeId().equals(userDto.getEmployeeId())) {
            Optional<User> alreadyExistUserEmployeeId = userRepository.findByEmployeeId(userDto.getEmployeeId());
            if (alreadyExistUserEmployeeId.isPresent()) {
                throw new UserAlreadyExistAuthenticationException("Employee ID Already Exist");
            }
        }

        if (userDto.getEmail() != null) {
            existingUser.setName(userDto.getName());
            existingUser.setEmail(userDto.getEmail());
            existingUser.setPhone(userDto.getPhone());
            existingUser.setFirstname(userDto.getFirstname());
            existingUser.setLastname(userDto.getLastname());
            existingUser.setEmployeeId(userDto.getEmployeeId());
            existingUser.setCountry(userDto.getCountry());
            existingUser.setCity(userDto.getCity());

            rolesAssignedForFirstTime = existingUser.getRoles().isEmpty() && !userDto.getRoles().isEmpty();
            existingUser.getRoles().removeIf(role -> !userDto.getRoles().contains(role));
        }


        // Check if the password is provided before attempting to encode it
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        }

        Set<Role> roleList = userDto.getRoles().stream()
                .map(role -> roleRepository.findById(role.getId())
                        .orElseThrow(() -> new RecordNotFoundException("Role not found")))
                .collect(Collectors.toSet());

        existingUser.setRoles(roleList);
        User updatedUser = userRepository.save(existingUser);

        // If roles are assigned for the first time, generate a password and send email
        if (rolesAssignedForFirstTime) {
            String password = helperUtils.generateResetPassword();
            existingUser.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(existingUser);
            emailUtils.sendWelcomeEmail(existingUser, password);
        }
        return toDto(updatedUser);
    }


    @Override
    @Transactional
    public void forgotPassword(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        String resetCode = helperUtils.generateResetCode();

        Otp otp = new Otp();
        otp.setResetCode(resetCode);
        otpRepository.save(otp);
        emailUtils.sendPasswordResetEmail(user, resetCode);
    }

    @Override
    @Transactional
    public void resetPassword(String userEmail, String resetCode, String newPassword) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        Otp otp = otpRepository.findByResetCode(resetCode)
                .orElseThrow(() -> new RecordNotFoundException("Otp not found"));

        // Check if reset code is valid and not expired
        if (helperUtils.isValidResetCode(otp, resetCode)) {
            String encodedNewPassword = bCryptPasswordEncoder.encode(newPassword);
            userRepository.updatePassword(user.getId(), encodedNewPassword);
        } else {
            throw new InvalidResetCodeException("Invalid or expired reset code");
        }
    }

    @Override
    @Transactional
    public void isValidOtp(String resetCode) {
        Otp otp = otpRepository.findByResetCode(resetCode)
                .orElseThrow(() -> new RecordNotFoundException("Invalid Otp"));

        if (!helperUtils.isValidResetCode(otp, resetCode)) {
            throw new InvalidResetCodeException("Invalid or expired Otp");
        }
    }

    @Override
    public List<UserDto> getUsersBetweenDates(LocalDate startDate, LocalDate endDate) {
        return userRepository.findAllByCreatedAtBetween(startDate, endDate)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .employeeId(user.getEmployeeId())
                .name(user.getName())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phone(user.getPhone())
                .employeeId(user.getEmployeeId())
                .email(user.getEmail())
                .password(user.getPassword())
                .status(user.getStatus())
                .roles(user.getRoles())
                .country(user.getCountry())
                .city(user.getCity())
                .employeeReferenceId(user.getEmployeeReferenceId())
                .build();
    }

    public User toEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .createdAt(userDto.getCreatedAt())
                .employeeId(userDto.getEmployeeId())
                .email(userDto.getEmail())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .phone(userDto.getPhone())
                .name(userDto.getName())
                .employeeId(userDto.getEmployeeId())
                .password(userDto.getPassword())
                .status(userDto.getStatus())
                .city(userDto.getCity())
                .employeeReferenceId(userDto.getEmployeeReferenceId())
                .country(userDto.getCountry())
                .roles(userDto.getRoles())
                .build();
    }
}