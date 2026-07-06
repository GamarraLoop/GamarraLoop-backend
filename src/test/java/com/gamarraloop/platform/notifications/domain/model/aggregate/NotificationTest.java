package com.gamarraloop.platform.notifications.domain.model.aggregate;

import com.gamarraloop.platform.notifications.domain.model.commands.CreateNotificationCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class NotificationTest {

    private Notification createNotification() {
        CreateNotificationCommand command = new CreateNotificationCommand(
                UUID.randomUUID(),
                "Reserva confirmada",
                "Tu lote ha sido reservado por un artesano."
        );

        return new Notification(command);
    }

    @Test
    void shouldCreateNotificationSuccessfully() {
        UUID userId = UUID.randomUUID();

        CreateNotificationCommand command = new CreateNotificationCommand(
                userId,
                "Reserva confirmada",
                "Tu lote ha sido reservado por un artesano."
        );

        Notification notification = new Notification(command);

        Assertions.assertEquals(userId, notification.getUserId());
        Assertions.assertEquals("Reserva confirmada", notification.getTitle());
        Assertions.assertEquals(
                "Tu lote ha sido reservado por un artesano.",
                notification.getMessage()
        );
        Assertions.assertFalse(notification.getIsRead());
    }

    @Test
    void shouldInitializeNotificationAsUnread() {
        Notification notification = createNotification();

        Assertions.assertFalse(notification.getIsRead());
    }

    @Test
    void shouldMarkNotificationAsRead() {
        Notification notification = createNotification();

        notification.markAsRead();

        Assertions.assertTrue(notification.getIsRead());
    }
}