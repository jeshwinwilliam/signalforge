import type { CreateCheckpointInput, ReleaseCheckpoint } from "./types";

const API_BASE = "http://localhost:8080/api/checkpoints";

export async function fetchCheckpoints(): Promise<ReleaseCheckpoint[]> {
  const response = await fetch(API_BASE);
  if (!response.ok) {
    throw new Error("Unable to load checkpoints.");
  }
  return response.json();
}

export async function createCheckpoint(
  payload: CreateCheckpointInput
): Promise<ReleaseCheckpoint> {
  const response = await fetch(API_BASE, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(payload)
  });

  if (!response.ok) {
    throw new Error("Unable to create checkpoint.");
  }

  return response.json();
}
