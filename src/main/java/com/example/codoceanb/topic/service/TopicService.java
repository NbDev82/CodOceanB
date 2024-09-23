package com.example.codoceanb.topic.service;

import com.example.codoceanb.topic.dto.TopicDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TopicService {
    List<TopicDTO> getTopics();
}
