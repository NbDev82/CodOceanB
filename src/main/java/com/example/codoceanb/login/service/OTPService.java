package com.example.codoceanb.login.service;

import com.example.codoceanb.login.entity.OTP;
import com.example.codoceanb.login.response.VerifyOTPResponse;

public interface OTPService {
    boolean requestOTP(String tokenOrEmail, OTP.EType type);

    VerifyOTPResponse verify(String tokenOrEmail, String otp, OTP.EType eType);
}
