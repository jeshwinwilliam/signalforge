package com.signalforge.checkpoint.api;

import com.signalforge.checkpoint.domain.ChangeSize;
import com.signalforge.checkpoint.domain.RolloutStrategy;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record CreateCheckpointRequest(
        @NotBlank String serviceName,
        @NotBlank String owner,
        @NotBlank String environment,
        @NotNull Instant releaseWindow,
        @NotNull RolloutStrategy rolloutStrategy,
        @NotNull ChangeSize changeSize,
        @Min(0) @Max(100) int testPassRate,
        @Min(0) int incidentCount,
        @Min(0) @Max(100) int errorBudgetRemaining,
        @Min(0) @Max(100) int infraHealth,
        boolean rollbackReady,
        String notes
) {
}
