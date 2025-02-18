package com.example.codoceanb.submitcode.DTO;

import com.example.codoceanb.submitcode.submission.entity.Submission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestCaseResultDTO {
    private List<ParameterDTO> input;

    private String outputData;
    private String outputDatatype;

    private String expected;
    private String expectedDatatype;

    private boolean isPassed;
    private Submission.EStatus status;

    private boolean isPublic;

    public TestCaseResultDTO(Submission.EStatus status) {
        this.status = status;
    }
}
