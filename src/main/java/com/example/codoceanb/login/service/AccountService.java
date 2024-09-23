package com.example.codoceanb.login.service;

public interface AccountService {
    String login(String email, String password) throws Exception;
    void changePassword(String token, String newPassword);
}
