package com.example.codoceanb.payment.entity;

import com.example.codoceanb.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Payment {
    @Id
    private String id;

    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime paymentDate;
    private Double amount;
    private String currency;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
