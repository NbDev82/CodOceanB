package com.example.codoceanb.admin.dashboard.controller;

import com.example.codoceanb.admin.dashboard.services.AdminStatisticsService;
import com.example.codoceanb.auth.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/admin/v1/dashboard")
public class AdminDashBoardController {

    @Autowired
    private AdminStatisticsService adminStatisticsService;

    @GetMapping("/posts/monthly")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyPosts(@RequestParam int year) {
        List<Map<String, Object>> monthlyPosts = adminStatisticsService.fetchMonthlyPosts(year);
        return ResponseEntity.ok().body(monthlyPosts);
    }

    @GetMapping("/users/total/monthly")
    public ResponseEntity<List<Map<String, Object>>> getTotalUsersMonthly(@RequestParam int year) {
        List<Map<String, Object>> totalUsersMonthly = adminStatisticsService.fetchTotalUsersMonthly(year);
        return ResponseEntity.ok().body(totalUsersMonthly);
    }

    @GetMapping("/users/new/monthly")
    public ResponseEntity<List<Map<String, Object>>> getNewUsersMonthly(@RequestParam int year) {
        List<Map<String, Object>> newUsersMonthly = adminStatisticsService.fetchNewUsersMonthly(year);
        return ResponseEntity.ok().body(newUsersMonthly);
    }

    @GetMapping("/users/total")
    public ResponseEntity<List<Map<String, Object>>> getTotalUsersByRole() {
        List<Map<String, Object>> totalUsersByRole = adminStatisticsService.fetchTotalUsersByRole();
        return ResponseEntity.ok().body(totalUsersByRole);
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyRevenue(@RequestParam int year) {
        List<Map<String, Object>> monthlyRevenue = adminStatisticsService.fetchMonthlyRevenue(year);
        return ResponseEntity.ok().body(monthlyRevenue);
    }

    @GetMapping("/total-revenue")
    public ResponseEntity<Long> getTotalMonthlyRevenue(@RequestParam int year) {
        Long totalMonthlyRevenue = adminStatisticsService.fetchTotalRevenue(year);
        return ResponseEntity.ok().body(totalMonthlyRevenue);
    }
}
