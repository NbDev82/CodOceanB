package com.example.codoceanb.auth.repository;

import com.example.codoceanb.auth.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface OTPRepos extends JpaRepository<OTP, UUID> {
    OTP findByUserEmailAndType(String email, OTP.EType type);

    OTP findByUserEmail(String email);
}
