package com.example.codoceanb.statistic.controller;

import com.example.codoceanb.statistic.dto.StatisticDTO;
import com.example.codoceanb.statistic.service.StatisticService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/statistic")
public class StatisticController {
    private static final Logger log = LogManager.getLogger(StatisticController.class);

    private final StatisticService service;

    public StatisticController(StatisticService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<StatisticDTO> getStatistic(@RequestHeader(value = "Authorization") String authHeader) {
        StatisticDTO statisticDTO = service.getStatistic(authHeader);
        log.info("Get StatisticDTO from StatisticController");
        return ResponseEntity.ok(statisticDTO);
    }
}