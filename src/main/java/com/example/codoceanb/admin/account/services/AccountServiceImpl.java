package com.example.codoceanb.admin.account.services;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.exception.UserNotFoundException;
import com.example.codoceanb.auth.repository.UserRepos;
import com.example.codoceanb.auth.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("adminAccountServiceImpl")
public class AccountServiceImpl implements AccountService{
    private final Logger log = LogManager.getLogger(AccountServiceImpl.class);

    @Autowired
    private UserRepos userRepos;

    @Override
    public boolean editRole(UUID userId, User.ERole newRole) {
        User user = userRepos.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setRole(newRole);
        userRepos.save(user);
        return true;
    }

    @Override
    public boolean lockUser(UUID userId) {
        User user = userRepos.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setLocked(true);
        userRepos.save(user);
        return true;
    }

    @Override
    public boolean unlockAccount(UUID userId) {
        User user = userRepos.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setLocked(false);
        userRepos.save(user);
        return true;
    }
}
