export type RolloutStrategy = "FULL" | "CANARY" | "BLUE_GREEN";
export type ChangeSize = "SMALL" | "MEDIUM" | "LARGE";
export type ReleasePosture = "READY" | "CAUTION" | "HOLD";

export interface RiskDriver {
  label: string;
  impact: number;
  detail: string;
}

export interface ReleaseAssessment {
  riskScore: number;
  posture: ReleasePosture;
  summary: string;
  drivers: RiskDriver[];
  recommendedActions: string[];
}

export interface ReleaseCheckpoint {
  id: string;
  serviceName: string;
  owner: string;
  environment: string;
  releaseWindow: string;
  rolloutStrategy: RolloutStrategy;
  changeSize: ChangeSize;
  testPassRate: number;
  incidentCount: number;
  errorBudgetRemaining: number;
  infraHealth: number;
  rollbackReady: boolean;
  notes: string;
  assessment: ReleaseAssessment;
}

export interface CreateCheckpointInput {
  serviceName: string;
  owner: string;
  environment: string;
  releaseWindow: string;
  rolloutStrategy: RolloutStrategy;
  changeSize: ChangeSize;
  testPassRate: number;
  incidentCount: number;
  errorBudgetRemaining: number;
  infraHealth: number;
  rollbackReady: boolean;
  notes: string;
}
