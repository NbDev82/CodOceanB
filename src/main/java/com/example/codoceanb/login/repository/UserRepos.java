package com.example.codoceanb.login.repository;

import com.example.codoceanb.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepos extends JpaRepository<User, Long> {
    boolean existsByPhoneNumberOrEmail(String phoneNumber, String email);
    boolean existsByPhoneNumber(String phoneNumber);
    User findByEmail(String email);
}
