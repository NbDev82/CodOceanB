package com.example.codoceanb.admin.dashboard.services;

import com.example.codoceanb.admin.account.services.AccountServiceImpl;
import com.example.codoceanb.auth.repository.UserRepos;
import com.example.codoceanb.discuss.entity.Discuss;
import com.example.codoceanb.discuss.repository.DiscussRepository;
import com.example.codoceanb.discuss.service.DiscussService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AdminStatisticsServiceImpl implements AdminStatisticsService{
    private final Logger log = LogManager.getLogger(AdminStatisticsServiceImpl.class);

    @Autowired
    private UserRepos userRepos;

    @Autowired
    private DiscussRepository discussRepository;

    @Override
    public List<Map<String, Object>> fetchMonthlyPosts(int year) {
        List<Map<String, Object>> monthlyPosts = new ArrayList<>();
        try {
            monthlyPosts = discussRepository.getMonthlyPostsCount(year);
            log.info("Fetched monthly posts for year {}: {}", year, monthlyPosts);
        } catch (Exception e) {
            log.error("Failed to fetch monthly posts for year {}: {}", year, e.getMessage());
        }
        return monthlyPosts;
    }

    @Override
    public List<Map<String, Object>> fetchTotalUsersMonthly(int year) {
        List<Map<String, Object>> totalUsersMonthly = new ArrayList<>();
        try {
            totalUsersMonthly = userRepos.getTotalMonthlyUsersCountByYear(year);
            log.info("Fetched total users monthly for year {}: {}", year, totalUsersMonthly);
        } catch (Exception e) {
            log.error("Failed to fetch total users monthly for year {}: {}", year, e.getMessage());
        }
        return totalUsersMonthly;
    }

    @Override
    public List<Map<String, Object>> fetchNewUsersMonthly(int year) {
        List<Map<String, Object>> newUsersMonthly = new ArrayList<>();
        try {
            newUsersMonthly = userRepos.getMonthlyNewUsersCountByYear(year);
            log.info("Fetched new users monthly for year {}: {}", year, newUsersMonthly);
        } catch (Exception e) {
            log.error("Failed to fetch new users monthly for year {}: {}", year, e.getMessage());
        }
        return newUsersMonthly;
    }
}