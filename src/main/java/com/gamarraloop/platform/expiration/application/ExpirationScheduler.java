package com.gamarraloop.platform.expiration.application;

import com.gamarraloop.platform.reservations.application.ports.input.ReservationCommandService;
import com.gamarraloop.platform.reservations.application.ports.input.ReservationQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpirationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ExpirationScheduler.class);
    private final ReservationQueryService reservationQueryService;
    private final ReservationCommandService reservationCommandService;

    public ExpirationScheduler(ReservationQueryService reservationQueryService,
                               ReservationCommandService reservationCommandService) {
        this.reservationQueryService = reservationQueryService;
        this.reservationCommandService = reservationCommandService;
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void expireOldReservations() {
        logger.info("Checking for expired reservations...");

        var expiredReservations = reservationQueryService.getExpiredActiveReservations();

        for (var reservation : expiredReservations) {
            try {
                reservationCommandService.expire(reservation.getId());
                logger.info("Successfully expired reservation {}", reservation.getId());
            } catch (Exception e) {
                logger.error("Failed to expire reservation {}", reservation.getId(), e);
            }
        }
    }
}
