package com.example.codoceanb.submitcode.submission.repository;

import com.example.codoceanb.login.entity.User;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByUserAndProblem(User user, Problem problem);
    List<Submission> findByUser(User user);
}
