import { useEffect, useState } from "react";
import { createCheckpoint, fetchCheckpoints } from "./api/checkpoints";
import type { CreateCheckpointInput, ReleaseCheckpoint } from "./api/types";
import { CheckpointCard } from "./features/checkpoints/CheckpointCard";
import { CheckpointDetail } from "./features/checkpoints/CheckpointDetail";
import { CheckpointForm } from "./features/checkpoints/CheckpointForm";

export default function App() {
  const [checkpoints, setCheckpoints] = useState<ReleaseCheckpoint[]>([]);
  const [selected, setSelected] = useState<ReleaseCheckpoint | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function load() {
      try {
        const items = await fetchCheckpoints();
        setCheckpoints(items);
        setSelected(items[0] ?? null);
      } catch (loadError) {
        setError(loadError instanceof Error ? loadError.message : "Unable to load data.");
      } finally {
        setLoading(false);
      }
    }

    void load();
  }, []);

  async function handleCreate(payload: CreateCheckpointInput) {
    const created = await createCheckpoint(payload);
    setCheckpoints((current) => [created, ...current]);
    setSelected(created);
    setError(null);
  }

  const readyCount = checkpoints.filter(
    (checkpoint) => checkpoint.assessment.posture === "READY"
  ).length;

  return (
    <div className="app-shell">
      <header className="hero">
        <div className="hero-copy">
          <p className="eyebrow">SignalForge</p>
          <h1>Ship with confidence, not guesswork.</h1>
          <p className="hero-text">
            Evaluate release risk using delivery signals, operational health,
            and rollout strategy in one engineering control room.
          </p>
        </div>

        <div className="hero-stats">
          <article className="stat-card">
            <span>Total checkpoints</span>
            <strong>{checkpoints.length}</strong>
          </article>
          <article className="stat-card">
            <span>Ready to ship</span>
            <strong>{readyCount}</strong>
          </article>
        </div>
      </header>

      <main className="main-grid">
        <CheckpointForm onSubmit={handleCreate} />

        <section className="panel list-panel">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Release board</p>
              <h2>Current checkpoints</h2>
            </div>
            <p className="panel-copy">
              Review live assessments and inspect why each release is ready,
              cautious, or blocked.
            </p>
          </div>

          {loading ? <p>Loading checkpoints...</p> : null}
          {error ? <p className="error-banner">{error}</p> : null}

          <div className="checkpoint-list">
            {checkpoints.map((checkpoint) => (
              <CheckpointCard
                key={checkpoint.id}
                checkpoint={checkpoint}
                onSelect={setSelected}
                selected={checkpoint.id === selected?.id}
              />
            ))}
          </div>
        </section>

        <CheckpointDetail checkpoint={selected} />
      </main>
    </div>
  );
}
