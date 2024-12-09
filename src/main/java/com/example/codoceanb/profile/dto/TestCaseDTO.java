package com.example.codoceanb.profile.dto;

import com.example.codoceanb.submitcode.DTO.InputDTO;
import com.example.codoceanb.submitcode.DTO.ParameterDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCaseDTO {
    private boolean isPublic;
    private List<ParameterDTO> parameterDTOs;
    private String output;
}
