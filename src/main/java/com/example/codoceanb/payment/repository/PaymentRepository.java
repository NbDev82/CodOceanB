package com.example.codoceanb.payment.repository;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    @Query("SELECT new map(MONTH(p.paymentDate) as month, SUM(p.amount) as totalRevenue) " +
            "FROM Payment p " +
            "WHERE p.paymentStatus = 'COMPLETED' AND YEAR(p.paymentDate) = :year " +
            "GROUP BY MONTH(p.paymentDate)")
    List<Map<String, Object>> getMonthlyRevenueCount(@Param("year") int year);

    @Query("SELECT SUM(p.amount) " +
            "FROM Payment p " +
            "WHERE p.paymentStatus = 'COMPLETED' AND YEAR(p.paymentDate) = :year")
    Long getTotalRevenueCountByYear(@Param("year") int year);


}
