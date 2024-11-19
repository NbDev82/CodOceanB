package com.example.codoceanb.notification.dto;

import com.example.codoceanb.notification.entity.Notification;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotificationDTO {
    private UUID id;
    private String content;
    private LocalDateTime receivedTime;
    private boolean isRead;

    private String ownerImageUrl;
    private String ownerName;


    public Notification toEntity() {
        return Notification.builder()
                .content(this.content)
                .receivedTime(this.receivedTime)
                .isRead(this.isRead)
                .build();
    }
}
