package com.example.codoceanb.notification.service;

import com.example.codoceanb.notification.dto.NotificationDTO;
import com.example.codoceanb.notification.request.SendNotificationBroadcastRequest;
import com.example.codoceanb.notification.request.SendNotificationPersonalRequest;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    NotificationDTO getNotificationById(UUID id);

    List<NotificationDTO> getNotifications(int pageNumber, int limit, String authHeader);

    NotificationDTO saveNotificationBroadcast(SendNotificationBroadcastRequest request);

    NotificationDTO saveNotificationPersonal(SendNotificationPersonalRequest request, String authHeader);

    void setNotificationRead(UUID id);

    void setAllNotificationRead(String authHeader);
}
