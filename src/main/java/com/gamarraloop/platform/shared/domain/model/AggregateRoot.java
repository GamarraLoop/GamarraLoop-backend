package com.gamarraloop.platform.shared.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@MappedSuperclass
public abstract class AggregateRoot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    public UUID getId() {
        return id;
    }

    protected void setId(UUID id) {
        this.id = id;
    }
}
