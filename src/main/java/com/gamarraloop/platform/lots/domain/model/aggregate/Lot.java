package com.gamarraloop.platform.lots.domain.model.aggregate;

import com.gamarraloop.platform.lots.domain.model.commands.CreateLotCommand;
import com.gamarraloop.platform.lots.domain.model.commands.UpdateLotCommand;
import com.gamarraloop.platform.lots.domain.model.valueobjects.LotStatus;
import com.gamarraloop.platform.shared.domain.model.AuditableEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "lots")
public class Lot extends AuditableEntity {

    @Column(name = "publisher_id", nullable = false)
    private UUID publisherId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "textile_type", length = 50)
    private String textileType;

    @Column(name = "weight_kg", precision = 6, scale = 2)
    private BigDecimal weightKg;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private LotStatus status;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "pickup_lat", precision = 10, scale = 7)
    private BigDecimal pickupLat;

    @Column(name = "pickup_lng", precision = 10, scale = 7)
    private BigDecimal pickupLng;

    @Column(name = "pickup_ref", length = 300)
    private String pickupRef;

    @Column(name = "published_at")
    private Instant publishedAt;

    protected Lot() {
    }

    public Lot(CreateLotCommand command) {
        this.publisherId = command.publisherId();
        this.title = command.title();
        this.description = command.description();
        this.textileType = command.textileType();
        this.weightKg = command.weightKg();
        this.imageUrl = command.imageUrl();
        this.pickupLat = command.pickupLat();
        this.pickupLng = command.pickupLng();
        this.pickupRef = command.pickupRef();
        this.status = LotStatus.DRAFT;
    }

    public void publish() {
        if (this.status != LotStatus.DRAFT) {
            throw new IllegalStateException("Lot can only be published from DRAFT status. Current status: " + this.status);
        }
        this.status = LotStatus.PUBLISHED;
        this.publishedAt = Instant.now();
    }

    public void withdraw() {
        if (this.status != LotStatus.PUBLISHED) {
            throw new IllegalStateException("Lot can only be withdrawn from PUBLISHED status. Current status: " + this.status);
        }
        this.status = LotStatus.WITHDRAWN;
    }

    public void markReserved() {
        if (this.status != LotStatus.PUBLISHED) {
            throw new IllegalStateException("Lot can only be reserved from PUBLISHED status. Current status: " + this.status);
        }
        this.status = LotStatus.RESERVED;
    }

    /** Devuelve un lote reservado a disponible (cancelación o expiración de la reserva). */
    public void release() {
        if (this.status != LotStatus.RESERVED) {
            throw new IllegalStateException("Lot can only be released from RESERVED status. Current status: " + this.status);
        }
        this.status = LotStatus.PUBLISHED;
    }

    /** Marca el lote como recogido tras confirmar la entrega física. */
    public void markPickedUp() {
        if (this.status != LotStatus.RESERVED) {
            throw new IllegalStateException("Lot can only be picked up from RESERVED status. Current status: " + this.status);
        }
        this.status = LotStatus.PICKED_UP;
    }

    /** Sobrescribe el tipo de textil con el inferido por IA (solo si es un valor útil). */
    public void applyInferredTextileType(String inferredType) {
        if (inferredType != null && !inferredType.isBlank()) {
            this.textileType = inferredType;
        }
    }

    public void updateDetails(UpdateLotCommand cmd) {
        if (this.status != LotStatus.DRAFT) {
            throw new IllegalStateException("Lot details can only be updated in DRAFT status. Current status: " + this.status);
        }
        this.title = cmd.title();
        this.description = cmd.description();
        this.textileType = cmd.textileType();
        this.weightKg = cmd.weightKg();
        this.imageUrl = cmd.imageUrl();
        this.pickupLat = cmd.pickupLat();
        this.pickupLng = cmd.pickupLng();
        this.pickupRef = cmd.pickupRef();
    }

    public UUID getPublisherId() {
        return publisherId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTextileType() {
        return textileType;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public LotStatus getStatus() {
        return status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public BigDecimal getPickupLat() {
        return pickupLat;
    }

    public BigDecimal getPickupLng() {
        return pickupLng;
    }

    public String getPickupRef() {
        return pickupRef;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }
}
