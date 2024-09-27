package com.example.codoceanb.auth.repository;

import com.example.codoceanb.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepos extends JpaRepository<User, Long> {
    boolean existsByPhoneNumberOrEmail(String phoneNumber, String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
