package com.signalforge.checkpoint.service;

import com.signalforge.checkpoint.api.CreateCheckpointRequest;
import com.signalforge.checkpoint.domain.ReleaseAssessment;
import com.signalforge.checkpoint.domain.ReleaseCheckpoint;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Service;

@Service
public class CheckpointService {

    private final ReleaseScoringService scoringService;
    private final List<ReleaseCheckpoint> store = new CopyOnWriteArrayList<>();

    public CheckpointService(ReleaseScoringService scoringService) {
        this.scoringService = scoringService;
        seedData();
    }

    public List<ReleaseCheckpoint> findAll() {
        return store.stream()
                .sorted(Comparator.comparing(ReleaseCheckpoint::releaseWindow).reversed())
                .toList();
    }

    public ReleaseCheckpoint findById(UUID id) {
        return store.stream()
                .filter(checkpoint -> checkpoint.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new CheckpointNotFoundException(id));
    }

    public ReleaseCheckpoint create(CreateCheckpointRequest request) {
        ReleaseCheckpoint draft = new ReleaseCheckpoint(
                UUID.randomUUID(),
                request.serviceName(),
                request.owner(),
                request.environment(),
                request.releaseWindow(),
                request.rolloutStrategy(),
                request.changeSize(),
                request.testPassRate(),
                request.incidentCount(),
                request.errorBudgetRemaining(),
                request.infraHealth(),
                request.rollbackReady(),
                request.notes(),
                null
        );

        ReleaseAssessment assessment = scoringService.assess(draft);
        ReleaseCheckpoint created = new ReleaseCheckpoint(
                draft.id(),
                draft.serviceName(),
                draft.owner(),
                draft.environment(),
                draft.releaseWindow(),
                draft.rolloutStrategy(),
                draft.changeSize(),
                draft.testPassRate(),
                draft.incidentCount(),
                draft.errorBudgetRemaining(),
                draft.infraHealth(),
                draft.rollbackReady(),
                draft.notes(),
                assessment
        );
        store.add(created);
        return created;
    }

    private void seedData() {
        if (!store.isEmpty()) {
            return;
        }

        store.add(assessedCheckpoint(
                "checkout-service",
                "Payments Team",
                "production",
                Instant.now().plusSeconds(7200),
                97,
                1,
                74,
                89,
                true,
                "Retry path refinement and observability tuning."
        ));
        store.add(assessedCheckpoint(
                "identity-gateway",
                "Platform Security",
                "production",
                Instant.now().plusSeconds(14400),
                84,
                3,
                19,
                58,
                false,
                "Token renewal patch and session invalidation updates."
        ));
    }

    private ReleaseCheckpoint assessedCheckpoint(
            String serviceName,
            String owner,
            String environment,
            Instant releaseWindow,
            int testPassRate,
            int incidentCount,
            int errorBudgetRemaining,
            int infraHealth,
            boolean rollbackReady,
            String notes
    ) {
        ReleaseCheckpoint draft = new ReleaseCheckpoint(
                UUID.randomUUID(),
                serviceName,
                owner,
                environment,
                releaseWindow,
                com.signalforge.checkpoint.domain.RolloutStrategy.CANARY,
                com.signalforge.checkpoint.domain.ChangeSize.MEDIUM,
                testPassRate,
                incidentCount,
                errorBudgetRemaining,
                infraHealth,
                rollbackReady,
                notes,
                null
        );
        return new ReleaseCheckpoint(
                draft.id(),
                draft.serviceName(),
                draft.owner(),
                draft.environment(),
                draft.releaseWindow(),
                draft.rolloutStrategy(),
                draft.changeSize(),
                draft.testPassRate(),
                draft.incidentCount(),
                draft.errorBudgetRemaining(),
                draft.infraHealth(),
                draft.rollbackReady(),
                draft.notes(),
                scoringService.assess(draft)
        );
    }
}
