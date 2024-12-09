package com.example.codoceanb.submitcode.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddProblemCommentRequest {
    private UUID problemId;
    private String text;
}
