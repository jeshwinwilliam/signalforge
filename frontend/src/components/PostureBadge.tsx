import type { ReleasePosture } from "../api/types";

interface PostureBadgeProps {
  posture: ReleasePosture;
}

export function PostureBadge({ posture }: PostureBadgeProps) {
  return <span className={`posture-badge posture-${posture.toLowerCase()}`}>{posture}</span>;
}
