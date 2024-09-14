package com.example.codoceanb.login.service;

import com.example.codoceanb.login.dto.UserDTO;
import com.example.codoceanb.login.entity.User;
import com.example.codoceanb.profile.response.ProfileResponse;
import org.springframework.stereotype.Service;


@Service
public interface UserService {
    Boolean createUser(UserDTO userDTO);
    String login(String phoneNumber, String password) throws Exception;
    User getUserDetailsFromToken(String token )throws Exception;
    User updateUser(UserDTO userDTO, Long userId);
    ProfileResponse getProfile(String token);
}
