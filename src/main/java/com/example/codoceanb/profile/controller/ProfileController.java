package com.example.codoceanb.profile.controller;

import com.example.codoceanb.login.service.UserService;
import com.example.codoceanb.profile.dto.ProfileDTO;
import com.example.codoceanb.profile.request.ChangeEmailRequest;
import com.example.codoceanb.profile.response.ProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private static final Logger log = LogManager.getLogger(ProfileController.class);

    @Autowired
    private final UserService userService;

    @GetMapping("/get-profile")
    public ResponseEntity<ProfileResponse> extractProfile(@RequestHeader(value = "Authorization") String token) {
        token = token.substring(7);
        ProfileResponse profileResponse = userService.getProfile(token);
        return ResponseEntity.ok(profileResponse);
    }

    @PostMapping("/change-profile")
    public ResponseEntity<ProfileResponse> changeProfile(@RequestBody ProfileDTO profileDTO,
                                                         @RequestHeader(value = "Authorization") String token) {
        token = token.substring(7);
        ProfileResponse profileResponse = userService.changeProfile(token, profileDTO);
        return ResponseEntity.ok(profileResponse);
    }

    @PostMapping("/change-email")
    public ResponseEntity<ProfileResponse> changeEmail(@RequestBody ChangeEmailRequest request,
                                                       @RequestHeader(value = "Authorization") String token) {
        token = token.substring(7);
        ProfileResponse profileResponse = userService.changeEmail(token, request.getOtp(), request.getNewEmail());
        return ResponseEntity.ok(profileResponse);
    }
}
