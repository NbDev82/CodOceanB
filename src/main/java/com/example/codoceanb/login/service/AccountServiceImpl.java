package com.example.codoceanb.login.service;

import com.example.codoceanb.infras.security.JwtTokenUtils;
import com.example.codoceanb.login.entity.User;
import com.example.codoceanb.login.exception.UserNotFoundException;
import com.example.codoceanb.login.repository.UserRepos;
import com.example.codoceanb.login.request.ChangePasswordRequest;
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
    public void changePassword(String bearerToken, ChangePasswordRequest request) {
        String email = jwtTokenUtil.extractEmailFromBearerToken(bearerToken);
        User user = userRepos.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Mật khẩu cũ không đúng");
        }
        
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        userRepos.save(user);
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        User user = userRepos.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng!"));
        
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepos.save(user);
    }
}
