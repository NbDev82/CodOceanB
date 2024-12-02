package com.example.codoceanb.comment.dto;

import com.example.codoceanb.comment.entity.Comment;
import com.example.codoceanb.discuss.comment.dto.DiscussCommentDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private UUID id;
    private String text;
    private LocalDateTime updatedAt;

    private UUID ownerId;
    private String ownerName;
    private String ownerImageUrl;

    private DiscussCommentDTO.EType type;

    @Getter
    public enum EType {
        COMMENT,
        REPLY,
        UPDATE,
        DELETE
    }
    public Comment toEntity() {
        return Comment.builder()
                .id(id)
                .text(text)
                .updatedAt(updatedAt)
                .build();
    }
}