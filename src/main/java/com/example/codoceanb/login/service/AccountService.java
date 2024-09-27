package com.example.codoceanb.login.service;

import com.example.codoceanb.login.request.ChangePasswordRequest;

public interface AccountService {
    String login(String email, String password) throws Exception;
    void changePassword(String bearerToken, ChangePasswordRequest request);

    void resetPassword(String email, String newPassword);
}
