package com.example.codoceanb.search.requestmodel;

import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchRequest {
    private int pageNumber;
    private int limit;
    private EStatus status;
    private Problem.EDifficultyLevel difficulty;
    private Problem.ETopic topic;
    private String searchTerm;

    public enum EStatus {
        SOLVED,
        ATTEMPTED,
        TODO
    }
}
