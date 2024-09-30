package com.example.codoceanb.search.dto;

import com.example.codoceanb.search.requestmodel.SearchRequest;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemDTO {
    private UUID id;
    private String title;
    private Problem.EDifficultyLevel difficulty;
    private boolean isDeleted;
    private SearchRequest.EStatus status;
    private int acceptedCount;
    private int submissionCount;
    private String acceptanceRate;

    public void setAcceptanceRate() {
        if(submissionCount <= 0) {
            this.acceptanceRate = "0";
            return;
        }
        double acceptanceRate = ((double) acceptedCount /submissionCount)*100;
        double roundedNumber = (double) Math.round(acceptanceRate * 100) / 100;
        DecimalFormat df = new DecimalFormat("#.#");
        this.acceptanceRate = df.format(roundedNumber);
    }
}
