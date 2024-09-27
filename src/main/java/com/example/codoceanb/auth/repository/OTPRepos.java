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
    @Transactional
    @Modifying
    @Query("DELETE FROM OTP o WHERE o.expirationDate < :now")
    void deleteExpiredOtps(LocalDateTime now);

    OTP findByUserEmailAndType(String email, OTP.EType type);
}
