package com.example.codoceanb.submitcode.DTO;

import com.example.codoceanb.submitcode.submission.entity.Submission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestCaseResultDTO {
    private String input;

    private String outputData;
    private String outputDatatype;

    private String expected;
    private String expectedDatatype;

    private boolean isPassed;
    private Submission.EStatus status;

    public TestCaseResultDTO(Submission.EStatus status) {
        this.status = status;
    }
}
