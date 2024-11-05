package com.example.codoceanb.auth.controller;

import com.example.codoceanb.auth.dto.UserDTO;
import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.mapper.UserMapper;
import com.example.codoceanb.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    private final UserService userService;

    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        UserDTO user =userService.getCurrentUser(authHeader);
        return ResponseEntity.ok(user);
    }
}
