import { useState, type FormEvent } from "react";
import type { ChangeSize, CreateCheckpointInput, RolloutStrategy } from "../../api/types";

interface CheckpointFormProps {
  onSubmit: (payload: CreateCheckpointInput) => Promise<void>;
}

const initialState: CreateCheckpointInput = {
  serviceName: "",
  owner: "",
  environment: "production",
  releaseWindow: new Date(Date.now() + 3_600_000).toISOString().slice(0, 16),
  rolloutStrategy: "CANARY",
  changeSize: "MEDIUM",
  testPassRate: 96,
  incidentCount: 0,
  errorBudgetRemaining: 75,
  infraHealth: 88,
  rollbackReady: true,
  notes: ""
};

export function CheckpointForm({ onSubmit }: CheckpointFormProps) {
  const [form, setForm] = useState(initialState);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setSubmitting(true);

    try {
      await onSubmit({
        ...form,
        releaseWindow: new Date(form.releaseWindow).toISOString()
      });
      setForm(initialState);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <section className="panel form-panel">
      <div className="panel-heading">
        <div>
          <p className="eyebrow">New checkpoint</p>
          <h2>Create a release evaluation</h2>
        </div>
        <p className="panel-copy">
          Capture deployment context and let the scoring engine explain the
          release posture.
        </p>
      </div>

      <form className="checkpoint-form" onSubmit={handleSubmit}>
        <label>
          Service name
          <input
            value={form.serviceName}
            onChange={(event) =>
              setForm((current) => ({ ...current, serviceName: event.target.value }))
            }
            placeholder="checkout-service"
            required
          />
        </label>

        <label>
          Owner
          <input
            value={form.owner}
            onChange={(event) =>
              setForm((current) => ({ ...current, owner: event.target.value }))
            }
            placeholder="Payments Team"
            required
          />
        </label>

        <label>
          Environment
          <input
            value={form.environment}
            onChange={(event) =>
              setForm((current) => ({ ...current, environment: event.target.value }))
            }
            placeholder="production"
            required
          />
        </label>

        <label>
          Release window
          <input
            type="datetime-local"
            value={form.releaseWindow}
            onChange={(event) =>
              setForm((current) => ({ ...current, releaseWindow: event.target.value }))
            }
            required
          />
        </label>

        <label>
          Rollout strategy
          <select
            value={form.rolloutStrategy}
            onChange={(event) =>
              setForm((current) => ({
                ...current,
                rolloutStrategy: event.target.value as RolloutStrategy
              }))
            }
          >
            <option value="FULL">Full rollout</option>
            <option value="CANARY">Canary</option>
            <option value="BLUE_GREEN">Blue-green</option>
          </select>
        </label>

        <label>
          Change size
          <select
            value={form.changeSize}
            onChange={(event) =>
              setForm((current) => ({
                ...current,
                changeSize: event.target.value as ChangeSize
              }))
            }
          >
            <option value="SMALL">Small</option>
            <option value="MEDIUM">Medium</option>
            <option value="LARGE">Large</option>
          </select>
        </label>

        <label>
          Test pass rate
          <input
            type="number"
            min="0"
            max="100"
            value={form.testPassRate}
            onChange={(event) =>
              setForm((current) => ({
                ...current,
                testPassRate: Number(event.target.value)
              }))
            }
          />
        </label>

        <label>
          Active incidents
          <input
            type="number"
            min="0"
            value={form.incidentCount}
            onChange={(event) =>
              setForm((current) => ({
                ...current,
                incidentCount: Number(event.target.value)
              }))
            }
          />
        </label>

        <label>
          Error budget remaining
          <input
            type="number"
            min="0"
            max="100"
            value={form.errorBudgetRemaining}
            onChange={(event) =>
              setForm((current) => ({
                ...current,
                errorBudgetRemaining: Number(event.target.value)
              }))
            }
          />
        </label>

        <label>
          Infrastructure health
          <input
            type="number"
            min="0"
            max="100"
            value={form.infraHealth}
            onChange={(event) =>
              setForm((current) => ({
                ...current,
                infraHealth: Number(event.target.value)
              }))
            }
          />
        </label>

        <label className="checkbox-row">
          <input
            type="checkbox"
            checked={form.rollbackReady}
            onChange={(event) =>
              setForm((current) => ({
                ...current,
                rollbackReady: event.target.checked
              }))
            }
          />
          Rollback plan is ready
        </label>

        <label className="full-span">
          Release notes
          <textarea
            rows={4}
            value={form.notes}
            onChange={(event) =>
              setForm((current) => ({ ...current, notes: event.target.value }))
            }
            placeholder="Summarize the release intent, risk areas, and expected monitoring focus."
          />
        </label>

        <button type="submit" className="primary-button" disabled={submitting}>
          {submitting ? "Evaluating..." : "Evaluate release"}
        </button>
      </form>
    </section>
  );
}
