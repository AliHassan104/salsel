package com.salsel.service;

import com.salsel.dto.UserDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface UserService {
    UserDto registerUser(UserDto userdto);
    UserDto registerRoleCustomerUser(UserDto userdto);
    String regeneratePassword(Long id);
    String changePassword(Long id, String currentPassword, String newPassword);
    List<UserDto> getUsersByRoleName(String roleName);
    List<UserDto> getAll(Boolean status);
    UserDto findById(Long id);
    UserDto findByName(String name);
    UserDto findByEmail(String email);
    void deleteById(Long id);
    void setToActiveById(Long id);
    UserDto update(Long id, UserDto userDto);
    void forgotPassword(String userEmail);
    void resetPassword(String userEmail, String resetCode, String newPassword);
    List<UserDto> getUsersBetweenDates(LocalDate startDate, LocalDate endDate);
    LocalDate getMinCreatedAt();
    LocalDate getMaxCreatedAt();
}
