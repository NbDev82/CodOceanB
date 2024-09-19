package com.example.codoceanb.notification.entity;

import com.example.codoceanb.login.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(name = "receiving_time")
    private LocalDateTime receivingTime;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "is_delete")
    private boolean isDelete;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User recipient;
}
