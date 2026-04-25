package com.signalforge.checkpoint.domain;

import java.util.List;

public record ReleaseAssessment(
        int riskScore,
        ReleasePosture posture,
        String summary,
        List<RiskDriver> drivers,
        List<String> recommendedActions
) {
}
