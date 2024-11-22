package com.example.codoceanb.auth.repository;

import com.example.codoceanb.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepos extends JpaRepository<User, UUID> {
    boolean existsByPhoneNumberOrEmail(String phoneNumber, String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role != 'ADMIN'")
    List<User> findUsers();

    @Query(value = "SELECT EXTRACT(MONTH FROM u.created_at) AS month, COUNT(u.id) AS total " +
                   "FROM users u " +
                   "WHERE EXTRACT(YEAR FROM u.created_at) = :year " +
                   "GROUP BY EXTRACT(MONTH FROM u.created_at) " +
                   "ORDER BY month",
           nativeQuery = true)
    List<Map<String, Object>> getTotalMonthlyUsersCountByYear(int year);

    @Query(value = "SELECT EXTRACT(MONTH FROM u.created_at) AS month, COUNT(u.id) AS new_users " +
                   "FROM users u " +
                   "WHERE EXTRACT(YEAR FROM u.created_at) = :year " +
                   "GROUP BY EXTRACT(MONTH FROM u.created_at) " +
                   "ORDER BY month",
           nativeQuery = true)
    List<Map<String, Object>> getMonthlyNewUsersCountByYear(int year);

    @Query("SELECT COUNT(u.email) " +
            "FROM User u " +
            "WHERE u.isLocked = FALSE AND u.role = :role")
    double getTotalUsersByRole(User.ERole role);
}
