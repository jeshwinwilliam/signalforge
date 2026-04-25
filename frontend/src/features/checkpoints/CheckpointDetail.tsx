import { MetricPill } from "../../components/MetricPill";
import { PostureBadge } from "../../components/PostureBadge";
import type { ReleaseCheckpoint } from "../../api/types";

interface CheckpointDetailProps {
  checkpoint: ReleaseCheckpoint | null;
}

function impactLabel(value: number) {
  return value > 0 ? `+${value}` : `${value}`;
}

export function CheckpointDetail({ checkpoint }: CheckpointDetailProps) {
  if (!checkpoint) {
    return (
      <section className="panel detail-empty">
        <p className="eyebrow">Select a checkpoint</p>
        <h2>Review release reasoning</h2>
        <p>
          Choose a release checkpoint to inspect the risk drivers and
          recommendations behind the score.
        </p>
      </section>
    );
  }

  return (
    <section className="panel detail-panel">
      <div className="detail-header">
        <div>
          <p className="eyebrow">Assessment</p>
          <h2>{checkpoint.serviceName}</h2>
        </div>
        <PostureBadge posture={checkpoint.assessment.posture} />
      </div>

      <div className="detail-metrics">
        <MetricPill
          label="Risk score"
          value={String(checkpoint.assessment.riskScore)}
        />
        <MetricPill label="Owner" value={checkpoint.owner} />
        <MetricPill label="Rollout" value={checkpoint.rolloutStrategy} />
        <MetricPill label="Change size" value={checkpoint.changeSize} />
      </div>

      <p className="detail-summary">{checkpoint.assessment.summary}</p>

      <div className="detail-columns">
        <div>
          <h3>Risk Drivers</h3>
          <div className="driver-list">
            {checkpoint.assessment.drivers.map((driver) => (
              <article key={`${driver.label}-${driver.impact}`} className="driver-item">
                <div className="driver-row">
                  <strong>{driver.label}</strong>
                  <span
                    className={`impact-chip ${driver.impact > 0 ? "risk" : "confidence"}`}
                  >
                    {impactLabel(driver.impact)}
                  </span>
                </div>
                <p>{driver.detail}</p>
              </article>
            ))}
          </div>
        </div>

        <div>
          <h3>Recommended Actions</h3>
          <div className="action-list">
            {checkpoint.assessment.recommendedActions.map((action) => (
              <article key={action} className="action-item">
                <p>{action}</p>
              </article>
            ))}
          </div>
        </div>
      </div>

      <div className="notes-block">
        <h3>Release Notes</h3>
        <p>{checkpoint.notes || "No release notes provided."}</p>
      </div>
    </section>
  );
}
