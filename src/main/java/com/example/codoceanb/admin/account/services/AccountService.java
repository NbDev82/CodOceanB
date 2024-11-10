package com.example.codoceanb.admin.account.services;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.profile.dto.ProfileDTO;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    boolean editRole(String email, User.ERole newRole);
    boolean lockUser(String email);
    boolean unlockAccount(String email);
    List<ProfileDTO> getProfiles();
    ProfileDTO getProfileByEmail(String email);
    ProfileDTO changeProfileByEmail(String email, ProfileDTO profileDTO);
}
