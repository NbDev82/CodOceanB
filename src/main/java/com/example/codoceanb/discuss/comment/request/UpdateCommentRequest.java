package com.example.codoceanb.discuss.comment.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCommentRequest{
    private UUID commentId;
    private String text;
}
