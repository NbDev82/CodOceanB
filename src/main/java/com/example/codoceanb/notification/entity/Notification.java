package com.example.codoceanb.notification.entity;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.notification.dto.NotificationDTO;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String content;

    @Column(name = "received_time")
    private LocalDateTime receivedTime;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "is_delete")
    private boolean isDelete;

    @Column(name="notification_type")
    private EType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User recipient;

    @Getter
    public enum EType {
        COMMON_USER,
        COMMON_USER_VIP,
        COMMON_MODERATOR,
        COMMON_ADMIN,
        PERSONAL
    }

    public NotificationDTO toDTO() {
        return NotificationDTO.builder()
                .id(this.id)
                .content(this.content)
                .receivedTime(this.receivedTime)
                .isRead(this.isRead)
                .build();
    }
}
