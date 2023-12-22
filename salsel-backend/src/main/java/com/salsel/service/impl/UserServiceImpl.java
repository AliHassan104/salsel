package com.salsel.service.impl;

import com.salsel.dto.UserDto;
import com.salsel.exception.InvalidResetCodeException;
import com.salsel.exception.RecordNotFoundException;
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
    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User not found for id => %d", id)));

        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setEmployeeId(userDto.getEmployeeId());
        existingUser.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        existingUser.getRoles().removeIf(role -> !userDto.getRoles().contains(role));

        Set<Role> roleList = userDto.getRoles().stream()
                .map(role -> roleRepository.findById(role.getId())
                        .orElseThrow(() -> new RecordNotFoundException("Role not found")))
                .collect(Collectors.toSet());

        existingUser.setRoles(roleList);
        User updatedUser = userRepository.save(existingUser);
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

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .employeeId(user.getEmployeeId())
                .name(user.getName())
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
                .employeeId(userDto.getEmployeeId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .employeeId(userDto.getEmployeeId())
                .password(userDto.getPassword())
                .status(userDto.getStatus())
                .roles(userDto.getRoles())
                .build();
    }
}
