package com.example.codoceanb.submitcode.DTO;

import lombok.Data;

import java.util.List;

@Data
public class AddTestCaseRequestDTO {
    private boolean isPublic;
    private List<InputDTO> input;
    private String output;
}
