package com.example.codoceanb.submitcode.request;

import com.example.codoceanb.submitcode.DTO.AddParameterRequestDTO;
import com.example.codoceanb.submitcode.DTO.AddProblemRequestDTO;
import com.example.codoceanb.submitcode.DTO.AddTestCaseRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddProblemRequest {
    private AddProblemRequestDTO problem;
    private List<String> libraries;
    private List<AddParameterRequestDTO> parameters;
    private List<AddTestCaseRequestDTO> testcases;
}
