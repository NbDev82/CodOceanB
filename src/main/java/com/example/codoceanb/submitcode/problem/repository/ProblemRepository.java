package com.example.codoceanb.submitcode.problem.repository;

import com.example.codoceanb.submitcode.problem.entity.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    @Query("SELECT p " +
            "FROM Problem p " +
            "WHERE ( :difficulty IS NULL OR p.difficultyLevel = :difficulty ) " +
            "AND p.isDeleted = false ")
    Page<Problem> findByCriteria(Problem.EDifficultyLevel difficulty, Pageable pageable);


    @Query("SELECT p " +
            "FROM Problem p " +
            "WHERE ( p.isDeleted = false AND p.owner.id = :userId ) ")
    List<Problem> getProblemsByOwner(Long userId);

    @Query("SELECT p " +
            "FROM Problem p " +
            "WHERE ( p.isDeleted = false AND  p.owner.id = :userId AND p.name LIKE %:problemName% )")
    List<Problem> getProblemsByOwnerAndName(Long userId, String problemName);

    @Query("SELECT p " +
            "FROM Problem p " +
            "WHERE ( p.isDeleted = false ) ")
    List<Problem> findAllProblemAvailable();
}
