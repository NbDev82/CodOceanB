package com.example.codoceanb.profile.request;

import com.example.codoceanb.submitcode.DTO.AddTestCaseRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateTestCaseRequest {
    List<AddTestCaseRequestDTO> testcases;
}
