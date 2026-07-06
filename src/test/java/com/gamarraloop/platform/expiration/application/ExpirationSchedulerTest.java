package com.gamarraloop.platform.expiration.application;

import com.gamarraloop.platform.reservations.application.ports.input.ReservationCommandService;
import com.gamarraloop.platform.reservations.application.ports.input.ReservationQueryService;
import com.gamarraloop.platform.reservations.domain.model.aggregate.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpirationSchedulerTest {

    @Mock
    private ReservationQueryService reservationQueryService;

    @Mock
    private ReservationCommandService reservationCommandService;

    @InjectMocks
    private ExpirationScheduler expirationScheduler;

    private Reservation reservation1;
    private Reservation reservation2;

    private UUID reservationId1;
    private UUID reservationId2;

    @BeforeEach
    void setUp() {
        reservation1 = mock(Reservation.class);
        reservation2 = mock(Reservation.class);

        reservationId1 = UUID.randomUUID();
        reservationId2 = UUID.randomUUID();
    }

    @Test
    void shouldExpireAllExpiredReservations() {
        when(reservation1.getId()).thenReturn(reservationId1);
        when(reservation2.getId()).thenReturn(reservationId2);

        when(reservationQueryService.getExpiredActiveReservations())
                .thenReturn(List.of(reservation1, reservation2));

        expirationScheduler.expireOldReservations();

        verify(reservationQueryService).getExpiredActiveReservations();
        verify(reservationCommandService).expire(reservationId1);
        verify(reservationCommandService).expire(reservationId2);
    }

    @Test
    void shouldDoNothingWhenNoExpiredReservationsExist() {
        when(reservationQueryService.getExpiredActiveReservations())
                .thenReturn(List.of());

        expirationScheduler.expireOldReservations();

        verify(reservationQueryService).getExpiredActiveReservations();
        verifyNoInteractions(reservationCommandService);
    }

    @Test
    void shouldContinueExpiringReservationsWhenOneFails() {
        when(reservation1.getId()).thenReturn(reservationId1);
        when(reservation2.getId()).thenReturn(reservationId2);

        when(reservationQueryService.getExpiredActiveReservations())
                .thenReturn(List.of(reservation1, reservation2));

        doThrow(new RuntimeException("Unexpected error"))
                .when(reservationCommandService)
                .expire(reservationId1);

        expirationScheduler.expireOldReservations();

        verify(reservationCommandService).expire(reservationId1);
        verify(reservationCommandService).expire(reservationId2);
    }
}