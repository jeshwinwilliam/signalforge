package com.signalforge.checkpoint.domain;

import java.time.Instant;
import java.util.UUID;

public record ReleaseCheckpoint(
        UUID id,
        String serviceName,
        String owner,
        String environment,
        Instant releaseWindow,
        RolloutStrategy rolloutStrategy,
        ChangeSize changeSize,
        int testPassRate,
        int incidentCount,
        int errorBudgetRemaining,
        int infraHealth,
        boolean rollbackReady,
        String notes,
        ReleaseAssessment assessment
) {
}
