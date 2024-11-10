package com.example.codoceanb.admin.dashboard.services;

import java.util.List;
import java.util.Map;

public interface AdminStatisticsService {

    List<Map<String, Object>> fetchMonthlyPosts(int year);

    List<Map<String, Object>> fetchTotalUsersMonthly(int year);

    List<Map<String, Object>> fetchNewUsersMonthly(int year);
}
