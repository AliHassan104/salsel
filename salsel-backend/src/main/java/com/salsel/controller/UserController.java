package com.salsel.controller;

import com.salsel.dto.UserDto;
import com.salsel.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private  final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('CREATE_USER') and hasAuthority('READ_USER')")
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(value = "status") Boolean status) {
        List<UserDto> userDtoList = userService.getAll(status);
        return ResponseEntity.ok(userDtoList);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('READ_USER')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.findById(id);
        return ResponseEntity.ok(userDto);
    }
    @GetMapping("/user/name/{name}")
    @PreAuthorize("hasAuthority('READ_USER')")
    public ResponseEntity<UserDto> getUserByName(@PathVariable String name) {
        UserDto userDto = userService.findByName(name);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/user/email/{email}")
    @PreAuthorize("hasAuthority('READ_USER')")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        UserDto userDto = userService.findByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasAuthority('DELETE_USER') and hasAuthority('READ_USER')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("hasAuthority('CREATE_USER') and hasAuthority('READ_USER')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updatedUserDto = userService.update(id, userDto);
        return ResponseEntity.ok(updatedUserDto);
    }

    @PutMapping("/user/status/{id}")
    @PreAuthorize("hasAuthority('CREATE_USER') and hasAuthority('READ_USER')")
    public ResponseEntity<Void> updateUserStatusToActive(@PathVariable Long id) {
        userService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }
}
