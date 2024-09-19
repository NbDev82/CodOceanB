package com.example.codoceanb.login.service;

import com.example.codoceanb.infras.security.JwtTokenUtils;
import com.example.codoceanb.login.dto.UserDTO;
import com.example.codoceanb.login.entity.User;
import com.example.codoceanb.login.exception.UserNotFoundException;
import com.example.codoceanb.login.mapper.UserMapper;
import com.example.codoceanb.login.repository.UserRepos;
import com.example.codoceanb.profile.dto.ProfileDTO;
import com.example.codoceanb.profile.mapper.ProfileMapper;
import com.example.codoceanb.profile.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final ProfileMapper profileMapper = ProfileMapper.INSTANCE;
    private final UserRepos userRepos;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtil;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepos.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public Boolean createUser(UserDTO userDTO)  {
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
    public User updateUser(UserDTO userDTO, Long userId) {
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
        if (userDTO.getPassword() != null
                && !userDTO.getPassword().isEmpty())
        {

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
    public User getEntityUserById(Long userId) {
        return userRepos.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Could not find any user with id=" + userId));
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepos.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Could not find any user with id=" + userId));
        return userMapper.toDTO(user);
    }
    @Override
    public User getUserDetailsFromToken(String token) throws Exception{
        if(jwtTokenUtil.isTokenExpired(token)){
            throw new Exception("Token is expired");
        }
        String email= jwtTokenUtil.extractEmail(token);
        return userRepos.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found",email)));
    }
}

