package com.example.codoceanb.admin.account.services;

import com.example.codoceanb.auth.entity.User;

import java.util.UUID;

public interface AccountService {
    boolean editRole(String email, User.ERole newRole);
    boolean lockUser(String email);
    boolean unlockAccount(String email);
}
