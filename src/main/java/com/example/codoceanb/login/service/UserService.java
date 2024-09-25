package com.example.codoceanb.login.service;

import com.example.codoceanb.login.dto.UserDTO;
import com.example.codoceanb.login.entity.User;
import com.example.codoceanb.profile.dto.ProfileDTO;
import com.example.codoceanb.profile.response.ProfileResponse;
import org.springframework.stereotype.Service;


@Service
public interface UserService {
    Boolean createUser(UserDTO userDTO);
    User getUserDetailsFromToken(String token )throws Exception;
    User getUserDetailsFromCleanToken(String token )throws Exception;
    User updateUser(UserDTO userDTO, Long userId);
    ProfileResponse getProfile(String token);
    ProfileResponse changeProfile(String token, ProfileDTO profileDTO);
    User getEntityUserById(Long userId);
    UserDTO getUserById(Long userId);

    ProfileResponse changeEmail(String token, String otp, String newEmail);
}
