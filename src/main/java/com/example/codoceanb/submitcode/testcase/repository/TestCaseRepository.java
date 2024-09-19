package com.example.codoceanb.submitcode.testcase.repository;

import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.testcase.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> getTestCasesByProblem(Problem problem);
}
