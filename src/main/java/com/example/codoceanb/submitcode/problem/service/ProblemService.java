package com.example.codoceanb.submitcode.problem.service;

import com.example.codoceanb.comment.dto.CommentDTO;
import com.example.codoceanb.statistic.dto.TrendingProblemDTO;
import com.example.codoceanb.submitcode.DTO.ProblemDTO;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.request.AddProblemCommentRequest;
import com.example.codoceanb.submitcode.request.AddProblemRequest;

import java.util.List;
import java.util.UUID;

public interface ProblemService {
    Problem getEntityByProblemId(UUID problemId);
    <T> T findById(UUID problemId, Class<T> returnType);
    List<Problem> getAll();
    List<ProblemDTO> getAllDTOs();
    Boolean add(AddProblemRequest request, String authHeader);
    Boolean delete(UUID problemId);
    Problem getRandomProblem();
    List<TrendingProblemDTO> getTopProblemsByTopic(String topic, int limit);
    List<ProblemDTO> getAllUploadedProblemsByUser(String authHeader);
    List<ProblemDTO> getAllSolvedProblemsByUser(String authHeader);
    List<TrendingProblemDTO> getTopProblems(int limit);

    String getCorrectAnswer(UUID problemId);

    List<CommentDTO> getCommentsByProblem(UUID problemId);

    CommentDTO createComment(AddProblemCommentRequest request, String authHeader);
}
