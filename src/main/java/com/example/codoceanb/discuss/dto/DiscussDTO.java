package com.example.codoceanb.discuss.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscussDTO {
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime endAt;
    private String image;
    private int commentCount;
    private int reactCount;

    private UUID ownerId;
    private String ownerName;
    private String ownerImageUrl;
}
