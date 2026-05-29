package com.gamarraloop.platform.delivery.domain.model.aggregate;

import com.gamarraloop.platform.delivery.domain.model.commands.StartDeliveryCommand;
import com.gamarraloop.platform.delivery.domain.model.valueobjects.DeliveryStatus;
import com.gamarraloop.platform.shared.domain.model.AuditableEntity;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "deliveries")
public class DeliveryProcess extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID reservationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @Column(nullable = false)
    private Instant startedAt;

    private Instant completedAt;

    public DeliveryProcess() {
        // Required by JPA
    }

    public DeliveryProcess(StartDeliveryCommand command) {
        this.reservationId = command.reservationId();
        this.status = DeliveryStatus.IN_TRANSIT;
        this.startedAt = Instant.now();
    }

    public void complete() {
        this.status = DeliveryStatus.DELIVERED;
        this.completedAt = Instant.now();
    }

    public void fail() {
        this.status = DeliveryStatus.FAILED;
        this.completedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }
}
