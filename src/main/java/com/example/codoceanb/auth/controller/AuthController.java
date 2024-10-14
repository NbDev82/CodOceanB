package com.example.codoceanb.auth.controller;

import com.example.codoceanb.auth.dto.UserDTO;
import com.example.codoceanb.auth.dto.UserLoginDTO;
import com.example.codoceanb.auth.entity.OTP;
import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.exception.PermissionDenyException;
import com.example.codoceanb.auth.request.ChangePasswordRequest;
import com.example.codoceanb.auth.request.ForgotPasswordRequest;
import com.example.codoceanb.auth.request.VerifyOTPRequest;
import com.example.codoceanb.auth.response.*;
import com.example.codoceanb.auth.service.AccountService;
import com.example.codoceanb.auth.service.OTPService;
import com.example.codoceanb.auth.service.UserService;
import com.example.codoceanb.auth.utils.MessageKeys;
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
@RequestMapping("/api/auth/v1")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    private final UserService userService;

    @Autowired
    private final AccountService accountService;

    @Autowired
    private final OTPService otpService;

    @PostMapping("/sign-up")
    public ResponseEntity<RegisterResponse> signUp(@RequestBody UserDTO userDTO) {
        try {
            boolean result = userService.createUser(userDTO);
            String message = result ? MessageKeys.REGISTER_SUCCESSFULLY : MessageKeys.REGISTER_FAILED;
            return ResponseEntity.ok(RegisterResponse.builder()
                    .message(message)
                    .build());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(RegisterResponse.builder()
                    .message(MessageKeys.EMAIL_ALREADY_EXISTS)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(RegisterResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> signIn(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            String accessToken = accountService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword());
            String refreshToken = accountService.generateAndSaveRefreshToken(accessToken);
            User user= userService.getUserDetailsFromCleanToken(accessToken);
            
            return ResponseEntity.ok(LoginResponse.builder()
                    .message(MessageKeys.LOGIN_SUCCESSFULLY)
                    .refreshToken(refreshToken)
                    .accessToken(accessToken)
                    .isActive(user.isActive())
                    .isFirstLogin(user.isFirstLogin())
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

    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut(@RequestHeader(value = "Authorization") String refreshToken) {
        if (refreshToken != null && !refreshToken.isEmpty()) {
            accountService.deleteRefreshToken(refreshToken);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AccessTokenResponse> refreshToken(@RequestHeader(value = "Authorization") String refreshToken) {
        try {
            String newAccessToken = accountService.refreshToken(refreshToken);
            return ResponseEntity.ok(AccessTokenResponse.builder()
                    .accessToken(newAccessToken)
                    .message(MessageKeys.REFRESH_TOKEN_SUCCESSFUL)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(AccessTokenResponse.builder()
                    .message(MessageKeys.REFRESH_TOKEN_FAILED)
                    .build());
        }
    }
    
    @GetMapping("/request-otp")
    public ResponseEntity<Void> requestOtp(@RequestParam(required = false) String email,
                                           @RequestHeader(value = "Authorization", required = false) String token) {
        boolean isSuccessful = false;
        if(token != null && !token.isEmpty()) {
            if(email == null || email.isEmpty()) {
                isSuccessful = otpService.requestOTP(token, OTP.EType.ACTIVE_ACCOUNT);
            } else {
                isSuccessful = otpService.requestOTP(token, OTP.EType.CHANGE_EMAIL);
            }
        } else if(email != null && !email.isEmpty()) {
            isSuccessful = otpService.requestOTP(email, OTP.EType.FORGOT_PASSWORD);
        }

        return isSuccessful?
                ResponseEntity.status(HttpStatus.CREATED).build():
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Void> verifyOtp(@RequestBody VerifyOTPRequest verifyRequest,
                                          @RequestHeader(value = "Authorization", required = false) String token) {
        boolean isSuccessful = false;
        if(token != null && !token.isEmpty()) {
            isSuccessful = otpService.verify(token, verifyRequest.getOtp(), OTP.EType.ACTIVE_ACCOUNT);
        }

        return isSuccessful?
                ResponseEntity.status(HttpStatus.CREATED).build():
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                                                 @RequestHeader(value = "Authorization", required = false) String bearerToken) {
        try {
            if (bearerToken != null && !bearerToken.isEmpty()) {
                accountService.changePassword(bearerToken, changePasswordRequest);
                return ResponseEntity.ok().build();
            } else {
                throw new PermissionDenyException("Permission deny!");
            }
        } catch (PermissionDenyException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(ChangePasswordResponse.builder()
                    .message(MessageKeys.PASSWORD_NOT_MATCH)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            boolean isVerified = otpService.verify(request.getEmail(), request.getOtp(), OTP.EType.FORGOT_PASSWORD);
            if (isVerified) {
                accountService.resetPassword(request.getEmail(), request.getNewPassword());
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
