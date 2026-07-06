package com.gamarraloop.platform.delivery.domain.model.aggregate;

import com.gamarraloop.platform.delivery.domain.model.commands.StartDeliveryCommand;
import com.gamarraloop.platform.delivery.domain.model.valueobjects.DeliveryStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

class DeliveryProcessTest {

    private DeliveryProcess createDeliveryProcess() {
        StartDeliveryCommand command =
                new StartDeliveryCommand(UUID.randomUUID());

        return new DeliveryProcess(command);
    }

    @Test
    void shouldCreateDeliveryProcessSuccessfully() {
        UUID reservationId = UUID.randomUUID();
        StartDeliveryCommand command =
                new StartDeliveryCommand(reservationId);

        DeliveryProcess delivery = new DeliveryProcess(command);

        Assertions.assertEquals(reservationId, delivery.getReservationId());
        Assertions.assertEquals(DeliveryStatus.IN_TRANSIT, delivery.getStatus());
        Assertions.assertNotNull(delivery.getStartedAt());
        Assertions.assertNull(delivery.getCompletedAt());
    }

    @Test
    void shouldCompleteDeliverySuccessfully() {
        DeliveryProcess delivery = createDeliveryProcess();

        delivery.complete();

        Assertions.assertEquals(DeliveryStatus.DELIVERED, delivery.getStatus());
        Assertions.assertNotNull(delivery.getCompletedAt());
    }

    @Test
    void shouldThrowExceptionWhenCompletingDeliveredDelivery() {
        DeliveryProcess delivery = createDeliveryProcess();

        delivery.complete();

        Assertions.assertThrows(
                IllegalStateException.class,
                delivery::complete
        );
    }

    @Test
    void shouldFailDeliverySuccessfully() {
        DeliveryProcess delivery = createDeliveryProcess();

        delivery.fail();

        Assertions.assertEquals(DeliveryStatus.FAILED, delivery.getStatus());
        Assertions.assertNotNull(delivery.getCompletedAt());
    }

    @Test
    void shouldThrowExceptionWhenFailingDeliveredDelivery() {
        DeliveryProcess delivery = createDeliveryProcess();

        delivery.complete();

        Assertions.assertThrows(
                IllegalStateException.class,
                delivery::fail
        );
    }

    @Test
    void shouldSetStartedAtWhenCreatingDeliveryProcess() {
        Instant beforeCreation = Instant.now();

        DeliveryProcess delivery = createDeliveryProcess();

        Instant afterCreation = Instant.now();

        Assertions.assertTrue(
                !delivery.getStartedAt().isBefore(beforeCreation)
                        && !delivery.getStartedAt().isAfter(afterCreation)
        );
    }

    @Test
    void shouldSetCompletedAtWhenDeliveryIsCompleted() {
        DeliveryProcess delivery = createDeliveryProcess();

        delivery.complete();

        Assertions.assertNotNull(delivery.getCompletedAt());
    }

    @Test
    void shouldSetCompletedAtWhenDeliveryFails() {
        DeliveryProcess delivery = createDeliveryProcess();

        delivery.fail();

        Assertions.assertNotNull(delivery.getCompletedAt());
    }
}