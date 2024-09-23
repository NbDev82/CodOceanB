package com.example.codoceanb.submitcode.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubmitCodeRequest {
    private Long userId;
    private String code;
    private String language;
    private Long problemId;
}

