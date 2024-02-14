package com.salsel.service.impl;

import com.salsel.dto.UserDto;
import com.salsel.exception.InvalidResetCodeException;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.exception.UserAlreadyExistAuthenticationException;
import com.salsel.model.Account;
import com.salsel.model.Otp;
import com.salsel.model.Role;
import com.salsel.model.User;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final OtpRepository otpRepository;
    private final HelperUtils helperUtils;
    private final EmailUtils emailUtils;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, OtpRepository otpRepository, HelperUtils helperUtils, EmailUtils emailUtils) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.otpRepository = otpRepository;
        this.helperUtils = helperUtils;
        this.emailUtils = emailUtils;
    }

    @Override
    @Transactional
    public UserDto registerUser(UserDto userdto) {
        User user = toEntity(userdto);
        user.setCreatedAt(LocalDate.now());

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if(existingUser.isPresent()){
            throw new UserAlreadyExistAuthenticationException("User Already Exist");
        }

        user.setStatus(true);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> roleList = new HashSet<>();
        for(Role role: user.getRoles()){
            roleRepository.findById(role.getId())
                    .orElseThrow(()-> new RecordNotFoundException("Role not found"));
            roleList.add(role);
        }
        user.setRoles(roleList);
        userRepository.save(user);
        return toDto(user);
    }

    @Override
    @Transactional
    public UserDto registerRoleCustomerUser(UserDto userdto) {
        User user = toEntity(userdto);
        user.setCreatedAt(LocalDate.now());
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if(existingUser.isPresent()){
            throw new UserAlreadyExistAuthenticationException("User Already Exist");
        }

        user.setStatus(true);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> roleList = new HashSet<>();
        Role role = roleRepository.findByName("ROLE_CUSTOMER_USER")
                .orElseThrow(()-> new RecordNotFoundException("Role not found"));
        roleList.add(role);
        user.setRoles(roleList);
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
    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        boolean rolesAssignedForFirstTime = false;
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User not found for id => %d", id)));

        if (userDto.getEmail() != null) {
            existingUser.setName(userDto.getName());
            existingUser.setEmail(userDto.getEmail());
            existingUser.setPhone(userDto.getPhone());
            existingUser.setFirstname(userDto.getFirstname());
            existingUser.setLastname(userDto.getLastname());
            existingUser.setEmployeeId(userDto.getEmployeeId());

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
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new InvalidResetCodeException("Invalid or expired reset code");
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
                .roles(userDto.getRoles())
                .build();
    }
}
