package com.example.codoceanb.submitcode.controller;

import com.example.codoceanb.comment.dto.CommentDTO;
import com.example.codoceanb.discuss.comment.dto.DiscussCommentDTO;
import com.example.codoceanb.submitcode.problem.service.ProblemService;
import com.example.codoceanb.submitcode.request.AddProblemCommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/problem-comments")
public class ProblemCommentController {

    @Autowired
    public ProblemService problemService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("{problemId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByProblemIdId(@PathVariable("problemId") UUID problemId) {
        return ResponseEntity.ok(problemService.getCommentsByProblem(problemId));
    }

    @PostMapping
    public ResponseEntity<Boolean> createComment(@RequestBody AddProblemCommentRequest request,
                                                          @RequestHeader("Authorization") String authHeader) {

        CommentDTO commentDTO = problemService.createComment(request, authHeader);
        commentDTO.setType(DiscussCommentDTO.EType.COMMENT);

        messagingTemplate.convertAndSend(
                "/topic/problem-comments/" + request.getProblemId(),
                commentDTO
        );

        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }
}
