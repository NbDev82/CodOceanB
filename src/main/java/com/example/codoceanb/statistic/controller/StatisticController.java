package com.example.codoceanb.statistic.controller;

import com.example.codoceanb.statistic.dto.StatisticDTO;
import com.example.codoceanb.statistic.service.StatisticService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistic")
public class StatisticController {
    private static final Logger log = LogManager.getLogger(StatisticController.class);

    private final StatisticService service;

    public StatisticController(StatisticService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<StatisticDTO> getStatistic(@RequestParam Long userId) {
        StatisticDTO statisticDTO = service.getStatistic(userId);
        log.info("Get StatisticDTO from StatisticController");
        return ResponseEntity.ok(statisticDTO);
    }
}