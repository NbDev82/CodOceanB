package com.example.codoceanb.login.service;

import com.example.codoceanb.infras.service.EmailService;
import com.example.codoceanb.infras.security.JwtTokenUtils;
import com.example.codoceanb.login.entity.OTP;
import com.example.codoceanb.login.entity.User;
import com.example.codoceanb.login.exception.UserNotFoundException;
import com.example.codoceanb.login.repository.OTPRepos;
import com.example.codoceanb.login.repository.UserRepos;
import com.example.codoceanb.login.response.VerifyOTPResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService{
    private static final Logger log = LogManager.getLogger(OTPServiceImpl.class);

    @Autowired
    private OTPRepos otpRepos;
    @Autowired
    private UserRepos userRepos;
    @Autowired
    private JwtTokenUtils jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public boolean requestOTP(String tokenOrEmail, OTP.EType type) {
        return type == OTP.EType.ACTIVE_ACCOUNT?
                requestActiveAccountOTP(tokenOrEmail, type):
                requestResetPasswordOTP(tokenOrEmail, type);
    }

    @Override
    public VerifyOTPResponse verify(String tokenOrEmail, String otp, OTP.EType type) {
        if(type.equals(OTP.EType.ACTIVE_ACCOUNT)) {
            return verifyActiveAccountOTP(tokenOrEmail, otp, type);
        } else {
            return verifyResetPasswordOTP(tokenOrEmail, otp, type);
        }

    }

    private VerifyOTPResponse verifyResetPasswordOTP(String email, String otp, OTP.EType type) {
        return verifyOtp(email, otp, type);
    }

    private VerifyOTPResponse verifyActiveAccountOTP(String token, String otp, OTP.EType type) {
        String email = jwtTokenUtil.extractEmail(token);
        return verifyOtp(email, otp, type);
    }

    private VerifyOTPResponse verifyOtp(String email, String otp, OTP.EType type) {
        OTP otpExisted = otpRepos.findByUserEmailAndType(email,type);
        boolean isMatches =otpExisted != null && passwordEncoder.matches(otp, otpExisted.getEncryptedOTP());
        if(isMatches) {
            User user = userRepos.findByEmail(email).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found", email)));
            user.setActive(true);
            userRepos.save(user);
        }
        return VerifyOTPResponse.builder()
                .isSuccessfully(isMatches)
                .build();
    }

    public boolean requestResetPasswordOTP(String email, OTP.EType type) {
        return handleRequestOTP(email, type);
    }

    public boolean requestActiveAccountOTP(String token, OTP.EType type) {
        String email = jwtTokenUtil.extractEmail(token);
        return handleRequestOTP(email, type);
    }

    public boolean handleRequestOTP(String email, OTP.EType type) {
        OTP existedOTP = checkExistedOTP(email, type);
        if(existedOTP != null) {
            deleteOTP(existedOTP);
        }

        String otpString = generateOtpString();
        OTP otp = createOTP(otpString, type);
        saveOTP(otp, email);
        sendEmail(email,otpString,type);

        return true;
    }

    private void sendEmail(String email, String otpString, OTP.EType type) {
        String subject = type.equals(OTP.EType.ACTIVE_ACCOUNT)?
                "This is your OTP for active your account":
                "This is your OTP for reset your password";
        String htmlEmail = emailService.createHtmlEmailContentWithOTP(otpString,type);
        emailService.sendHtmlContent(email,subject,htmlEmail);
    }

    public String generateOtpString() {
        SecureRandom random = new SecureRandom();
        return String.valueOf(random.nextInt(900000) + 100000);
    }

    private void saveOTP(OTP otp, String email) {
        if (email != null && otp != null) {
            User user = userRepos.findByEmail(email).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found", email)));
            otp.setUser(user);
            otpRepos.save(otp);
        } else {
            throw new IllegalArgumentException("Email or OTP cannot be null");
        }
    }

    private OTP checkExistedOTP(String email, OTP.EType type) {
        return otpRepos.findByUserEmailAndType(email,type);
    }

    private void deleteOTP(OTP existedOTP) {
        otpRepos.delete(existedOTP);
    }


    private OTP createOTP(String otpString, OTP.EType type) {
        String encryptedOTP = passwordEncoder.encode(otpString);
        return OTP.builder()
                .encryptedOTP(encryptedOTP)
                .type(type)
                .expirationDate(LocalDateTime.now().plusMinutes(5))
                .build();
    }
}
