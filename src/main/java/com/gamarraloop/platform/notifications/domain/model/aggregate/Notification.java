package com.gamarraloop.platform.notifications.domain.model.aggregate;

import com.gamarraloop.platform.notifications.domain.model.commands.CreateNotificationCommand;
import com.gamarraloop.platform.shared.domain.model.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "notifications")
public class Notification extends AuditableEntity {


    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String title;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @NotNull
    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    protected Notification() {
    }

    public Notification(CreateNotificationCommand command) {

        this.userId = command.userId();
        this.title = command.title();
        this.message = command.message();
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }


    public UUID getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getIsRead() {
        return isRead;
    }
}
