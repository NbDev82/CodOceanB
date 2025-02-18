package com.example.codoceanb.auth.service;

import com.example.codoceanb.infras.service.EmailService;
import com.example.codoceanb.infras.security.JwtTokenUtils;
import com.example.codoceanb.auth.entity.OTP;
import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.exception.UserNotFoundException;
import com.example.codoceanb.auth.repository.OTPRepos;
import com.example.codoceanb.auth.repository.UserRepos;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {
    private static final Logger log = LogManager.getLogger(OTPServiceImpl.class);

    private final OTPRepos otpRepos;
    private final UserRepos userRepos;
    private final JwtTokenUtils jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public boolean requestOTP(String tokenOrEmail, OTP.EType type) {
        String email = (type == OTP.EType.ACTIVE_ACCOUNT || type == OTP.EType.CHANGE_EMAIL) ? 
                        jwtTokenUtil.extractEmailFromBearerToken(tokenOrEmail) : tokenOrEmail;
        return handleRequestOTP(email, type);
    }

    @Override
    public boolean verify(String tokenOrEmail, String otp, OTP.EType type) {
        String email = (type == OTP.EType.ACTIVE_ACCOUNT || type == OTP.EType.CHANGE_EMAIL) ? 
                        jwtTokenUtil.extractEmailFromBearerToken(tokenOrEmail) : tokenOrEmail;
        return handleVerifyOtp(email, otp, type);
    }

    private boolean handleVerifyOtp(String email, String otp, OTP.EType type) {
        OTP otpExisted = otpRepos.findByUserEmailAndType(email, type);
        boolean isMatches = otpExisted != null && passwordEncoder.matches(otp, otpExisted.getEncryptedOTP());
        if (isMatches && LocalDateTime.now().isBefore(otpExisted.getExpirationDate())) {
            if(type.equals(OTP.EType.ACTIVE_ACCOUNT)) {
                User user = userRepos.findByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException(String.format("User %s not found", email)));
                user.setActive(true);
                userRepos.save(user);
            }
            otpExisted.setEncryptedOTP(null);
            otpRepos.save(otpExisted);
        }
        return isMatches;
    }

    public boolean handleRequestOTP(String email, OTP.EType type) {
        OTP existedOTP = otpRepos.findByUserEmailAndType(email, type);
        String otpString = generateOtpString();
        if(existedOTP == null) {
            existedOTP = createOTP(otpString, type);
        } else {
            existedOTP.setEncryptedOTP(passwordEncoder.encode(otpString));
        }
        existedOTP.setExpirationDate(LocalDateTime.now().plusMinutes(5));
        saveOTP(existedOTP, email);
        sendEmail(email, otpString, type);

        return true;
    }

    private void sendEmail(String email, String otpString, OTP.EType type) {
        String subject = type.equals(OTP.EType.ACTIVE_ACCOUNT) ?
                "This is your OTP for active your account" :
                "This is your OTP for reset your password";
        String htmlEmail = emailService.createHtmlEmailContentWithOTP(otpString, type);
        emailService.sendHtmlContent(email, subject, htmlEmail);
    }

    public String generateOtpString() {
        SecureRandom random = new SecureRandom();
        return String.valueOf(random.nextInt(900000) + 100000);
    }

    private void saveOTP(OTP otp, String email) {
        if (email != null && otp != null) {
            User user = userRepos.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException(String.format("User %s not found", email)));
            otp.setUser(user);
            otpRepos.save(otp);
        } else {
            throw new IllegalArgumentException("Email or OTP cannot be null");
        }
    }

    private OTP checkExistedOTP(String email, OTP.EType type) {
        return otpRepos.findByUserEmailAndType(email, type);
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
