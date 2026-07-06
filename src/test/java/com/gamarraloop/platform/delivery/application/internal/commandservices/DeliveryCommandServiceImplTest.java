package com.gamarraloop.platform.delivery.application.internal.commandservices;

import com.gamarraloop.platform.delivery.domain.model.aggregate.DeliveryProcess;
import com.gamarraloop.platform.delivery.domain.model.commands.StartDeliveryCommand;
import com.gamarraloop.platform.delivery.domain.model.valueobjects.DeliveryStatus;
import com.gamarraloop.platform.delivery.infrastructure.persistence.jpa.DeliveryRepository;
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
class DeliveryCommandServiceImplTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private DeliveryCommandServiceImpl deliveryCommandService;

    @Test
    void shouldStartDeliverySuccessfully() {
        UUID reservationId = UUID.randomUUID();
        StartDeliveryCommand command = new StartDeliveryCommand(reservationId);

        when(deliveryRepository.save(ArgumentMatchers.any(DeliveryProcess.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DeliveryProcess result = deliveryCommandService.startDelivery(command);

        Assertions.assertEquals(reservationId, result.getReservationId());
        Assertions.assertEquals(DeliveryStatus.IN_TRANSIT, result.getStatus());

        verify(deliveryRepository).save(any(DeliveryProcess.class));
    }

    @Test
    void shouldCompleteDeliverySuccessfully() {
        UUID id = UUID.randomUUID();

        DeliveryProcess delivery =
                new DeliveryProcess(new StartDeliveryCommand(UUID.randomUUID()));

        when(deliveryRepository.findById(id))
                .thenReturn(Optional.of(delivery));

        when(deliveryRepository.save(any(DeliveryProcess.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DeliveryProcess result =
                deliveryCommandService.completeDelivery(id);

        Assertions.assertEquals(DeliveryStatus.DELIVERED, result.getStatus());

        verify(deliveryRepository).findById(id);
        verify(deliveryRepository).save(delivery);
    }

    @Test
    void shouldThrowExceptionWhenCompletingNonExistingDelivery() {
        UUID id = UUID.randomUUID();

        when(deliveryRepository.findById(id))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> deliveryCommandService.completeDelivery(id)
        );

        verify(deliveryRepository).findById(id);
        verify(deliveryRepository, never()).save(any());
    }

    @Test
    void shouldFailDeliverySuccessfully() {
        UUID id = UUID.randomUUID();

        DeliveryProcess delivery =
                new DeliveryProcess(new StartDeliveryCommand(UUID.randomUUID()));

        when(deliveryRepository.findById(id))
                .thenReturn(Optional.of(delivery));

        when(deliveryRepository.save(any(DeliveryProcess.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DeliveryProcess result =
                deliveryCommandService.failDelivery(id);

        Assertions.assertEquals(DeliveryStatus.FAILED, result.getStatus());

        verify(deliveryRepository).findById(id);
        verify(deliveryRepository).save(delivery);
    }

    @Test
    void shouldThrowExceptionWhenFailingNonExistingDelivery() {
        UUID id = UUID.randomUUID();

        when(deliveryRepository.findById(id))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> deliveryCommandService.failDelivery(id)
        );

        verify(deliveryRepository).findById(id);
        verify(deliveryRepository, never()).save(any());
    }
}