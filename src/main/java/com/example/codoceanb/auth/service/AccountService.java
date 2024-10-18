package com.example.codoceanb.auth.service;

import com.example.codoceanb.auth.request.ChangePasswordRequest;

public interface AccountService {
    String login(String email, String password) throws Exception;
    void changePassword(String bearerToken, ChangePasswordRequest request);

    void resetPassword(String email, String newPassword);

    void deleteRefreshToken(String refreshToken);

    String generateAndSaveRefreshToken(String accessToken);

    String refreshToken(String refreshToken);
}
