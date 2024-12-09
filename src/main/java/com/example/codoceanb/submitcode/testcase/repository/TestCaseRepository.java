package com.example.codoceanb.submitcode.testcase.repository;

import com.example.codoceanb.submitcode.testcase.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, UUID> {

    @Query(
            "SELECT t " +
            "FROM TestCase t " +
            "WHERE t.problem.id = :problemId " +
            "AND t.isPublic = TRUE "
    )
    List<TestCase> getPublicTestCasesByProblemId(UUID problemId);

    @Transactional
    @Modifying
    @Query(
            "DELETE FROM TestCase tc WHERE tc.id IN :uuids"
    )
    void deleteAllById(List<UUID> uuids);
}
