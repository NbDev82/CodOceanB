package com.example.codoceanb.login.service;

import com.example.codoceanb.infras.security.JwtTokenUtils;
import com.example.codoceanb.login.entity.User;
import com.example.codoceanb.login.exception.UserNotFoundException;
import com.example.codoceanb.login.repository.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserRepos userRepos;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtils jwtTokenUtil;

    @Override
    public String login(String email, String password) throws Exception {
        User existingUser = userRepos.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public void changePassword(String token, String newPassword) {
        String email = jwtTokenUtil.extractEmail(token);
        User user = userRepos.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepos.save(user);
    }
}
