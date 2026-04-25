package com.signalforge.checkpoint.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.signalforge.checkpoint.domain.ChangeSize;
import com.signalforge.checkpoint.domain.ReleaseCheckpoint;
import com.signalforge.checkpoint.domain.ReleasePosture;
import com.signalforge.checkpoint.domain.RolloutStrategy;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReleaseScoringServiceTest {

    private final ReleaseScoringService service = new ReleaseScoringService();

    @Test
    void shouldMarkHealthyReleaseAsReady() {
        ReleaseCheckpoint checkpoint = new ReleaseCheckpoint(
                UUID.randomUUID(),
                "catalog-service",
                "Storefront Team",
                "production",
                Instant.now(),
                RolloutStrategy.CANARY,
                ChangeSize.SMALL,
                98,
                0,
                82,
                91,
                true,
                "Minor ranking improvements.",
                null
        );

        var assessment = service.assess(checkpoint);

        assertEquals(ReleasePosture.READY, assessment.posture());
        assertTrue(assessment.riskScore() < 30);
    }

    @Test
    void shouldHoldHighRiskRelease() {
        ReleaseCheckpoint checkpoint = new ReleaseCheckpoint(
                UUID.randomUUID(),
                "identity-service",
                "Platform",
                "production",
                Instant.now(),
                RolloutStrategy.FULL,
                ChangeSize.LARGE,
                72,
                4,
                10,
                44,
                false,
                "Auth token lifecycle rewrite.",
                null
        );

        var assessment = service.assess(checkpoint);

        assertEquals(ReleasePosture.HOLD, assessment.posture());
        assertTrue(assessment.riskScore() >= 60);
    }
}
