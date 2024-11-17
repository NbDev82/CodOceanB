package com.example.codoceanb.statistic.service;

import com.example.codoceanb.statistic.dto.TrendingProblemDTO;
import com.example.codoceanb.submitcode.DTO.ProblemDTO;
import com.example.codoceanb.submitcode.problem.service.ProblemService;
import com.example.codoceanb.submitcode.submission.service.SubmissionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrendingServiceImpl implements TrendingService {
    private static final Logger log = LogManager.getLogger(TrendingServiceImpl.class);

    private final ProblemService problemService;

    public TrendingServiceImpl(ProblemService problemService) {
        this.problemService = problemService;
    }

    @Override
    public List<TrendingProblemDTO> getTrendingProblems(String topic, int limit) {
        log.info("Đang lấy các bài toán thịnh hành cho chủ đề: {} với giới hạn: {}", topic, limit);
        List<TrendingProblemDTO> trendingProblems = problemService.getTopProblemsByTopic(topic.toUpperCase(), limit);
        log.info("Đã lấy được {} bài toán thịnh hành", trendingProblems.size());
        return trendingProblems;
    }

    @Override
    public List<TrendingProblemDTO> getTrendingProblems(int limit) {
        log.info("Đang lấy các bài toán thịnh hành với giới hạn: {}", limit);
        List<TrendingProblemDTO> trendingProblems = problemService.getTopProblems(limit);
        log.info("Đã lấy được {} bài toán thịnh hành", trendingProblems.size());
        return trendingProblems;
    }
}
