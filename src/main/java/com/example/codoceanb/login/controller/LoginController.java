package com.example.codoceanb.login.controller;

import com.example.codoceanb.login.dto.UserDTO;
import com.example.codoceanb.login.dto.UserLoginDTO;
import com.example.codoceanb.login.entity.OTP;
import com.example.codoceanb.login.mapper.UserMapper;
import com.example.codoceanb.login.request.VerifyOTPRequest;
import com.example.codoceanb.login.response.LoginResponse;
import com.example.codoceanb.login.response.RegisterResponse;
import com.example.codoceanb.login.response.VerifyOTPResponse;
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
    private final OTPService otpService;

    @Autowired
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> createUser(
            @RequestBody UserDTO userDTO
    ) {
        RegisterResponse registerResponse=new RegisterResponse();
        try{
            Boolean result = userService.createUser(userDTO);
            if (result) registerResponse.setMessage("Register successfully");
            return ResponseEntity.ok(RegisterResponse.builder()
                    .message(result?
                            MessageKeys.REGISTER_SUCCESSFULLY
                            :
                            MessageKeys.REGISTER_FAILED)
                    .build());
        }catch (DataIntegrityViolationException e) {
            registerResponse.setMessage(MessageKeys.PHONE_NUMBER_ALREADY_EXISTS);
            return ResponseEntity.badRequest().body(registerResponse);
        } catch (Exception e) {
            registerResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(registerResponse);
        }

    }

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> login(
            @RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request
    ) {
        try{
            LoginResponse loginResponse = new LoginResponse();

            String token = userService.login(
                    userLoginDTO.getEmail(),
                    userLoginDTO.getPassword()
            );
            loginResponse.setToken(token);
            Boolean isActive = userService.getUserDetailsFromToken(token).isActive();

            return ResponseEntity.ok(LoginResponse.builder()
                    .message(MessageKeys.LOGIN_SUCCESSFULLY)
                    .token(token)
                    .isActive(isActive)
                    .build());
        }catch (BadCredentialsException e) {
            LoginResponse loginResponse = LoginResponse.builder()
                    .message(MessageKeys.PASSWORD_NOT_MATCH)
                    .build();
            return ResponseEntity.badRequest().body(loginResponse);

        } catch (Exception e) {
            LoginResponse loginResponse = LoginResponse.builder()
                    .message(MessageKeys.LOGIN_FAILED)
                    .build();
            return ResponseEntity.badRequest().body(loginResponse);
        }
    }

    @GetMapping("/request-otp")
    public ResponseEntity<Void> requestOtp(@RequestParam(required = false) String email,
                                                  @NonNull HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        boolean isSuccessful;
        if(authHeader != null && email == null) {
            String token = authHeader.substring(7);
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
            @NonNull HttpServletRequest request) {
        VerifyOTPResponse response;
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String token = authHeader.substring(7);
            response = otpService.verify(token, verifyRequest.getOtp(), OTP.EType.ACTIVE_ACCOUNT);
        } else {
            response = otpService.verify(verifyRequest.getEmail(), verifyRequest.getOtp(), OTP.EType.FORGOT_PASSWORD);
        }

        if (response.isSuccessfully()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
