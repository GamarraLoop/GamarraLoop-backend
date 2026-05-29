package com.gamarraloop.platform.notifications.application.internal.commandservices;

import com.gamarraloop.platform.notifications.application.ports.input.NotificationCommandService;
import com.gamarraloop.platform.notifications.domain.model.aggregate.Notification;
import com.gamarraloop.platform.notifications.domain.model.commands.CreateNotificationCommand;
import com.gamarraloop.platform.notifications.infrastructure.persistence.jpa.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationRepository notificationRepository;

    public NotificationCommandServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public Optional<Notification> create(CreateNotificationCommand command) {
        var notification = new Notification(command);
        var savedNotification = notificationRepository.save(notification);
        return Optional.of(savedNotification);
    }

    @Override
    @Transactional
    public Optional<Notification> markAsRead(UUID id) {
        return notificationRepository.findById(id).map(notification -> {
            notification.markAsRead();
            return notificationRepository.save(notification);
        });
    }
}
