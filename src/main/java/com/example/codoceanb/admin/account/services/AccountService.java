package com.example.codoceanb.admin.account.services;

import com.example.codoceanb.auth.entity.User;

import java.util.UUID;

public interface AccountService {
    boolean editRole(UUID userId, User.ERole newRole);
    boolean lockUser(UUID userId);
    boolean unlockAccount(UUID userId);
}
