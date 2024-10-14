package com.example.codoceanb.auth.service;

import com.example.codoceanb.auth.entity.Token;
import com.example.codoceanb.auth.exception.TokenNotFoundException;
import com.example.codoceanb.auth.repository.TokenRepos;
import com.example.codoceanb.infras.security.JwtTokenUtils;
import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.exception.UserNotFoundException;
import com.example.codoceanb.auth.repository.UserRepos;
import com.example.codoceanb.auth.request.ChangePasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserRepos userRepos;
    @Autowired
    private TokenRepos tokenRepos;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtils jwtTokenUtil;

    @Override
    public String login(String email, String password) {
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
        tokenRepos.findAndDeleteByUserEmail(email);
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        User user = userRepos.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        tokenRepos.findAndDeleteByUserEmail(email);
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepos.save(user);
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        tokenRepos.deleteByToken(refreshToken);
    }

    @Transactional
    @Override
    public String generateAndSaveRefreshToken(String accessToken) {
        String email = jwtTokenUtil.extractEmail(accessToken);
        User user = userRepos.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        String refreshToken = jwtTokenUtil.generateRefreshToken(accessToken);
        Token savedToken = tokenRepos.findByUser(user);

        if(savedToken == null) {
            Token token = Token.builder()
                    .token(refreshToken)
                    .user(user)
                    .build();
            tokenRepos.save(token);
        } else {
            savedToken.setToken(refreshToken);
            tokenRepos.save(savedToken);
        }

        return refreshToken;
    }

    @Override
    public String refreshToken(String refreshToken) {
        Token token = tokenRepos.findByToken(refreshToken)
                .orElseThrow(() ->  new TokenNotFoundException("Token not found"));
        return jwtTokenUtil.generateToken(token.getUser());
    }
}
