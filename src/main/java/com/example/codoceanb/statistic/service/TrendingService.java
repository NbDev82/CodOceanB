package com.example.codoceanb.statistic.service;

import com.example.codoceanb.statistic.dto.TrendingProblemDTO;
import com.example.codoceanb.submitcode.DTO.ProblemDTO;

import java.util.List;

public interface TrendingService {
    List<TrendingProblemDTO> getTrendingProblems(String topic, int limit);
    List<TrendingProblemDTO> getTrendingProblems(int limit);
}
