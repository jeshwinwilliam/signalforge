package com.signalforge.checkpoint.service;

import com.signalforge.checkpoint.domain.ChangeSize;
import com.signalforge.checkpoint.domain.ReleaseAssessment;
import com.signalforge.checkpoint.domain.ReleaseCheckpoint;
import com.signalforge.checkpoint.domain.ReleasePosture;
import com.signalforge.checkpoint.domain.RiskDriver;
import com.signalforge.checkpoint.domain.RolloutStrategy;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReleaseScoringService {

    public ReleaseAssessment assess(ReleaseCheckpoint checkpoint) {
        int score = 15;
        List<RiskDriver> drivers = new ArrayList<>();
        List<String> actions = new ArrayList<>();

        if (checkpoint.testPassRate() < 85) {
            score += 28;
            drivers.add(new RiskDriver("Test confidence", 28, "Test pass rate is below 85%."));
            actions.add("Stabilize failing test suites before approving the release.");
        } else if (checkpoint.testPassRate() < 95) {
            score += 12;
            drivers.add(new RiskDriver("Test confidence", 12, "Test pass rate is below the target confidence band."));
            actions.add("Review flaky or recently failing tests before rollout.");
        } else {
            drivers.add(new RiskDriver("Test confidence", -6, "High test pass rate improves confidence."));
        }

        if (checkpoint.incidentCount() >= 3) {
            score += 24;
            drivers.add(new RiskDriver("Active incidents", 24, "Multiple active incidents raise deployment risk."));
            actions.add("Reduce active incidents before shipping production changes.");
        } else if (checkpoint.incidentCount() > 0) {
            score += 10;
            drivers.add(new RiskDriver("Active incidents", 10, "There are unresolved incidents associated with the service."));
            actions.add("Confirm incident blast radius and assign owners during rollout.");
        }

        if (checkpoint.errorBudgetRemaining() < 25) {
            score += 18;
            drivers.add(new RiskDriver("Error budget", 18, "The service has limited error budget remaining."));
            actions.add("Delay the release or prepare a tighter rollback watch window.");
        } else if (checkpoint.errorBudgetRemaining() > 70) {
            drivers.add(new RiskDriver("Error budget", -4, "Healthy error budget gives room for controlled changes."));
        }

        if (checkpoint.infraHealth() < 60) {
            score += 20;
            drivers.add(new RiskDriver("Infrastructure health", 20, "Infrastructure health is poor."));
            actions.add("Resolve platform instability before rollout.");
        } else if (checkpoint.infraHealth() < 80) {
            score += 8;
            drivers.add(new RiskDriver("Infrastructure health", 8, "Infrastructure health is acceptable but not ideal."));
        } else {
            drivers.add(new RiskDriver("Infrastructure health", -5, "Infrastructure health is strong."));
        }

        if (checkpoint.changeSize() == ChangeSize.LARGE) {
            score += 15;
            drivers.add(new RiskDriver("Change size", 15, "This is a large release with broader blast radius."));
            actions.add("Split non-critical scope or deploy behind a feature flag.");
        } else if (checkpoint.changeSize() == ChangeSize.SMALL) {
            drivers.add(new RiskDriver("Change size", -3, "Small changes lower operational uncertainty."));
        }

        if (!checkpoint.rollbackReady()) {
            score += 14;
            drivers.add(new RiskDriver("Rollback readiness", 14, "Rollback path is not confirmed."));
            actions.add("Document and validate a rollback plan before approval.");
        } else {
            drivers.add(new RiskDriver("Rollback readiness", -7, "Rollback readiness lowers operational risk."));
        }

        if (checkpoint.rolloutStrategy() == RolloutStrategy.CANARY) {
            drivers.add(new RiskDriver("Rollout strategy", -5, "Canary rollout reduces initial exposure."));
            actions.add("Monitor service KPIs for the first canary segment.");
        } else if (checkpoint.rolloutStrategy() == RolloutStrategy.BLUE_GREEN) {
            drivers.add(new RiskDriver("Rollout strategy", -4, "Blue-green rollout supports cleaner fallback."));
        } else {
            score += 4;
            drivers.add(new RiskDriver("Rollout strategy", 4, "Full rollout increases initial exposure."));
        }

        int normalizedScore = Math.max(0, Math.min(score, 100));
        ReleasePosture posture = postureFor(normalizedScore);
        String summary = summaryFor(posture);

        if (actions.isEmpty()) {
            actions.add("Proceed with the release and monitor service health during the deployment window.");
        }

        return new ReleaseAssessment(normalizedScore, posture, summary, drivers, actions);
    }

    private ReleasePosture postureFor(int score) {
        if (score >= 60) {
            return ReleasePosture.HOLD;
        }
        if (score >= 30) {
            return ReleasePosture.CAUTION;
        }
        return ReleasePosture.READY;
    }

    private String summaryFor(ReleasePosture posture) {
        return switch (posture) {
            case READY -> "Release conditions are healthy with manageable risk.";
            case CAUTION -> "Release can proceed, but it should be tightly monitored and deliberately staged.";
            case HOLD -> "Release risk is elevated. Resolve the highest-impact issues before shipping.";
        };
    }
}
