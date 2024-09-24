package com.example.codoceanb.login.service;

import com.example.codoceanb.login.entity.OTP;

public interface OTPService {
    boolean requestOTP(String tokenOrEmail, OTP.EType type);
    boolean verify(String tokenOrEmail, String otp, OTP.EType eType);
}
