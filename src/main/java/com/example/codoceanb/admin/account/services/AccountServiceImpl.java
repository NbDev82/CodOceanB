package com.example.codoceanb.admin.account.services;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.exception.UserNotFoundException;
import com.example.codoceanb.auth.repository.UserRepos;
import com.example.codoceanb.profile.dto.ProfileDTO;
import com.example.codoceanb.profile.mapper.ProfileMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("adminAccountServiceImpl")
public class AccountServiceImpl implements AccountService{
    private final Logger log = LogManager.getLogger(AccountServiceImpl.class);

    @Autowired
    private UserRepos userRepos;

    private final ProfileMapper profileMapper = ProfileMapper.INSTANCE;
    @Override
    public boolean editRole(String email, User.ERole newRole) {
        User user = userRepos.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setRole(newRole);
        if(newRole.equals(User.ERole.USER)) {
            user.setVIPExpDate(null);
        }
        userRepos.save(user);
        return true;
    }

    @Override
    public boolean lockUser(String email) {
        User user = userRepos.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setLocked(true);
        userRepos.save(user);
        return true;
    }

    @Override
    public boolean unlockAccount(String email) {
        User user = userRepos.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setLocked(false);
        userRepos.save(user);
        return true;
    }

    @Override
    public List<ProfileDTO> getProfiles() {
        try {
            List<User> users = userRepos.findUsers();
            return profileMapper.toDTOs(users);
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public ProfileDTO getProfileByEmail(String email) {
        try {
            User user = userRepos.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found!"));
            return profileMapper.toDTO(user);
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public ProfileDTO changeProfileByEmail(String email, ProfileDTO profileDTO) {
        try {
            User user = userRepos.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("user not found"));
            if (profileDTO.getFullName() != null) {
                user.setFullName(profileDTO.getFullName());
            }
            if (profileDTO.getPhoneNumber() != null) {
                user.setPhoneNumber(profileDTO.getPhoneNumber());
            }
            if (profileDTO.getDateOfBirth() != null) {
                user.setDateOfBirth(profileDTO.getDateOfBirth());
            }
            user.setRole(profileDTO.getRole());
            user.setVIPExpDate(null);

            if (profileDTO.getVIPExpDate() != null) {
                user.setVIPExpDate(profileDTO.getVIPExpDate());
                user.setRole(User.ERole.USER_VIP);
            }

            user.setUpdatedAt(LocalDateTime.now());
            userRepos.save(user);
            return profileMapper.toDTO(user);
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }
}
