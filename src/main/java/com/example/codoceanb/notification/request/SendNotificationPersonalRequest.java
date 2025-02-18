package com.example.codoceanb.notification.request;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.notification.dto.NotificationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SendNotificationPersonalRequest {
    private NotificationDTO notification;
    private List<String> recipientEmails;
}
