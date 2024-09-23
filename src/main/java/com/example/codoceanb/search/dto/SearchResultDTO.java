package com.example.codoceanb.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchResultDTO {
    private int totalPage;
    private long totalElement;
    private List<ProblemDTO> problemDTOs;
}
