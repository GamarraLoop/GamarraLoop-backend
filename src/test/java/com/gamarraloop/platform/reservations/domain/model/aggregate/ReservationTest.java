package com.gamarraloop.platform.reservations.domain.model.aggregate;

import com.gamarraloop.platform.reservations.domain.model.commands.CreateReservationCommand;
import com.gamarraloop.platform.reservations.domain.model.valueobjects.ReservationStatus;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {
    private Reservation createReservation() {
        CreateReservationCommand command =
                new CreateReservationCommand(
                        UUID.randomUUID(),
                        UUID.randomUUID()
                );

        return new Reservation(command);
    }

    @Test
    void shouldCreateReservationSuccessfully() {

        UUID lotId = UUID.randomUUID();
        UUID artisanId = UUID.randomUUID();

        CreateReservationCommand command =
                new CreateReservationCommand(
                        lotId,
                        artisanId
                );

        Reservation reservation =
                new Reservation(command);

        assertEquals(
                lotId,
                reservation.getLotId()
        );

        assertEquals(
                artisanId,
                reservation.getArtisanId()
        );

        assertEquals(
                ReservationStatus.ACTIVE,
                reservation.getStatus()
        );

        assertNotNull(
                reservation.getReservedAt()
        );

        assertNotNull(
                reservation.getExpiresAt()
        );
    }

    @Test
    void shouldCompleteReservationSuccessfully() {

        Reservation reservation =
                createReservation();

        reservation.complete();

        assertEquals(
                ReservationStatus.COMPLETED,
                reservation.getStatus()
        );
    }

    @Test
    void shouldThrowExceptionWhenCompletingCancelledReservation() {

        Reservation reservation =
                createReservation();

        reservation.cancel();

        assertThrows(
                IllegalStateException.class,
                reservation::complete
        );
    }

    @Test
    void shouldThrowExceptionWhenCompletingExpiredReservation() {

        Reservation reservation =
                createReservation();

        reservation.expire();

        assertThrows(
                IllegalStateException.class,
                reservation::complete
        );
    }

    @Test
    void shouldCancelReservationSuccessfully() {

        Reservation reservation =
                createReservation();

        reservation.cancel();

        assertEquals(
                ReservationStatus.CANCELLED,
                reservation.getStatus()
        );
    }

    @Test
    void shouldThrowExceptionWhenCancellingCompletedReservation() {

        Reservation reservation =
                createReservation();

        reservation.complete();

        assertThrows(
                IllegalStateException.class,
                reservation::cancel
        );
    }

    @Test
    void shouldExpireReservationSuccessfully() {

        Reservation reservation =
                createReservation();

        reservation.expire();

        assertEquals(
                ReservationStatus.EXPIRED,
                reservation.getStatus()
        );
    }

    @Test
    void shouldThrowExceptionWhenExpiringCompletedReservation() {

        Reservation reservation =
                createReservation();

        reservation.complete();

        assertThrows(
                IllegalStateException.class,
                reservation::expire
        );
    }

    @Test
    void shouldSetExpirationTimeTo24HoursAfterReservation() {

        Reservation reservation =
                createReservation();

        long hours =
                ChronoUnit.HOURS.between(
                        reservation.getReservedAt(),
                        reservation.getExpiresAt()
                );

        assertEquals(
                24,
                hours
        );
    }

}