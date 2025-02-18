package com.example.codoceanb.discuss.comment.controller;

import com.example.codoceanb.discuss.comment.request.AddCommentRequest;
import com.example.codoceanb.discuss.comment.request.ReplyCommentRequest;
import com.example.codoceanb.discuss.comment.request.UpdateCommentRequest;
import com.example.codoceanb.discuss.comment.service.DiscussCommentService;
import com.example.codoceanb.discuss.comment.dto.DiscussCommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/discuss/comments")
@RequiredArgsConstructor
public class DiscussCommentController {

    @Autowired
    private DiscussCommentService commentService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public ResponseEntity<Boolean> createComment(@RequestBody AddCommentRequest request,
                                                           @RequestHeader("Authorization") String authHeader) {
        DiscussCommentDTO commentDTO = commentService.createComment(request, authHeader);
        commentDTO.setType(DiscussCommentDTO.EType.COMMENT);

        messagingTemplate.convertAndSend(
                "/topic/discuss/" + request.getDiscussId(),
                commentDTO
        );

        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DiscussCommentDTO>> getAllComments(@RequestParam UUID discussId) {
        List<DiscussCommentDTO> commentDTOs = commentService.getAllCommentsByDiscussId(discussId);
        return new ResponseEntity<>(commentDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscussCommentDTO> getCommentById(@PathVariable UUID id) {
        DiscussCommentDTO comment = commentService.getCommentById(id);
        return comment != null ? new ResponseEntity<>(comment, HttpStatus.OK)
                               : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateComment(@PathVariable UUID id, @RequestBody UpdateCommentRequest request) {
        DiscussCommentDTO updatedComment = commentService.updateComment(id, request);
        updatedComment.setType(DiscussCommentDTO.EType.UPDATE);

        messagingTemplate.convertAndSend(
                "/topic/discuss-comment/" + request.getCommentId(),
                updatedComment
        );

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DiscussCommentDTO> deleteComment(@PathVariable UUID id) {
        DiscussCommentDTO deletedComment = commentService.deleteComment(id);
        return new ResponseEntity<>(deletedComment, HttpStatus.ACCEPTED);
    }

    @PostMapping("/reply")
    public ResponseEntity<Boolean> replyComment(@RequestBody ReplyCommentRequest request,
                                                          @RequestHeader("Authorization") String authHeader) {
        DiscussCommentDTO replyComment = commentService.reply(authHeader, request);
        replyComment.setType(DiscussCommentDTO.EType.REPLY);

        messagingTemplate.convertAndSend(
                "/topic/discuss-comment/" + request.getCommentId(),
                replyComment
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(true);
    }

    @GetMapping("/replies")
    public ResponseEntity<List<DiscussCommentDTO>> getReplyComments(@RequestParam UUID commentId) {
        List<DiscussCommentDTO> commentDTOs = commentService.getReplies(commentId);
        return ResponseEntity.ok(commentDTOs);
    }
}
