package com.example.codoceanb.login.controller;

import com.example.codoceanb.login.dto.UserDTO;
import com.example.codoceanb.login.dto.UserLoginDTO;
import com.example.codoceanb.login.entity.User;
import com.example.codoceanb.login.mapper.UserMapper;
import com.example.codoceanb.login.response.LoginResponse;
import com.example.codoceanb.login.response.RegisterResponse;
import com.example.codoceanb.login.service.UserService;
import com.example.codoceanb.login.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    private final UserService userService;

    @Autowired
    private final UserMapper userMapper;

}
