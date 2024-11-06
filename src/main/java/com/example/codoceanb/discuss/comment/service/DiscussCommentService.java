package com.example.codoceanb.discuss.comment.service;

import com.example.codoceanb.discuss.comment.dto.DiscussCommentDTO;
import com.example.codoceanb.discuss.comment.request.AddCommentRequest;
import com.example.codoceanb.discuss.comment.request.ReplyCommentRequest;
import com.example.codoceanb.discuss.comment.request.UpdateCommentRequest;

import java.util.List;
import java.util.UUID;

public interface DiscussCommentService {
    DiscussCommentDTO createComment(AddCommentRequest request, String authHeader);

    List<DiscussCommentDTO> getAllCommentsByDiscussId(UUID discussId);

    DiscussCommentDTO getCommentById(UUID id);

    DiscussCommentDTO updateComment(UUID id, UpdateCommentRequest request);

    boolean deleteComment(UUID id);

    DiscussCommentDTO reply(String authHeader, ReplyCommentRequest request);

    List<DiscussCommentDTO> getReplies(UUID commentId);
}
