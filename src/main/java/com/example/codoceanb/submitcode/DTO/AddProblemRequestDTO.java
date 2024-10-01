package com.example.codoceanb.submitcode.DTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AddProblemRequestDTO {
    private UUID ownerId;
    private String title;
    private String description;
    private String point;
    private String difficulty;
    private List<String> topics;
    private boolean deleted;
    private String functionName;
    private String outputDataType;
}
