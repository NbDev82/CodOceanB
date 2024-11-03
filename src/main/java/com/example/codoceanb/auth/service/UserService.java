package com.example.codoceanb.auth.service;

import com.example.codoceanb.auth.dto.UserDTO;
import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.profile.dto.ProfileDTO;
import com.example.codoceanb.profile.response.ProfileResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@Service
public interface UserService {
    Boolean createUser(UserDTO userDTO);
    User getUserDetailsFromToken(String token);
    User getUserDetailsFromCleanToken(String token);
    ProfileResponse getProfile(String token);
    ProfileResponse getProfile(UUID userId);
    ProfileResponse changeProfile(String token, ProfileDTO profileDTO);
    ProfileResponse changeProfile(UUID userId, ProfileDTO profileDTO);
    User getEntityUserById(UUID userId);
    UserDTO getUserById(UUID userId);

    ProfileResponse changeEmail(String token, String otp, String newEmail);
    String changeAvatar(String authHeader, MultipartFile file);
    UserDTO getCurrentUser(String authHeader);
    List<ProfileResponse> getProfiles();
}
