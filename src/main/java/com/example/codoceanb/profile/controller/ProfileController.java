package com.example.codoceanb.profile.controller;

import com.example.codoceanb.login.service.UserService;
import com.example.codoceanb.profile.response.ProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private static final Logger log = LogManager.getLogger(ProfileController.class);

    @Autowired
    private final UserService userService;

    @GetMapping("/get-profile")
    public ResponseEntity<ProfileResponse> extractProfile(@NonNull HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String token = authHeader.substring(7);
        ProfileResponse profileResponse = userService.getProfile(token);
        return ResponseEntity.ok(profileResponse);
    }
}
