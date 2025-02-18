package com.example.codoceanb.submitcode.problem.repository;

import com.example.codoceanb.submitcode.problem.entity.Problem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, UUID> {

@Query("SELECT p " +
       "FROM Problem p " +
       "WHERE p.isDeleted = false " +
       "AND ( :difficulty IS NULL OR p.difficulty = :difficulty ) " +
       "AND (:topic IS NULL OR :topic MEMBER OF p.topics) " +
       "AND (:searchTerm LIKE '' OR LOWER(p.title) LIKE %:searchTerm%)")
Page<Problem> findByCriteria(Problem.EDifficulty difficulty, Problem.ETopic topic, String searchTerm, Pageable pageable);


    @Query("SELECT p " +
            "FROM Problem p " +
            "WHERE ( p.isDeleted = false AND p.owner.email = :email ) ")
    List<Problem> getProblemsByOwner(String email);

    @Query("SELECT p " +
            "FROM Problem p " +
            "WHERE ( p.isDeleted = false AND  p.owner.id = :userId AND p.title LIKE %:problemName% )")
    List<Problem> getProblemsByOwnerAndName(Long userId, String problemName);

    @Query("SELECT p " +
            "FROM Problem p " +
            "WHERE ( p.isDeleted = false ) ")
    List<Problem> findAllProblemAvailable();

    @Query("SELECT p " +
            "FROM Problem p " +
            "JOIN p.topics t " +
            "WHERE t IN :topics AND size(p.submissions) != 0 " +
            "ORDER BY size(p.submissions) DESC")
    List<Problem> findTopByTopicsOrderBySubmissionsDesc(@Param("topics") List<Problem.ETopic> topics, Pageable pageable);

    @Query("SELECT p " +
            "FROM Problem p " +
            "JOIN p.topics t " +
            "WHERE size(p.submissions) != 0 " +
            "ORDER BY size(p.submissions) DESC")
    List<Problem> findTopByOrderBySubmissionsDesc(Pageable pageable);

    @Query("SELECT DISTINCT p FROM Problem p " +
           "JOIN p.submissions s " +
           "JOIN s.user u " +
           "WHERE u.email = :email AND s.status = 'ACCEPTED' AND p.isDeleted = false")
    List<Problem> findSolvedProblemsByUser(@Param("email") String email);


    @Query("SELECT p FROM Problem p WHERE p.isDeleted = false")
    List<Problem> getAll();
}
