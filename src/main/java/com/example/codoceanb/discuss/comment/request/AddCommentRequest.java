package com.example.codoceanb.discuss.comment.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCommentRequest {
    private UUID discussId;
    private String text;

}
