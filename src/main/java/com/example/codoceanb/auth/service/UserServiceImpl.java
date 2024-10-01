package com.example.codoceanb.auth.service;

import com.example.codoceanb.infras.security.JwtTokenUtils;
import com.example.codoceanb.auth.dto.UserDTO;
import com.example.codoceanb.auth.entity.OTP;
import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.exception.UserNotFoundException;
import com.example.codoceanb.auth.mapper.UserMapper;
import com.example.codoceanb.auth.repository.UserRepos;
import com.example.codoceanb.profile.dto.ProfileDTO;
import com.example.codoceanb.profile.mapper.ProfileMapper;
import com.example.codoceanb.profile.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final ProfileMapper profileMapper = ProfileMapper.INSTANCE;
    private final UserRepos userRepos;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtil;

    private final OTPService otpService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepos.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public Boolean createUser(UserDTO userDTO) {
        String email = userDTO.getEmail();
        String phoneNumber = userDTO.getPhoneNumber();
        userDTO.setRole(User.ERole.USER);

        if (userRepos.existsByPhoneNumberOrEmail(phoneNumber, email)) {
            throw new DataIntegrityViolationException("Account already exits");
        }

        User user = userMapper.toEntity(userDTO);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        String password = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        userRepos.save(user);
        return true;
    }

    @Transactional
    @Override
    public User updateUser(UserDTO userDTO, UUID userId) {
        User existingUser = userRepos.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Could not find any user with id=" + userId));
        String newPhoneNumber = userDTO.getPhoneNumber();
        if (!existingUser.getPhoneNumber().equals(newPhoneNumber) && userRepos.existsByPhoneNumber(newPhoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        if (userDTO.getFullName() != null) {
            existingUser.setFullName(userDTO.getFullName());
        }
        if (userDTO.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        }
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            String newPassword = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encodedPassword);
        }
        return userRepos.save(existingUser);
    }

    @Override
    public ProfileResponse getProfile(String token) {
        try {
            User user = getUserDetailsFromToken(token);
            ProfileDTO profileDTO = profileMapper.toDTO(user);
            return ProfileResponse.builder()
                    .profile(profileDTO)
                    .build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public ProfileResponse changeProfile(String token, ProfileDTO profileDTO) {
        try {
            User user = getUserDetailsFromToken(token);
            if (profileDTO.getFullName() != null) {
                user.setFullName(profileDTO.getFullName());
            }
            if (profileDTO.getPhoneNumber() != null) {
                user.setPhoneNumber(profileDTO.getPhoneNumber());
            }
            if (profileDTO.getDateOfBirth() != null) {
                user.setDateOfBirth(profileDTO.getDateOfBirth());
            }
            user.setUpdatedAt(LocalDateTime.now());
            userRepos.save(user);
            ProfileDTO updatedProfileDTO = profileMapper.toDTO(user);
            return ProfileResponse.builder()
                    .profile(updatedProfileDTO)
                    .build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public ProfileResponse changeEmail(String token, String otp, String newEmail) {
        try {
            boolean isSuccessful = otpService.verify(token, otp, OTP.EType.CHANGE_EMAIL);
            if(isSuccessful) {
                User user = getUserDetailsFromToken(token);
                if (newEmail != null && !newEmail.isEmpty()) {
                    if (userRepos.existsByEmail(newEmail)) {
                        throw new DataIntegrityViolationException("Email already exists");
                    }
                    user.setEmail(newEmail);
                    user.setUpdatedAt(LocalDateTime.now());
                    userRepos.save(user);
                    ProfileDTO updatedProfileDTO = profileMapper.toDTO(user);
                    return ProfileResponse.builder()
                            .profile(updatedProfileDTO)
                            .build();
                } else {
                    throw new IllegalArgumentException("New email must not be null or empty");
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }
    
    @Override
    public User getEntityUserById(UUID userId) {
        return userRepos.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Could not find any user with id=" + userId));
    }

    @Override
    public UserDTO getUserById(UUID userId) {
        User user = userRepos.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Could not find any user with id=" + userId));
        return userMapper.toDTO(user);
    }

    @Override
    public User getUserDetailsFromToken(String token){
        String email = jwtTokenUtil.extractEmailFromBearerToken(token);
        return userRepos.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", email)));
    }

    @Override
    public User getUserDetailsFromCleanToken(String token){
        String email = jwtTokenUtil.extractEmail(token);
        return userRepos.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", email)));
    }
}
