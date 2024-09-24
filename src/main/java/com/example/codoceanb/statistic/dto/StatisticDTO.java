package com.example.codoceanb.statistic.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StatisticDTO {
    private int totalEasy;
    private int totalNormal;
    private int totalHard;
    private List<Integer> Easy;
    private List<Integer> Normal;
    private List<Integer> Hard;
}
