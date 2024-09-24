package com.example.codoceanb.submitcode.problem.service;

import com.example.codoceanb.submitcode.DTO.ProblemDTO;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.request.AddProblemRequest;

import java.util.List;

public interface ProblemService {
    Problem getEntityByProblemId(Long problemId);
    <T> T findById(Long problemId, Class<T> returnType);
    List<Problem> getAll();
    List<ProblemDTO> getAllDTOs();
    Boolean add(AddProblemRequest request);
    Boolean delete(Long problemId);
    Problem getRandomProblem();
    List<ProblemDTO> getTopProblemsByTopic(String topic, int limit);
    List<ProblemDTO> getAllUploadedProblemsByUser(String token);

    List<ProblemDTO> getAllSolvedProblemsByUser(String token);
}
