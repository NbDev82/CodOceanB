package com.example.codoceanb.submitcode.request;

import com.example.codoceanb.submitcode.DTO.CustomTestCaseDTO;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TestCodeWithCustomTestcaseRequest extends SubmitCodeRequest{
    private List<CustomTestCaseDTO> customTestCaseDTOs;
}