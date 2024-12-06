package com.example.codoceanb.submitcode.problem.service;

import com.example.codoceanb.profile.dto.TestCaseDTO;
import com.example.codoceanb.submitcode.DTO.ProblemHintDTO;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.testcase.entity.TestCase;

import java.util.List;
import java.util.UUID;

public interface ProfileProblemService {
    void updateTitle(UUID id, String title);

    void updateDifficulty(UUID id, Problem.EDifficulty difficulty);

    void updateDescription(UUID id, String description);

    void updateProblemHint(UUID problemId, ProblemHintDTO problemHintDTO);

    List<TestCaseDTO> getTestCases(UUID problemId);

    void lockHint(UUID problemId);

    void unlockHint(UUID problemId);

//    void updateTestCases(UUID problemId, List<TestCaseDTO> testCases);
}
