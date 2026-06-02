package com.gamarraloop.platform.reservations.domain.model.aggregate;

import com.gamarraloop.platform.reservations.domain.model.commands.CreateReservationCommand;
import com.gamarraloop.platform.reservations.domain.model.valueobjects.ReservationStatus;
import com.gamarraloop.platform.shared.domain.model.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class Reservation extends AuditableEntity {


    @NotNull
    @Column(name = "lot_id", nullable = false)
    private UUID lotId;

    @NotNull
    @Column(name = "artisan_id", nullable = false)
    private UUID artisanId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    @Column(name = "reserved_at")
    private Instant reservedAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    protected Reservation() {
        // JPA requires default constructor
    }

    public Reservation(CreateReservationCommand command) {
        this.lotId = command.lotId();
        this.artisanId = command.artisanId();
        this.status = ReservationStatus.ACTIVE;
        this.reservedAt = Instant.now();
        this.expiresAt = this.reservedAt.plus(24, ChronoUnit.HOURS);
    }

    public void complete() {
        if (this.status != ReservationStatus.ACTIVE) {
            throw new IllegalStateException("Only ACTIVE reservations can be completed. Current status: " + this.status);
        }
        this.status = ReservationStatus.COMPLETED;
    }

    public void cancel() {
        if (this.status != ReservationStatus.ACTIVE) {
            throw new IllegalStateException("Only ACTIVE reservations can be cancelled. Current status: " + this.status);
        }
        this.status = ReservationStatus.CANCELLED;
    }

    public void expire() {
        if (this.status != ReservationStatus.ACTIVE) {
            throw new IllegalStateException("Only ACTIVE reservations can be expired. Current status: " + this.status);
        }
        this.status = ReservationStatus.EXPIRED;
    }


    public UUID getLotId() {
        return lotId;
    }

    public UUID getArtisanId() {
        return artisanId;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public Instant getReservedAt() {
        return reservedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
