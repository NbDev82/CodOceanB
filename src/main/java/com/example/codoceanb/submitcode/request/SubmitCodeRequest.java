package com.example.codoceanb.submitcode.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubmitCodeRequest {
    private String code;
    private String language;
    private UUID problemId;
}

