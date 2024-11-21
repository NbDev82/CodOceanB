package com.example.codoceanb.notification.repository;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.notification.entity.Notification;
import com.example.codoceanb.report.entity.Report;
import io.micrometer.common.lang.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    @Query("SELECT n FROM Notification n WHERE (n.recipient IS NULL AND n.type = :eType) OR (n.recipient IS NOT NULL AND n.type = :personal AND n.recipient = :recipient) ORDER BY n.receivedTime DESC")
    List<Notification> findAllByRecipientOrType(@Nullable User recipient, Notification.EType eType, Notification.EType personal);

    @Query("SELECT n FROM Notification n WHERE (n.recipient IS NULL AND n.type = :eType) OR (n.recipient IS NOT NULL AND n.type = :personal AND n.recipient = :recipient) ORDER BY n.receivedTime DESC")
    Page<Notification> findAllByRecipientOrType(Pageable pageable, @Nullable User recipient, Notification.EType eType, Notification.EType personal);
}
