package com.example.codoceanb.notification.service;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.exception.UserNotFoundException;
import com.example.codoceanb.auth.repository.UserRepos;
import com.example.codoceanb.auth.service.UserService;
import com.example.codoceanb.notification.dto.NotificationDTO;
import com.example.codoceanb.notification.entity.Notification;
import com.example.codoceanb.notification.exception.NotificationNotFoundException;
import com.example.codoceanb.notification.repository.NotificationRepository;
import com.example.codoceanb.notification.request.SendNotificationBroadcastRequest;
import com.example.codoceanb.notification.request.SendNotificationPersonalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepos userRepository;

    @Override
    public NotificationDTO getNotificationById(UUID id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException("Notification not found!"));

        if (notification.isDelete())
            throw new NotificationNotFoundException("Notification not found!");

        return notification.toDTO();
    }

    @Override
    public List<NotificationDTO> getNotifications(int pageNumber, int limit, String authHeader) {
        User user = userService.getUserDetailsFromToken(authHeader);

        Notification.EType type = switch (user.getRole()) {
            case ADMIN -> Notification.EType.COMMON_ADMIN;
            case MODERATOR -> Notification.EType.COMMON_MODERATOR;
            case USER_VIP-> Notification.EType.COMMON_USER_VIP;
            default -> Notification.EType.COMMON_USER;
        };

        Pageable pageable = PageRequest.of(pageNumber, limit);
        Page<Notification> page = notificationRepository.findAllByRecipientOrType(pageable, user, type, Notification.EType.PERSONAL);
        return page.getContent().stream()
                .filter(notification -> !notification.isDelete())
                .map(Notification::toDTO)
                .toList();
    }

    @Override
    public NotificationDTO saveNotificationBroadcast(SendNotificationBroadcastRequest request) {
        NotificationDTO notificationDTO = request.getNotification();

        Notification newNotification = Notification.builder()
                .content(notificationDTO.getContent())
                .receivedTime(LocalDateTime.now())
                .isRead(false)
                .isDelete(false)
                .build();

        for(User.ERole type : request.getRecipientRoles()) {
            switch (type) {
                case ADMIN:
                    newNotification.setType(Notification.EType.COMMON_ADMIN);
                    break;
                case MODERATOR:
                    newNotification.setType(Notification.EType.COMMON_MODERATOR);
                    break;
                case USER_VIP:
                    newNotification.setType(Notification.EType.COMMON_USER_VIP);
                    break;
                default:
                    newNotification.setType(Notification.EType.COMMON_USER);
            }
            notificationRepository.save(newNotification);
        }
        return newNotification.toDTO();
    }

    @Override
    public NotificationDTO saveNotificationPersonal(SendNotificationPersonalRequest request, String authHeader) {
        NotificationDTO notificationDTO = request.getNotification();

        Notification newNotification = Notification.builder()
                .content(notificationDTO.getContent())
                .receivedTime(LocalDateTime.now())
                .isRead(false)
                .isDelete(false)
                .type(Notification.EType.PERSONAL)
                .build();

        for(String email : request.getRecipientEmails()) {
            addRecipientAndSave(newNotification, email);
        }

        return newNotification.toDTO();
    }

    @Override
    public void setNotificationRead(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found!"));
        notification.setRead(true);
        notificationRepository.save(notification);


    }

    @Override
    public void setAllNotificationRead(String authHeader) {
        User user = userService.getUserDetailsFromToken(authHeader);

        Notification.EType type = switch (user.getRole()) {
            case ADMIN -> Notification.EType.COMMON_ADMIN;
            case MODERATOR -> Notification.EType.COMMON_MODERATOR;
            case USER_VIP-> Notification.EType.COMMON_USER_VIP;
            default -> Notification.EType.COMMON_USER;
        };

        List<Notification> notifications = notificationRepository.findAllByRecipientOrType(user, type, Notification.EType.PERSONAL);
        for(Notification notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

    public void addRecipientAndSave(Notification notification, String email) {
        User recipientUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Recipient not found!"));
        notification.setRecipient(recipientUser);
        notificationRepository.save(notification);
    }
}
