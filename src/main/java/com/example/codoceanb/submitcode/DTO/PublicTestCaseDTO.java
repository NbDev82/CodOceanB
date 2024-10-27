package com.example.codoceanb.submitcode.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PublicTestCaseDTO {
    List<ParameterDTO> parameterDTOs;
}
