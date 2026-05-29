package com.gamarraloop.platform.delivery.interfaces.rest;

import com.gamarraloop.platform.delivery.application.ports.input.DeliveryCommandService;
import com.gamarraloop.platform.delivery.application.ports.input.DeliveryQueryService;
import com.gamarraloop.platform.delivery.domain.model.aggregate.DeliveryProcess;
import com.gamarraloop.platform.delivery.interfaces.rest.resources.DeliveryResource;
import com.gamarraloop.platform.delivery.interfaces.rest.resources.StartDeliveryResource;
import com.gamarraloop.platform.delivery.interfaces.rest.transform.DeliveryResourceFromEntityAssembler;
import com.gamarraloop.platform.delivery.interfaces.rest.transform.StartDeliveryCommandFromResourceAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryCommandService deliveryCommandService;
    private final DeliveryQueryService deliveryQueryService;

    public DeliveryController(DeliveryCommandService deliveryCommandService, DeliveryQueryService deliveryQueryService) {
        this.deliveryCommandService = deliveryCommandService;
        this.deliveryQueryService = deliveryQueryService;
    }

    @PostMapping("/start")
    public ResponseEntity<DeliveryResource> startDelivery(@Valid @RequestBody StartDeliveryResource resource) {
        DeliveryProcess deliveryProcess = deliveryCommandService.startDelivery(
                StartDeliveryCommandFromResourceAssembler.toCommandFromResource(resource)
        );
        return new ResponseEntity<>(
                DeliveryResourceFromEntityAssembler.toResourceFromEntity(deliveryProcess),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResource> getById(@PathVariable UUID id) {
        return deliveryQueryService.getById(id)
                .map(DeliveryResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<List<DeliveryResource>> getByReservationId(@PathVariable UUID reservationId) {
        List<DeliveryResource> resources = deliveryQueryService.getByReservationId(reservationId)
                .stream()
                .map(DeliveryResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<DeliveryResource> completeDelivery(@PathVariable UUID id) {
        try {
            DeliveryProcess deliveryProcess = deliveryCommandService.completeDelivery(id);
            return ResponseEntity.ok(DeliveryResourceFromEntityAssembler.toResourceFromEntity(deliveryProcess));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/fail")
    public ResponseEntity<DeliveryResource> failDelivery(@PathVariable UUID id) {
        try {
            DeliveryProcess deliveryProcess = deliveryCommandService.failDelivery(id);
            return ResponseEntity.ok(DeliveryResourceFromEntityAssembler.toResourceFromEntity(deliveryProcess));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
