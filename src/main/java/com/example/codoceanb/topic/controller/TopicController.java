package com.example.codoceanb.topic.controller;

import com.example.codoceanb.topic.dto.TopicDTO;
import com.example.codoceanb.topic.service.TopicService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {
    private static final Logger log = LogManager.getLogger(TopicController.class);

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public ResponseEntity<List<TopicDTO>> getTopics() {
        List<TopicDTO> topicDTOs = topicService.getTopics();
        log.info("Get topicDTO from TopicController");
        return ResponseEntity.ok(topicDTOs);
    }
}
