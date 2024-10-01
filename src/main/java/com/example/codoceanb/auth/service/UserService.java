package com.example.codoceanb.auth.service;

import com.example.codoceanb.auth.dto.UserDTO;
import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.profile.dto.ProfileDTO;
import com.example.codoceanb.profile.response.ProfileResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public interface UserService {
    Boolean createUser(UserDTO userDTO);
    User getUserDetailsFromToken(String token);
    User getUserDetailsFromCleanToken(String token);
    User updateUser(UserDTO userDTO, UUID userId);
    ProfileResponse getProfile(String token);
    ProfileResponse changeProfile(String token, ProfileDTO profileDTO);
    User getEntityUserById(UUID userId);
    UserDTO getUserById(UUID userId);

    ProfileResponse changeEmail(String token, String otp, String newEmail);
}
