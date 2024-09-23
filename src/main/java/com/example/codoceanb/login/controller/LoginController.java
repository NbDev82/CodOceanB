package com.example.codoceanb.login.controller;

import com.example.codoceanb.login.dto.UserDTO;
import com.example.codoceanb.login.dto.UserLoginDTO;
import com.example.codoceanb.login.entity.OTP;
import com.example.codoceanb.login.exception.PermissionDenyException;
import com.example.codoceanb.login.mapper.UserMapper;
import com.example.codoceanb.login.request.ChangePasswordRequest;
import com.example.codoceanb.login.request.VerifyOTPRequest;
import com.example.codoceanb.login.response.LoginResponse;
import com.example.codoceanb.login.response.RegisterResponse;
import com.example.codoceanb.login.response.VerifyOTPResponse;
import com.example.codoceanb.login.service.AccountService;
import com.example.codoceanb.login.service.OTPService;
import com.example.codoceanb.login.service.UserService;
import com.example.codoceanb.login.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {
    private static final Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    private final UserService userService;

    @Autowired
    private final AccountService accountService;

    @Autowired
    private final OTPService otpService;

    @Autowired
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> createUser(@RequestBody UserDTO userDTO) {
        try {
            boolean result = userService.createUser(userDTO);
            String message = result ? MessageKeys.REGISTER_SUCCESSFULLY : MessageKeys.REGISTER_FAILED;
            return ResponseEntity.ok(RegisterResponse.builder()
                    .message(message)
                    .build());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(RegisterResponse.builder()
                    .message(MessageKeys.PHONE_NUMBER_ALREADY_EXISTS)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(RegisterResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            String token = accountService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword());
            Boolean isActive = userService.getUserDetailsFromToken(token).isActive();
            
            return ResponseEntity.ok(LoginResponse.builder()
                    .message(MessageKeys.LOGIN_SUCCESSFULLY)
                    .token(token)
                    .isActive(isActive)
                    .build());
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                    .message(MessageKeys.PASSWORD_NOT_MATCH)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                    .message(MessageKeys.LOGIN_FAILED)
                    .build());
        }
    }

    @GetMapping("/request-otp")
    public ResponseEntity<Void> requestOtp(@RequestParam(required = false) String email,
                                           @RequestHeader(value = "Authorization") String token) {
        boolean isSuccessful;
        if(token != null && email == null) {
            token = token.substring(7);
            isSuccessful = otpService.requestOTP(token, OTP.EType.ACTIVE_ACCOUNT);
        }
        else {
            isSuccessful = otpService.requestOTP(email, OTP.EType.FORGOT_PASSWORD);
        }

        return isSuccessful?
                ResponseEntity.status(HttpStatus.CREATED).build():
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<VerifyOTPResponse> verifyOtp(@RequestBody VerifyOTPRequest verifyRequest,
                                                       @RequestHeader(value = "Authorization", required = false) String token) {
        VerifyOTPResponse response;
        if (token != null && !token.isEmpty()) {
            token = token.substring(7);
            response = otpService.verify(token, verifyRequest.getOtp(), OTP.EType.ACTIVE_ACCOUNT);
        } else {
            response = otpService.verify(verifyRequest.getEmail(), verifyRequest.getOtp(), OTP.EType.FORGOT_PASSWORD);
        }

        return response.isSuccessfully() ?
                ResponseEntity.ok(response) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                               @RequestHeader(value = "Authorization", required = false) String token) {
        if (token != null && !token.isEmpty()) {
            token = token.substring(7);
            accountService.changePassword(token,changePasswordRequest.getNewPassword());
        } else {
            throw new PermissionDenyException("Permission deny!");
        }
        return null;
    }
}
