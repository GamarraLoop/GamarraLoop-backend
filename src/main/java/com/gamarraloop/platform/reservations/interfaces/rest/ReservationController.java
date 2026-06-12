package com.gamarraloop.platform.reservations.interfaces.rest;

import com.gamarraloop.platform.lots.application.ports.input.LotQueryService;
import com.gamarraloop.platform.reservations.application.ports.input.ReservationCommandService;
import com.gamarraloop.platform.reservations.application.ports.input.ReservationQueryService;
import com.gamarraloop.platform.reservations.domain.model.aggregate.Reservation;
import com.gamarraloop.platform.reservations.interfaces.rest.resources.CreateReservationResource;
import com.gamarraloop.platform.reservations.interfaces.rest.resources.ReservationResource;
import com.gamarraloop.platform.reservations.interfaces.rest.transform.ReservationAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;
    private final LotQueryService lotQueryService;

    public ReservationController(ReservationCommandService reservationCommandService,
                                 ReservationQueryService reservationQueryService,
                                 LotQueryService lotQueryService) {
        this.reservationCommandService = reservationCommandService;
        this.reservationQueryService = reservationQueryService;
        this.lotQueryService = lotQueryService;
    }

    /** Enriquece la reserva con los datos del lote (título, tipo, dirección) vía join. */
    private ReservationResource toResource(Reservation reservation) {
        var lot = lotQueryService.getById(reservation.getLotId()).orElse(null);
        return ReservationAssembler.toResourceFromEntity(
                reservation,
                lot != null ? lot.getTitle() : null,
                lot != null ? lot.getTextileType() : null,
                lot != null ? lot.getPickupRef() : null
        );
    }

    @PostMapping
    public ResponseEntity<ReservationResource> createReservation(@RequestBody CreateReservationResource resource) {
        Reservation reservation = reservationCommandService.create(ReservationAssembler.toCommandFromResource(resource));
        return new ResponseEntity<>(toResource(reservation), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResource> getReservationById(@PathVariable UUID id) {
        return reservationQueryService.getById(id)
                .map(reservation -> ResponseEntity.ok(toResource(reservation)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lot/{lotId}")
    public ResponseEntity<List<ReservationResource>> getReservationsByLotId(@PathVariable UUID lotId) {
        List<Reservation> reservations = reservationQueryService.getByLotId(lotId);
        List<ReservationResource> resources = reservations.stream()
                .map(this::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/artisan/{artisanId}")
    public ResponseEntity<List<ReservationResource>> getReservationsByArtisanId(@PathVariable UUID artisanId) {
        List<Reservation> reservations = reservationQueryService.getByArtisanId(artisanId);
        List<ReservationResource> resources = reservations.stream()
                .map(this::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ReservationResource> completeReservation(@PathVariable UUID id) {
        try {
            Reservation reservation = reservationCommandService.complete(id);
            return ResponseEntity.ok(toResource(reservation));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationResource> cancelReservation(@PathVariable UUID id) {
        try {
            Reservation reservation = reservationCommandService.cancel(id);
            return ResponseEntity.ok(toResource(reservation));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
