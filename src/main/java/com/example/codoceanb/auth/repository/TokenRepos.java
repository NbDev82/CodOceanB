package com.example.codoceanb.auth.repository;

import com.example.codoceanb.auth.entity.Token;
import com.example.codoceanb.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepos extends JpaRepository<Token, UUID> {
    void deleteByToken(String token);

    Optional<Token> findByToken(String refreshToken);

    void findAndDeleteByUserEmail(String email);

    Token findByUser(User user);
}
