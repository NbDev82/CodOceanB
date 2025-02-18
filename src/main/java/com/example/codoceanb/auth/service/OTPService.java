package com.example.codoceanb.auth.service;

import com.example.codoceanb.auth.entity.OTP;

public interface OTPService {
    boolean requestOTP(String tokenOrEmail, OTP.EType type);
    boolean verify(String tokenOrEmail, String otp, OTP.EType eType);
}
