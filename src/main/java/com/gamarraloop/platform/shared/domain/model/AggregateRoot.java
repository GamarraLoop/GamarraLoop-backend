package com.gamarraloop.platform.shared.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@MappedSuperclass
public abstract class AggregateRoot {

    // El id se asigna en la aplicación: si la entidad ya trae uno (p. ej.
    // UserProfile usa el uid de Supabase Auth, requerido por la FK
    // user_profiles.id -> auth.users.id) se respeta; si no, se genera uno.
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @PrePersist
    protected void ensureId() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    public UUID getId() {
        return id;
    }

    protected void setId(UUID id) {
        this.id = id;
    }
}
