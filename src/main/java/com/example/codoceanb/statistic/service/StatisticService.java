package com.example.codoceanb.statistic.service;

import com.example.codoceanb.statistic.dto.StatisticDTO;

public interface StatisticService {
    StatisticDTO getStatistic(String authHeader);
}