package com.salsel.controller;

import com.salsel.config.security.JwtUtil;
import com.salsel.dto.AuthenticationResponse;
import com.salsel.dto.LoginCredentials;
import com.salsel.dto.UserDto;
import com.salsel.dto.WebUserHistoryDto;
import com.salsel.service.UserService;
import com.salsel.service.WebUserHistoryService;
import com.salsel.service.impl.MyUserDetailServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MyUserDetailServiceImplementation myUserDetailService;
    private final UserService userService;
    private final WebUserHistoryService webUserHistoryService;

    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, MyUserDetailServiceImplementation myUserDetailService, UserService userService, WebUserHistoryService webUserHistoryService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.myUserDetailService = myUserDetailService;
        this.userService = userService;
        this.webUserHistoryService = webUserHistoryService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginCredentials loginCredentials) throws Exception {

        if(loginCredentials.getEmail().contains("@")){
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginCredentials.getEmail(), loginCredentials.getPassword())
                );
            } catch (Exception e) {
                throw new BadCredentialsException("Incorrect Username or Password! ", e);
            }

            UserDetails userDetails = myUserDetailService.loadUserByUsername(loginCredentials.getEmail());
            String jwtToken = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthenticationResponse(jwtToken,loginCredentials.getEmail()));

        }else{
            UserDto user = userService.findByEmployeeId(loginCredentials.getEmail());
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getEmail(), loginCredentials.getPassword())
                );
            } catch (Exception e) {
                throw new BadCredentialsException("Incorrect Username or Password! ", e);
            }
            UserDetails userDetails = myUserDetailService.loadUserByUsername(user.getEmail());
            String jwtToken = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthenticationResponse(jwtToken,user.getEmail()));
        }
    }

    @PostMapping("/login/web-user")
    public ResponseEntity<?> createAuthenticationTokenForWebUser(@RequestBody WebUserHistoryDto webUserHistory) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken("webuser@gmail.com", "12345"));
        } catch (Exception e) {
            throw new BadCredentialsException("Incorrect Username or Password! ", e);
        }
        UserDetails userDetails = myUserDetailService.loadUserByUsername("webuser@gmail.com");
        String jwtToken = jwtUtil.generateToken(userDetails);
        webUserHistoryService.loginWebUser(webUserHistory);
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken,"webuser@gmail.com"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto userdto) {
        return ResponseEntity.ok(userService.registerUser(userdto));
    }

    @PostMapping("/signup/customer")
    public ResponseEntity<?> customerSignUp(@RequestBody UserDto userdto) {
        return ResponseEntity.ok(userService.registerRoleCustomerUser(userdto));
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok("Password reset email sent successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String newPassword,
            @RequestParam String email,
            @RequestParam String code) {
        userService.resetPassword(email, code, newPassword);
        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/reset-code-check")
    public ResponseEntity<String> resetCodeCheck(@RequestParam String code) {
        userService.isValidOtp(code);
        return ResponseEntity.ok("Valid Otp");
    }

    @PostMapping("/send/otp")
    public ResponseEntity<String> SendOtpForAwbCreation(@RequestParam String email) {
        userService.sendOtpForAwbCreation(email);
        return ResponseEntity.ok("Otp email sent successfully");
    }
}
