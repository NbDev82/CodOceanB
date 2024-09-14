package com.example.codoceanb.infras.service;

import com.example.codoceanb.login.entity.OTP;

public interface EmailService {
    void sendHtmlContent(String toEmail, String subject, String htmlBody);

    String createHtmlEmailContentWithOTP(String otpString, OTP.EType type);
}
