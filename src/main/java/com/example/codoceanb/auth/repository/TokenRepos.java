package com.example.codoceanb.auth.repository;

import com.example.codoceanb.auth.entity.Token;
import com.example.codoceanb.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepos extends JpaRepository<Token, UUID> {
    void deleteByToken(String token);

    Optional<Token> findByToken(String refreshToken);

    Token findByUser(User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM Token t WHERE t.user.id = :id")
    void deleteByUserId(@Param("id") UUID id);
}
