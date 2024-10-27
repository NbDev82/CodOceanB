package com.example.codoceanb.submitcode.DTO;

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
    private String description;
    private String difficulty;
    private int acceptedCount;
    private int discussCount;
    private int submissionCount;
    private String acceptanceRate = getAcceptanceRate();

    public String getAcceptanceRate() {
        double acceptanceRate = ((double) acceptedCount /submissionCount)*100;
        double roundedNumber = Math.round(acceptanceRate * 10.0) / 10.0;
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(roundedNumber);
    }
}
