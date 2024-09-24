package com.example.codoceanb.discuss.repository;

import com.example.codoceanb.discuss.entity.Discuss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussRepository extends JpaRepository<Discuss, Long> {
    List<Discuss> findByOwnerEmail(String email);
}
