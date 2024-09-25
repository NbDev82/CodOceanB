package com.example.codoceanb.statistic.service;

import com.example.codoceanb.submitcode.DTO.ProblemDTO;

import java.util.List;

public interface TrendingService {
    List<ProblemDTO> getTrendingProblems(String topic, int limit);
    List<ProblemDTO> getTrendingProblems(int limit);
}
