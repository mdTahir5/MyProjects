package com.phonebook.service;

import com.phonebook.repository.RevokedTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Housekeeping jobs.
 */
@Service
public class MaintenanceService {

    private static final Logger log = LoggerFactory.getLogger(MaintenanceService.class);

    private final RevokedTokenRepository revokedTokenRepository;

    public MaintenanceService(RevokedTokenRepository revokedTokenRepository) {
        this.revokedTokenRepository = revokedTokenRepository;
    }

    /**
     * Removes deny-list rows whose token has already expired naturally - they can
     * never be presented again, so keeping them has no value.
     *
     * <p>
     * Runs daily at 03:00 by default (see {@code app.admin.token-cleanup-cron}).
     * </p>
     */
    @Scheduled(cron = "${app.admin.token-cleanup-cron:0 0 3 * * *}")
    @Transactional
    public void purgeExpiredRevokedTokens() {
        int removed = revokedTokenRepository.deleteAllExpiredBefore(Instant.now());
        if (removed > 0) {
            log.info("Purged {} expired revoked token(s)", removed);
        }
    }
}
