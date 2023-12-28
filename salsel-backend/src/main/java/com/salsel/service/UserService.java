package com.salsel.service;

import com.salsel.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDto registerUser(UserDto userdto);
    UserDto registerRoleCustomerUser(UserDto userdto);
    List<UserDto> getAll(Boolean status);
    UserDto findById(Long id);
    UserDto findByName(String name);
    void deleteById(Long id);
    void setToActiveById(Long id);
    UserDto update(Long id, UserDto userDto);
    void forgotPassword(String userEmail);
    void resetPassword(String userEmail, String resetCode, String newPassword);
}
