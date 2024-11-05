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

    @PutMapping("/edit-role/{email}")
    public ResponseEntity<Boolean> editRole(@PathVariable String email, @RequestParam User.ERole newRole) {
        boolean isEdited = accountService.editRole(email, newRole);
        return ResponseEntity.ok().body(isEdited);
    }

    @PutMapping("/lock-user/{email}")
    public ResponseEntity<Boolean> lockUser(@PathVariable String email) {
        boolean isLocked = accountService.lockUser(email);
        return ResponseEntity.ok().body(isLocked);
    }

    @PutMapping("/unlock-user/{email}")
    public ResponseEntity<Boolean> unlockUser(@PathVariable String email) {
        boolean isUnlocked = accountService.unlockAccount(email);
        return ResponseEntity.ok().body(isUnlocked);
    }

    @GetMapping("/view-user/{email}")
    public ResponseEntity<ProfileDTO> viewUser(@PathVariable String email) {
        return ResponseEntity.ok().body(accountService.getProfileByEmail(email));
    }

    @GetMapping("/view-users")
    public ResponseEntity<List<ProfileDTO>> viewUsers() {
        return ResponseEntity.ok().body(accountService.getProfiles());
    }

    @PutMapping ("/edit-user/{email}")
    public ResponseEntity<ProfileDTO> editUser(@RequestBody ProfileDTO profileDTO,
                                                       @PathVariable(name = "email") String email) {
        return ResponseEntity.ok().body(accountService.changeProfileByEmail(email, profileDTO));
    }
}
