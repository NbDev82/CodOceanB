package com.example.codoceanb.submitcode.submission.service;

import com.example.codoceanb.submitcode.DTO.ResultDTO;
import com.example.codoceanb.submitcode.DTO.SubmissionDTO;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.submission.entity.Submission;

import java.util.List;

public interface SubmissionService {
    String getInputCode(Problem problem, Submission.ELanguage language);
    ResultDTO compile(String code, Submission.ELanguage eLanguage);
    ResultDTO runCode(Long userId, String code, Problem problem, Submission.ELanguage eLanguage);
    List<SubmissionDTO> getByUserIdAndProblemId(Long userId, Long problemId);
    <T> List<T> getByUserId(long userId, Class<T> returnType);
}
