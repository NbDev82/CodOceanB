package com.example.codoceanb.statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatisticDTO {
    private int totalEasy;
    private int totalNormal;
    private int totalHard;
    private List<Integer> Easy;
    private List<Integer> Normal;
    private List<Integer> Hard;
}
