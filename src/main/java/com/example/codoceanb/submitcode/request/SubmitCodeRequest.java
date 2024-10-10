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
    protected String code;
    protected String language;
    protected UUID problemId;
}

