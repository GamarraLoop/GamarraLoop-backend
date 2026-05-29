package com.gamarraloop.platform.notifications.application.internal.queryservices;

import com.gamarraloop.platform.notifications.application.ports.input.NotificationQueryService;
import com.gamarraloop.platform.notifications.domain.model.aggregate.Notification;
import com.gamarraloop.platform.notifications.infrastructure.persistence.jpa.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationRepository notificationRepository;

    public NotificationQueryServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notification> getById(UUID id) {
        return notificationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getByUserId(UUID userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUnreadByUserId(UUID userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }
}
