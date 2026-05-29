package com.gamarraloop.platform.lots.interfaces.rest;

import com.gamarraloop.platform.lots.application.ports.input.LotCommandService;
import com.gamarraloop.platform.lots.application.ports.input.LotQueryService;
import com.gamarraloop.platform.lots.domain.model.aggregate.Lot;
import com.gamarraloop.platform.lots.interfaces.rest.resources.CreateLotResource;
import com.gamarraloop.platform.lots.interfaces.rest.resources.LotResource;
import com.gamarraloop.platform.lots.interfaces.rest.resources.UpdateLotResource;
import com.gamarraloop.platform.lots.interfaces.rest.transform.CreateLotCommandFromResourceAssembler;
import com.gamarraloop.platform.lots.interfaces.rest.transform.LotResourceFromEntityAssembler;
import com.gamarraloop.platform.lots.interfaces.rest.transform.UpdateLotCommandFromResourceAssembler;
import com.gamarraloop.platform.shared.domain.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lots")
public class LotController {

    private final LotCommandService lotCommandService;
    private final LotQueryService lotQueryService;

    public LotController(LotCommandService lotCommandService, LotQueryService lotQueryService) {
        this.lotCommandService = lotCommandService;
        this.lotQueryService = lotQueryService;
    }

    @PostMapping
    public ResponseEntity<LotResource> createLot(@Valid @RequestBody CreateLotResource resource) {
        var command = CreateLotCommandFromResourceAssembler.toCommandFromResource(resource);
        var lot = lotCommandService.create(command);
        var lotResource = LotResourceFromEntityAssembler.toResourceFromEntity(lot);
        return new ResponseEntity<>(lotResource, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LotResource> getLotById(@PathVariable UUID id) {
        var lot = lotQueryService.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lot", id));
        var lotResource = LotResourceFromEntityAssembler.toResourceFromEntity(lot);
        return ResponseEntity.ok(lotResource);
    }

    @GetMapping
    public ResponseEntity<List<LotResource>> getLots(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID publisherId) {

        List<Lot> lots;

        if (status != null) {
            lots = lotQueryService.getByStatus(status);
        } else if (publisherId != null) {
            lots = lotQueryService.getByPublisherId(publisherId);
        } else {
            lots = lotQueryService.getAll();
        }

        var lotResources = lots.stream()
                .map(LotResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(lotResources);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LotResource> updateLot(@PathVariable UUID id, @RequestBody UpdateLotResource resource) {
        var command = UpdateLotCommandFromResourceAssembler.toCommandFromResource(resource);
        var lot = lotCommandService.update(id, command);
        var lotResource = LotResourceFromEntityAssembler.toResourceFromEntity(lot);
        return ResponseEntity.ok(lotResource);
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<LotResource> publishLot(@PathVariable UUID id) {
        var lot = lotCommandService.publish(id);
        var lotResource = LotResourceFromEntityAssembler.toResourceFromEntity(lot);
        return ResponseEntity.ok(lotResource);
    }

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<LotResource> withdrawLot(@PathVariable UUID id) {
        var lot = lotCommandService.withdraw(id);
        var lotResource = LotResourceFromEntityAssembler.toResourceFromEntity(lot);
        return ResponseEntity.ok(lotResource);
    }
}
