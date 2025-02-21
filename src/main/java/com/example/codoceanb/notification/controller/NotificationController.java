package com.example.codoceanb.notification.controller;

import com.example.codoceanb.notification.dto.NotificationDTO;
import com.example.codoceanb.notification.request.SendNotificationBroadcastRequest;
import com.example.codoceanb.notification.request.SendNotificationPersonalRequest;
import com.example.codoceanb.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable UUID id) {
        NotificationDTO notificationDTO = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notificationDTO);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<NotificationDTO>> getNotifications(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int limit,
            @RequestHeader("Authorization") String authHeader) {
        List<NotificationDTO> notificationDTOs = notificationService.getNotifications(pageNumber, limit, authHeader);
        return ResponseEntity.ok(notificationDTOs);
    }

    @PostMapping("/send-broadcast")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Boolean> sendNotificationBroadcast(@RequestBody SendNotificationBroadcastRequest request) {
        NotificationDTO notificationDTO = notificationService.saveNotificationBroadcast(request);

        for(String role : request.getRecipientRoles().stream().map(Enum::name).toList()) {
            messagingTemplate.convertAndSend(
                    "/topic/notification/broadcast/" + role,
                    notificationDTO
            );
        }

        return ResponseEntity.ok(true);
    }

    @PostMapping("/send-personal")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Boolean> sendNotificationPersonal(@RequestBody SendNotificationPersonalRequest request,
                                                            @RequestHeader("Authorization") String authHeader) {
        NotificationDTO notificationDTO = notificationService.saveNotificationPersonal(request, authHeader);

        for(String email: request.getRecipientEmails()) {
            messagingTemplate.convertAndSend(
                    "/topic/notification/personal/" + email,
                    notificationDTO
            );
        }

        return ResponseEntity.ok(true);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Boolean> setNotificationRead(@PathVariable UUID id) {
        notificationService.setNotificationRead(id);
        return ResponseEntity.ok(true);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Boolean> setAllNotificationRead(@RequestHeader("Authorization") String authHeader) {
        notificationService.setAllNotificationRead(authHeader);
        return ResponseEntity.ok(true);
    }
}
