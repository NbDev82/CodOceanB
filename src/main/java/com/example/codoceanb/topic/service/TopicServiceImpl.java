package com.example.codoceanb.topic.service;

import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.problem.service.ProblemService;
import com.example.codoceanb.topic.dto.TopicDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TopicServiceImpl implements TopicService{
    private static final int NOT_EXIST_TOPIC = -1;
    private final ProblemService problemService;

    public TopicServiceImpl(ProblemService problemService) {
        this.problemService = problemService;
    }

    @Override
    public List<TopicDTO> getTopics() {
        List<TopicDTO> topicDTOs = new ArrayList<>();
        List<Problem> problems = problemService.getAll();

        int index;
        TopicDTO topicDTO;

        for(Problem p : problems) {
            for (Problem.ETopic topic : p.getTopics()) {
                index = getIndexOfTopic(topicDTOs, topic);
                if(index == NOT_EXIST_TOPIC) {
                    topicDTO = new TopicDTO(topic.name(),1);
                    topicDTOs.add(topicDTO);
                } else {
                    topicDTO = topicDTOs.get(index);
                    int newQuantity = topicDTO.getQuantity() + 1;
                    topicDTO.setQuantity(newQuantity);

                    topicDTOs.set(index, topicDTO);
                }
            }
        }

        return topicDTOs;
    }

    private int getIndexOfTopic(List<TopicDTO> topicDTOs, Problem.ETopic topic) {
        AtomicInteger index = new AtomicInteger(NOT_EXIST_TOPIC);

        topicDTOs.stream()
                .filter(topicDTO -> topicDTO.getName().equals(topic.name()))
                .findFirst()
                .ifPresentOrElse(
                        topicDTO -> index.set(topicDTOs.indexOf(topicDTO)),
                        () -> {}
                );

        return index.get();
    }
}
