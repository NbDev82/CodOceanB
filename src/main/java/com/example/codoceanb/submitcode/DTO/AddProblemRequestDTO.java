package com.example.codoceanb.submitcode.DTO;

import lombok.Data;

import java.util.List;

@Data
public class AddProblemRequestDTO {
    private Long ownerId;
    private String title;
    private String description;
    private String point;
    private String difficulty;
    private List<String> topics;
    private boolean deleted;
    private String functionName;
    private String outputDataType;
}
