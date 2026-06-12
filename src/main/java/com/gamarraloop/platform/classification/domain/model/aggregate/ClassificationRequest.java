package com.gamarraloop.platform.classification.domain.model.aggregate;

import com.gamarraloop.platform.classification.domain.model.commands.RequestClassificationCommand;
import com.gamarraloop.platform.classification.domain.model.valueobjects.ClassificationStatus;
import com.gamarraloop.platform.shared.domain.model.AuditableEntity;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "classification_requests")
public class ClassificationRequest extends AuditableEntity {

    @Column(name = "lot_id", nullable = true)
    private UUID lotId;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ClassificationStatus status;

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(name = "labels", columnDefinition = "jsonb")
    private String labels;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    @Column(name = "requested_at", nullable = false, updatable = false)
    private Instant requestedAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    protected ClassificationRequest() {
    }

    public ClassificationRequest(RequestClassificationCommand command) {
        this.imageUrl = command.imageUrl();
        this.status = ClassificationStatus.PENDING;
        this.requestedAt = Instant.now();
    }

    public void markProcessing() {
        this.status = ClassificationStatus.PROCESSING;
    }

    public void completeWithLabels(String labelsJson) {
        this.status = ClassificationStatus.COMPLETED;
        this.labels = labelsJson;
        this.processedAt = Instant.now();
    }

    public void failWithReason(String reason) {
        this.status = ClassificationStatus.FAILED;
        this.failureReason = reason;
        this.processedAt = Instant.now();
    }


    public UUID getLotId() {
        return lotId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ClassificationStatus getStatus() {
        return status;
    }

    public String getLabels() {
        return labels;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }
}
