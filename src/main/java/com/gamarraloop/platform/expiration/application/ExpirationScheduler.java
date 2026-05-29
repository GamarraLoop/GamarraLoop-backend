package com.gamarraloop.platform.expiration.application;

import com.gamarraloop.platform.reservations.application.ports.input.ReservationCommandService;
import com.gamarraloop.platform.reservations.infrastructure.persistence.jpa.ReservationRepository;
import com.gamarraloop.platform.reservations.domain.model.valueobjects.ReservationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ExpirationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ExpirationScheduler.class);
    private final ReservationRepository reservationRepository;
    private final ReservationCommandService reservationCommandService;

    public ExpirationScheduler(ReservationRepository reservationRepository,
                               ReservationCommandService reservationCommandService) {
        this.reservationRepository = reservationRepository;
        this.reservationCommandService = reservationCommandService;
    }

    // Run every minute
    @Scheduled(cron = "0 * * * * *")
    public void expireOldReservations() {
        logger.info("Checking for expired reservations...");
        
        var activeReservations = reservationRepository.findAll().stream()
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                .filter(r -> r.getExpiresAt() != null && r.getExpiresAt().isBefore(Instant.now()))
                .toList();

        for (var reservation : activeReservations) {
            try {
                reservationCommandService.expire(reservation.getId());
                logger.info("Successfully expired reservation {}", reservation.getId());
            } catch (Exception e) {
                logger.error("Failed to expire reservation {}", reservation.getId(), e);
            }
        }
    }
}
