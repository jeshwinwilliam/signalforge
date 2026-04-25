import { MetricPill } from "../../components/MetricPill";
import { PostureBadge } from "../../components/PostureBadge";
import type { ReleaseCheckpoint } from "../../api/types";

interface CheckpointCardProps {
  checkpoint: ReleaseCheckpoint;
  onSelect: (checkpoint: ReleaseCheckpoint) => void;
  selected: boolean;
}

export function CheckpointCard({
  checkpoint,
  onSelect,
  selected
}: CheckpointCardProps) {
  return (
    <button
      type="button"
      className={`checkpoint-card ${selected ? "selected" : ""}`}
      onClick={() => onSelect(checkpoint)}
    >
      <div className="checkpoint-card-header">
        <div>
          <p className="eyebrow">{checkpoint.environment}</p>
          <h3>{checkpoint.serviceName}</h3>
        </div>
        <PostureBadge posture={checkpoint.assessment.posture} />
      </div>

      <p className="checkpoint-card-summary">{checkpoint.assessment.summary}</p>

      <div className="checkpoint-card-grid">
        <MetricPill
          label="Risk score"
          value={String(checkpoint.assessment.riskScore)}
        />
        <MetricPill label="Tests" value={`${checkpoint.testPassRate}%`} />
        <MetricPill
          label="Incidents"
          value={String(checkpoint.incidentCount)}
        />
        <MetricPill
          label="Infra"
          value={`${checkpoint.infraHealth}/100`}
        />
      </div>
    </button>
  );
}
