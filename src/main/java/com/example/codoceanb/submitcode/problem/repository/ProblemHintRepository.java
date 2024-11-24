package com.example.codoceanb.submitcode.problem.repository;

import com.example.codoceanb.submitcode.problem.entity.ProblemHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProblemHintRepository extends JpaRepository<ProblemHint, UUID> {
}
