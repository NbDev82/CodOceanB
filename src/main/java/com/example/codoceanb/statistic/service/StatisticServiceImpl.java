package com.example.codoceanb.statistic.service;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.service.UserService;
import com.example.codoceanb.statistic.dto.StatisticDTO;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import com.example.codoceanb.submitcode.submission.service.SubmissionService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StatisticServiceImpl implements  StatisticService {
    private final SubmissionService submissionService;

    private final UserService userService;

    public StatisticServiceImpl(SubmissionService submissionService, UserService userService) {
        this.submissionService = submissionService;
        this.userService = userService;
    }

    @Override
    public StatisticDTO getStatistic(String authHeader) {
        User user = userService.getUserDetailsFromToken(authHeader);
        List<Submission> acceptedSubmissions = submissionService.getByUserId(user.getId(), Submission.class)
                .stream()
                .filter(submission -> submission.getStatus().equals(Submission.EStatus.ACCEPTED))
                .toList();

        AtomicInteger totalEasy = new AtomicInteger();
        AtomicInteger totalNormal = new AtomicInteger();
        AtomicInteger totalHard = new AtomicInteger();

        Map<DayOfWeek, Integer> dayIndexMap = initializeDayIndexMap();

        List<Integer> EASY = initializeList();
        List<Integer> NORMAL = initializeList();
        List<Integer> HARD = initializeList();

        LocalDateTime startOfWeek = getStartOfWeek();
        LocalDateTime endOfWeek = getEndOfWeek();

        acceptedSubmissions.forEach(submission -> {
            switch (submission.getProblem().getDifficultyLevel()) {
                case EASY -> totalEasy.incrementAndGet();
                case NORMAL -> totalNormal.incrementAndGet();
                case HARD -> totalHard.incrementAndGet();
            }

            if (submission.getCreatedAt().isAfter(startOfWeek) && submission.getCreatedAt().isBefore(endOfWeek)) {
                DayOfWeek submissionDayOfWeek = submission.getCreatedAt().getDayOfWeek();
                int index = dayIndexMap.getOrDefault(submissionDayOfWeek, -1);
                if (index != -1) {
                    switch (submission.getProblem().getDifficultyLevel()) {
                        case EASY -> EASY.set(index, EASY.get(index) + 1);
                        case NORMAL -> NORMAL.set(index, NORMAL.get(index) + 1);
                        case HARD -> HARD.set(index, HARD.get(index) + 1);
                    }
                }
            }
        });

        return StatisticDTO.builder()
                .totalEasy(totalEasy.get())
                .totalNormal(totalNormal.get())
                .totalHard(totalHard.get())
                .Easy(EASY)
                .Normal(NORMAL)
                .Hard(HARD)
                .build();
    }

    private List<Integer> initializeList() {
        return Arrays.asList(0, 0, 0, 0, 0, 0, 0);
    }

    private Map<DayOfWeek, Integer> initializeDayIndexMap() {
        Map<DayOfWeek, Integer> dayIndexMap = new HashMap<>();
        dayIndexMap.put(DayOfWeek.MONDAY, 0);
        dayIndexMap.put(DayOfWeek.TUESDAY, 1);
        dayIndexMap.put(DayOfWeek.WEDNESDAY, 2);
        dayIndexMap.put(DayOfWeek.THURSDAY, 3);
        dayIndexMap.put(DayOfWeek.FRIDAY, 4);
        dayIndexMap.put(DayOfWeek.SATURDAY, 5);
        dayIndexMap.put(DayOfWeek.SUNDAY, 6);
        return dayIndexMap;
    }

    private LocalDateTime getStartOfWeek() {
        return LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).withHour(0).withMinute(0).withSecond(0);
    }

    private LocalDateTime getEndOfWeek() {
        return LocalDateTime.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).withHour(23).withMinute(59).withSecond(59);
    }
}