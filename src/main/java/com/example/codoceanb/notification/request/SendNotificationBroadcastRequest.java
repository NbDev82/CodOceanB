package com.example.codoceanb.notification.request;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.notification.dto.NotificationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SendNotificationBroadcastRequest {
    private NotificationDTO notification;
    private List<User.ERole> recipientRoles;
}
