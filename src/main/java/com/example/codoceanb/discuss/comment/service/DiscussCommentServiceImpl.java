package com.example.codoceanb.discuss.comment.service;

import com.example.codoceanb.auth.service.UserService;
import com.example.codoceanb.comment.entity.Comment;
import com.example.codoceanb.discuss.comment.dto.DiscussCommentDTO;
import com.example.codoceanb.discuss.comment.exception.CommentNotFoundException;
import com.example.codoceanb.discuss.comment.exception.InvalidCommentLengthException;
import com.example.codoceanb.discuss.comment.repository.DiscussCommentRepository;
import com.example.codoceanb.discuss.comment.request.AddCommentRequest;
import com.example.codoceanb.discuss.comment.request.ReplyCommentRequest;
import com.example.codoceanb.discuss.comment.request.UpdateCommentRequest;
import com.example.codoceanb.discuss.service.DiscussService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class DiscussCommentServiceImpl implements DiscussCommentService{
    private static final Logger log = LogManager.getLogger(DiscussCommentServiceImpl.class);

    @Autowired
    private DiscussCommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DiscussService discussService;

    @Autowired
    private DiscussCommentRepository discussCommentRepository;

    @Override
    public DiscussCommentDTO createComment(AddCommentRequest request, String authHeader) {
        Comment comment = Comment.builder()
                .text(request.getText())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .user(userService.getUserDetailsFromToken(authHeader))
                .discuss(discussService.getDiscuss(request.getDiscussId()))
                .build();
        Comment savedComment = commentRepository.save(comment);
        return convertToDTO(savedComment);
    }

    @Override
    public List<DiscussCommentDTO> getAllCommentsByDiscussId(UUID discussId) {
        List<Comment> comments = commentRepository.findAllByDiscussId(discussId);
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DiscussCommentDTO getCommentById(UUID id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));
        return convertToDTO(comment);
    }

    @Override
    public DiscussCommentDTO updateComment(UUID id, UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));
        comment.setText(request.getText());
        comment.setUpdatedAt(LocalDateTime.now());

        Comment updatedComment = commentRepository.save(comment);
        return convertToDTO(updatedComment);
    }

    @Override
    public DiscussCommentDTO deleteComment(UUID id) {
        try {
            Comment comment = commentRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
            comment.setDeleted(true);
            commentRepository.save(comment);
            return convertToDTO(comment);
        } catch (Exception e) {
            log.error("Error deleting comment: ", e);
            throw new RuntimeException("Unable to delete comment", e);
        }
    }

    @Override
    public DiscussCommentDTO reply(String authHeader, ReplyCommentRequest request) {
        if (request.getCommentId() == null)
            return null;
        Comment commentParent = discussCommentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new CommentNotFoundException("Comment not found or had deleted!"));

        if(commentParent.isDeleted()) {
            throw new CommentNotFoundException("Comment not found or had deleted!");
        }

        Comment comment = Comment.builder()
                .text(request.getText())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .user(userService.getUserDetailsFromToken(authHeader))
                .commentParent(commentParent)
                .build();
        Comment saved = commentRepository.save(comment);

        return convertToDTO(saved);
    }

    @Override
    public List<DiscussCommentDTO> getReplies(UUID commentId) {
        List<Comment> comments = commentRepository.findByCommentParentId(commentId);

        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DiscussCommentDTO convertToDTO(Comment comment) {
        try{
            return DiscussCommentDTO.builder()
                    .id(comment.getId())
                    .text(comment.getText())
                    .updatedAt(comment.getUpdatedAt())
                    .ownerId(comment.getUser().getId())
                    .ownerName(comment.getUser().getFullName())
                    .ownerImageUrl(comment.getUser().getUrlImage())
                    .build();
        } catch (Exception e) {
            log.error("Error in convertToDTO DiscussCommentDTO: " + e.getMessage());
            return null;
        }
    }
}
