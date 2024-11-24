package com.example.codoceanb.submitcode.DTO;

import com.example.codoceanb.submitcode.problem.entity.ProblemApproach;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemHintDTO {
    private boolean isLocked;
    private String overView;
    private List<ProblemApproachDTO> approachDTOs;
    private String pseudoCode;
}
