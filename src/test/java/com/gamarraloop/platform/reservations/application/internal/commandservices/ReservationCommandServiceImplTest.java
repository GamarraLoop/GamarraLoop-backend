package com.gamarraloop.platform.reservations.application.internal.commandservices;

import com.gamarraloop.platform.reservations.domain.model.aggregate.Reservation;
import com.gamarraloop.platform.reservations.domain.model.commands.CreateReservationCommand;
import com.gamarraloop.platform.reservations.domain.model.valueobjects.ReservationStatus;
import com.gamarraloop.platform.reservations.infrastructure.persistence.jpa.ReservationRepository;
import com.gamarraloop.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationCommandServiceImplTest {
    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationCommandServiceImpl reservationCommandService;

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

        when(reservationRepository.findByLotId(lotId))
                .thenReturn(List.of());

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result =
                reservationCommandService.create(command);

        assertNotNull(result);

        assertEquals(
                ReservationStatus.ACTIVE,
                result.getStatus()
        );

        verify(reservationRepository)
                .findByLotId(lotId);

        verify(reservationRepository)
                .save(any(Reservation.class));
    }

    @Test
    void shouldThrowExceptionWhenLotAlreadyHasActiveReservation() {

        UUID lotId = UUID.randomUUID();

        CreateReservationCommand command =
                new CreateReservationCommand(
                        lotId,
                        UUID.randomUUID()
                );

        Reservation existingReservation =
                createReservation();

        when(reservationRepository.findByLotId(lotId))
                .thenReturn(List.of(existingReservation));

        assertThrows(
                IllegalStateException.class,
                () -> reservationCommandService.create(command)
        );

        verify(reservationRepository, never())
                .save(any());
    }

    @Test
    void shouldCompleteReservationSuccessfully() {

        UUID reservationId = UUID.randomUUID();

        Reservation reservation =
                createReservation();

        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.of(reservation));

        when(reservationRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result =
                reservationCommandService.complete(reservationId);

        assertEquals(
                ReservationStatus.COMPLETED,
                result.getStatus()
        );

        verify(reservationRepository)
                .save(reservation);
    }

    @Test
    void shouldThrowExceptionWhenCompletingNonExistingReservation() {

        UUID reservationId = UUID.randomUUID();

        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservationCommandService.complete(reservationId)
        );

        verify(reservationRepository, never())
                .save(any());
    }

    @Test
    void shouldCancelReservationSuccessfully() {

        UUID reservationId = UUID.randomUUID();

        Reservation reservation =
                createReservation();

        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.of(reservation));

        when(reservationRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result =
                reservationCommandService.cancel(reservationId);

        assertEquals(
                ReservationStatus.CANCELLED,
                result.getStatus()
        );
    }

    @Test
    void shouldThrowExceptionWhenCancellingNonExistingReservation() {

        UUID reservationId = UUID.randomUUID();

        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservationCommandService.cancel(reservationId)
        );
    }

    @Test
    void shouldExpireReservationSuccessfully() {

        UUID reservationId = UUID.randomUUID();

        Reservation reservation =
                createReservation();

        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.of(reservation));

        when(reservationRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result =
                reservationCommandService.expire(reservationId);

        assertEquals(
                ReservationStatus.EXPIRED,
                result.getStatus()
        );
    }

    @Test
    void shouldThrowExceptionWhenExpiringNonExistingReservation() {

        UUID reservationId = UUID.randomUUID();

        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservationCommandService.expire(reservationId)
        );
    }
}