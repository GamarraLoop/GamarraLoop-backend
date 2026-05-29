package com.gamarraloop.platform.notifications.interfaces.rest;

import com.gamarraloop.platform.notifications.application.ports.input.NotificationCommandService;
import com.gamarraloop.platform.notifications.application.ports.input.NotificationQueryService;
import com.gamarraloop.platform.notifications.interfaces.rest.resources.CreateNotificationResource;
import com.gamarraloop.platform.notifications.interfaces.rest.resources.NotificationResource;
import com.gamarraloop.platform.notifications.interfaces.rest.transform.CreateNotificationCommandFromResourceAssembler;
import com.gamarraloop.platform.notifications.interfaces.rest.transform.NotificationResourceFromEntityAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;

    public NotificationController(NotificationCommandService notificationCommandService, NotificationQueryService notificationQueryService) {
        this.notificationCommandService = notificationCommandService;
        this.notificationQueryService = notificationQueryService;
    }

    @PostMapping
    public ResponseEntity<NotificationResource> createNotification(@RequestBody CreateNotificationResource resource) {
        var command = CreateNotificationCommandFromResourceAssembler.toCommandFromResource(resource);
        var notification = notificationCommandService.create(command);
        return notification.map(n -> new ResponseEntity<>(NotificationResourceFromEntityAssembler.toResourceFromEntity(n), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResource> getNotificationById(@PathVariable UUID id) {
        var notification = notificationQueryService.getById(id);
        return notification.map(n -> ResponseEntity.ok(NotificationResourceFromEntityAssembler.toResourceFromEntity(n)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResource>> getNotificationsByUserId(
            @PathVariable UUID userId,
            @RequestParam(required = false) Boolean unread) {
        
        List<com.gamarraloop.platform.notifications.domain.model.aggregate.Notification> notifications;
        if (Boolean.TRUE.equals(unread)) {
            notifications = notificationQueryService.getUnreadByUserId(userId);
        } else {
            notifications = notificationQueryService.getByUserId(userId);
        }

        var resources = notifications.stream()
                .map(NotificationResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResource> markAsRead(@PathVariable UUID id) {
        var notification = notificationCommandService.markAsRead(id);
        return notification.map(n -> ResponseEntity.ok(NotificationResourceFromEntityAssembler.toResourceFromEntity(n)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
