package com.example.codoceanb.discuss.comment.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscussCommentDTO {
    private UUID id;
    private String text;
    private LocalDateTime updatedAt;

    private UUID ownerId;
    private String ownerName;
    private String ownerImageUrl;
}
