package com.example.codoceanb.admin.account.controller;

import com.example.codoceanb.admin.account.services.AccountService;
import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.service.UserService;
import com.example.codoceanb.profile.dto.ProfileDTO;
import com.example.codoceanb.profile.response.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/v1/account")
public class AdminManageAccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @PutMapping("/edit-role/{userId}")
    public ResponseEntity<Boolean> editRole(@PathVariable UUID userId, @RequestParam User.ERole newRole) {
        boolean isEdited = accountService.editRole(userId, newRole);
        return ResponseEntity.ok().body(isEdited);
    }

    @PostMapping("/lock-user/{userId}")
    public ResponseEntity<Boolean> lockUser(@PathVariable UUID userId) {
        boolean isLocked = accountService.lockUser(userId);
        return ResponseEntity.ok().body(isLocked);
    }

    @PostMapping("/unlock-user/{userId}")
    public ResponseEntity<Boolean> unlockUser(@PathVariable UUID userId) {
        boolean isUnlocked = accountService.unlockAccount(userId);
        return ResponseEntity.ok().body(isUnlocked);
    }

    @GetMapping("/view-user/{userId}")
    public ResponseEntity<ProfileResponse> viewUser(@PathVariable UUID userId) {
        return ResponseEntity.ok().body(userService.getProfile(userId));
    }

    @GetMapping("/view-users")
    public ResponseEntity<List<ProfileResponse>> viewUsers() {
        return ResponseEntity.ok().body(userService.getProfiles());
    }

    @GetMapping("/edit-user/{userId}")
    public ResponseEntity<ProfileResponse> editUser(@RequestBody ProfileDTO profileDTO,
                                                       @PathVariable(name = "userId") UUID userId) {
        return ResponseEntity.ok().body(userService.changeProfile(userId, profileDTO));
    }
}
