package com.gamarraloop.platform.lots.application.internal.commandservices;

import com.gamarraloop.platform.lots.application.ports.output.LotRepository;
import com.gamarraloop.platform.lots.domain.model.aggregate.Lot;
import com.gamarraloop.platform.lots.domain.model.commands.CreateLotCommand;
import com.gamarraloop.platform.lots.domain.model.commands.UpdateLotCommand;
import com.gamarraloop.platform.lots.domain.model.valueobjects.LotStatus;
import com.gamarraloop.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LotCommandServiceImplTest {

    @Mock
    private LotRepository lotRepository;

    @InjectMocks
    private LotCommandServiceImpl lotCommandService;

    private Lot createLot() {
        CreateLotCommand command =
                new CreateLotCommand(
                        UUID.randomUUID(),
                        "Cotton Scraps",
                        "Reusable textile remnants",
                        "Cotton",
                        BigDecimal.valueOf(10),
                        "image.jpg",
                        BigDecimal.valueOf(-12.0464),
                        BigDecimal.valueOf(-77.0428),
                        "Stand 15"
                );
        return new Lot(command);
    }

    @Test
    void shouldCreateLotSuccessfully() {

        CreateLotCommand command =
                new CreateLotCommand(
                        UUID.randomUUID(),
                        "Cotton",
                        "Description",
                        "Cotton",
                        BigDecimal.TEN,
                        "image.jpg",
                        BigDecimal.ONE,
                        BigDecimal.ONE,
                        "Ref"
                );

        //Cuando se llame save, devuelve el mismo objeto recibido
        when(lotRepository.save(any(Lot.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Lot result = lotCommandService.create(command);

        ArgumentCaptor<Lot> captor =
                ArgumentCaptor.forClass(Lot.class);

        verify(lotRepository).save(captor.capture());

        Lot savedLot = captor.getValue();

        assertEquals(result.getTitle(), savedLot.getTitle());
        assertEquals(result.getStatus(), savedLot.getStatus());
    }

    @Test
    void shouldUpdateLotSuccessfully() {

        UUID lotId = UUID.randomUUID();

        Lot lot = createLot();

        UpdateLotCommand command =
                new UpdateLotCommand(
                        "Updated Title",
                        "Updated Description",
                        "Denim",
                        BigDecimal.valueOf(20),
                        "updated-image.jpg",
                        BigDecimal.valueOf(-12.1),
                        BigDecimal.valueOf(-77.1),
                        "Updated Ref"
                );

        when(lotRepository.findById(lotId))
                .thenReturn(Optional.of(lot));

        when(lotRepository.save(any(Lot.class)))
                .thenAnswer(i -> i.getArgument(0));

        Lot result = lotCommandService.update(lotId, command);

        assertEquals("Updated Title", result.getTitle());
        assertEquals("Denim", result.getTextileType());

        verify(lotRepository).findById(lotId);
        verify(lotRepository).save(any(Lot.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingLot() {

        UUID lotId = UUID.randomUUID();

        UpdateLotCommand command =
                new UpdateLotCommand(
                        "Updated Title",
                        "Updated Description",
                        "Denim",
                        BigDecimal.valueOf(20),
                        "updated-image.jpg",
                        BigDecimal.ONE,
                        BigDecimal.ONE,
                        "Updated Ref"
                );

        when(lotRepository.findById(lotId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> lotCommandService.update(lotId, command)
        );

        verify(lotRepository).findById(lotId);
    }

    @Test
    void shouldPublishLotSuccessfully() {

        UUID lotId = UUID.randomUUID();

        Lot lot = createLot();

        when(lotRepository.findById(lotId))
                .thenReturn(Optional.of(lot));

        when(lotRepository.save(any(Lot.class)))
                .thenAnswer(i -> i.getArgument(0));

        Lot result = lotCommandService.publish(lotId);

        assertEquals(
                LotStatus.PUBLISHED,
                result.getStatus()
        );

        assertNotNull(
                result.getPublishedAt()
        );

        verify(lotRepository).findById(lotId);
        verify(lotRepository).save(any(Lot.class));
    }

    @Test
    void shouldThrowExceptionWhenPublishingNonExistingLot() {

        UUID lotId = UUID.randomUUID();

        when(lotRepository.findById(lotId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> lotCommandService.publish(lotId)
        );

        verify(lotRepository).findById(lotId);
    }

    @Test
    void shouldThrowExceptionWhenPublishingAlreadyPublishedLot() {

        UUID lotId = UUID.randomUUID();

        Lot lot = createLot();

        lot.publish();

        when(lotRepository.findById(lotId))
                .thenReturn(Optional.of(lot));

        assertThrows(
                IllegalStateException.class,
                () -> lotCommandService.publish(lotId)
        );

        verify(lotRepository).findById(lotId);
    }

    @Test
    void shouldWithdrawLotSuccessfully() {

        UUID lotId = UUID.randomUUID();

        Lot lot = createLot();

        lot.publish();

        when(lotRepository.findById(lotId))
                .thenReturn(Optional.of(lot));

        when(lotRepository.save(any(Lot.class)))
                .thenAnswer(i -> i.getArgument(0));

        Lot result = lotCommandService.withdraw(lotId);

        assertEquals(
                LotStatus.WITHDRAWN,
                result.getStatus()
        );

        verify(lotRepository).findById(lotId);
        verify(lotRepository).save(any(Lot.class));
    }

    @Test
    void shouldThrowExceptionWhenWithdrawingNonExistingLot() {

        UUID lotId = UUID.randomUUID();

        when(lotRepository.findById(lotId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> lotCommandService.withdraw(lotId)
        );

        verify(lotRepository).findById(lotId);
    }

    @Test
    void shouldThrowExceptionWhenWithdrawingDraftLot() {

        UUID lotId = UUID.randomUUID();

        Lot lot = createLot();

        when(lotRepository.findById(lotId))
                .thenReturn(Optional.of(lot));

        assertThrows(
                IllegalStateException.class,
                () -> lotCommandService.withdraw(lotId)
        );

        verify(lotRepository).findById(lotId);
    }

}