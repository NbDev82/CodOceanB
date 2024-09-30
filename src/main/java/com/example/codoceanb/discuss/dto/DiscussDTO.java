package com.example.codoceanb.discuss.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscussDTO {
    private UUID id;
    private String topic;
    private String content;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime endAt;
    private String image;
    private String commentCount;
}
