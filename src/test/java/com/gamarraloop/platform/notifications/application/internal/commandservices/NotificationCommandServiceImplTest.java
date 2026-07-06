package com.gamarraloop.platform.notifications.application.internal.commandservices;

import com.gamarraloop.platform.notifications.domain.model.aggregate.Notification;
import com.gamarraloop.platform.notifications.domain.model.commands.CreateNotificationCommand;
import com.gamarraloop.platform.notifications.infrastructure.persistence.jpa.NotificationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationCommandServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationCommandServiceImpl notificationCommandService;

    @Test
    void shouldCreateNotificationSuccessfully() {
        UUID userId = UUID.randomUUID();

        CreateNotificationCommand command = new CreateNotificationCommand(
                userId,
                "Reserva confirmada",
                "Tu lote ha sido reservado por un artesano."
        );

        when(notificationRepository.save(ArgumentMatchers.any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Notification> result = notificationCommandService.create(command);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(userId, result.get().getUserId());
        Assertions.assertEquals("Reserva confirmada", result.get().getTitle());
        Assertions.assertFalse(result.get().getIsRead());

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void shouldMarkNotificationAsReadSuccessfully() {
        UUID id = UUID.randomUUID();

        Notification notification = new Notification(
                new CreateNotificationCommand(
                        UUID.randomUUID(),
                        "Reserva confirmada",
                        "Tu lote ha sido reservado."
                )
        );

        when(notificationRepository.findById(id))
                .thenReturn(Optional.of(notification));

        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Notification> result =
                notificationCommandService.markAsRead(id);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertTrue(result.get().getIsRead());

        verify(notificationRepository).findById(id);
        verify(notificationRepository).save(notification);
    }

    @Test
    void shouldReturnEmptyWhenNotificationDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(notificationRepository.findById(id))
                .thenReturn(Optional.empty());

        Optional<Notification> result =
                notificationCommandService.markAsRead(id);

        Assertions.assertTrue(result.isEmpty());

        verify(notificationRepository).findById(id);
        verify(notificationRepository, never()).save(any());
    }
}