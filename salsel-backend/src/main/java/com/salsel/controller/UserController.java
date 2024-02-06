package com.salsel.controller;

import com.salsel.dto.UserDto;
import com.salsel.service.ExcelGenerationService;
import com.salsel.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.salsel.constants.ExcelConstants.USER_TYPE;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final ExcelGenerationService excelGenerationService;

    public UserController(UserService userService, ExcelGenerationService excelGenerationService) {
        this.userService = userService;
        this.excelGenerationService = excelGenerationService;
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('READ_USER')")
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


    @GetMapping("/user/role/{roleName}")
    @PreAuthorize("hasAuthority('READ_USER')")
    public ResponseEntity<List<UserDto>> getUserByRoleName(@PathVariable String roleName) {
        List<UserDto> userDtoList = userService.getUsersByRoleName(roleName);
        return ResponseEntity.ok(userDtoList);
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

    @PatchMapping("/user/{id}")
    @PreAuthorize("hasAuthority('CREATE_USER') and hasAuthority('READ_USER')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updatedUserDto = userService.update(id, userDto);
        return ResponseEntity.ok(updatedUserDto);
    }

    @PutMapping("/user/regenerate-password/{id}")
    @PreAuthorize("hasAuthority('CREATE_USER') and hasAuthority('READ_USER')")
    public ResponseEntity<String> regeneratePassword(@PathVariable Long id) {
        String password = userService.regeneratePassword(id);
        return ResponseEntity.ok(password);
    }

    @PutMapping("/user/change-password/{id}")
    @PreAuthorize("hasAuthority('CREATE_USER') and hasAuthority('READ_USER')")
    public ResponseEntity<String> changePassword(@PathVariable Long id,
                                                 @RequestParam(value = "currentPassword") String currentPassword,
                                                 @RequestParam(value = "newPassword") String newPassword) {
        String password = userService.changePassword(id, currentPassword, newPassword);
        return ResponseEntity.ok(password);
    }

    @PutMapping("/user/status/{id}")
    @PreAuthorize("hasAuthority('CREATE_USER') and hasAuthority('READ_USER')")
    public ResponseEntity<Void> updateUserStatusToActive(@PathVariable Long id) {
        userService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download-user-excel")
    public void downloadUsersBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");

        List<UserDto> users = userService.getUsersBetweenDates(startDate, endDate);
        List<Map<String, Object>> excelData = excelGenerationService.convertUsersToExcelData(users);

        OutputStream outputStream = response.getOutputStream();
        excelGenerationService.createExcelFile(excelData, outputStream, USER_TYPE);
        outputStream.close();
    }

}
