# SignalForge

SignalForge is a full-stack release readiness studio for engineering teams. It helps a team decide whether a software release is safe to ship by combining delivery signals, incident exposure, test confidence, infrastructure health, and rollout strategy into one operational view.

Instead of acting like a generic project tracker, SignalForge answers a sharper question:

`Should we ship this release right now, and if not, what should we fix first?`

This repository is intentionally designed as an interview-ready solo project. It demonstrates backend modeling, scoring logic, API design, frontend state management, UI composition, and the ability to present a product with a clear engineering narrative.

## Why This Project

Most portfolio full-stack apps stop at task boards, notes, ecommerce clones, or social feeds. SignalForge is different because it is built around a real decision workflow that software teams deal with:

- balancing speed against release risk
- interpreting noisy operational signals
- turning metrics into clear next actions
- supporting “ship / delay / canary / rollback plan” decisions

That makes it a strong project for a software developer interview because it gives room to discuss:

- system design
- business logic and domain modeling
- score calculation and explainability
- API design
- frontend composition around a meaningful workflow
- future extensibility into event-driven or ML-assisted analysis

## Product Overview

SignalForge lets a user create a release checkpoint for a service or deployment candidate. A checkpoint includes engineering inputs such as:

- service name and owner
- deployment window
- test pass rate
- open incidents
- error budget remaining
- infrastructure health
- change size
- rollback readiness
- rollout strategy

The backend evaluates those inputs and produces:

- an overall release risk score from 0 to 100
- a release posture: `READY`, `CAUTION`, or `HOLD`
- weighted risk factors
- suggested actions
- a generated shipping recommendation

The frontend presents these results in a dashboard that helps a reviewer understand both the score and the reasons behind it.

## Core User Flow

1. A user opens the dashboard and sees the current release checkpoints.
2. The user creates a new checkpoint for an upcoming deployment.
3. The backend evaluates the submission using a weighted scoring model.
4. The frontend immediately renders:
   - score summary
   - posture badge
   - factor breakdown
   - recommended next steps
5. The user can inspect details to understand what increased or reduced release confidence.

## Tech Stack

### Backend

- Java 21
- Spring Boot 3
- Maven
- Spring Web
- Spring Validation
- JUnit 5 / Spring Boot Test

### Frontend

- React 18
- Vite
- TypeScript
- CSS with a custom design system using CSS variables

## Repository Structure

```text
signalforge/
├── README.md
├── backend/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/signalforge/
│       │   ├── SignalForgeApplication.java
│       │   ├── checkpoint/
│       │   │   ├── api/
│       │   │   ├── domain/
│       │   │   └── service/
│       │   └── shared/
│       └── test/java/com/signalforge/
└── frontend/
    ├── package.json
    ├── vite.config.ts
    └── src/
        ├── api/
        ├── components/
        ├── features/
        └── styles/
```

## Architecture

### Backend Architecture

The backend is structured around a single business capability: release checkpoint evaluation.

- `api` contains HTTP request and response models plus REST controllers
- `domain` contains immutable records and enums that represent release data
- `service` contains the scoring engine, recommendations generator, and in-memory repository
- `shared` contains cross-cutting configuration such as CORS

This separation keeps the domain logic easy to explain in an interview. The controller stays thin, while the service layer owns the release-evaluation rules.

### Frontend Architecture

The frontend uses feature-oriented React organization:

- `api` contains backend communication helpers
- `components` contains shared UI building blocks
- `features/checkpoints` contains form, cards, and dashboard composition
- `styles` contains tokens and global styling

The UI is designed to feel like a product dashboard instead of a tutorial starter. It prioritizes:

- scanability
- explainability of the score
- quick comparison between readiness factors

## Domain Model

The core domain entity is a `ReleaseCheckpoint`.

Each checkpoint captures delivery and operational context for a release candidate:

- `serviceName`
- `owner`
- `environment`
- `releaseWindow`
- `rolloutStrategy`
- `changeSize`
- `testPassRate`
- `incidentCount`
- `errorBudgetRemaining`
- `infraHealth`
- `rollbackReady`
- `notes`

The backend transforms this raw input into a `ReleaseAssessment` containing:

- `riskScore`
- `posture`
- `summary`
- `drivers`
- `recommendedActions`

## Scoring Logic

The current scoring model is deterministic and explainable.

The engine starts from a healthy baseline and adjusts the score using weighted rules:

- lower test pass rates reduce confidence
- more active incidents increase risk
- low error budget remaining increases risk
- poor infrastructure health increases risk
- large changes increase risk
- rollback readiness reduces risk
- canary rollout slightly reduces risk compared to full rollout

This is intentionally opinionated. It creates an interview-friendly place to discuss:

- why certain signals matter
- how weights were chosen
- how the model could evolve
- how explainability is preserved

## API Design

### `GET /api/checkpoints`

Returns all checkpoints with their computed assessment.

### `POST /api/checkpoints`

Creates a new release checkpoint and returns the stored checkpoint plus assessment.

### `GET /api/checkpoints/{id}`

Returns a single checkpoint by id.

## Example Request

```json
{
  "serviceName": "checkout-service",
  "owner": "Payments Team",
  "environment": "production",
  "releaseWindow": "2026-04-25T20:30:00Z",
  "rolloutStrategy": "CANARY",
  "changeSize": "MEDIUM",
  "testPassRate": 96,
  "incidentCount": 1,
  "errorBudgetRemaining": 72,
  "infraHealth": 88,
  "rollbackReady": true,
  "notes": "Includes payment retry fix and logging improvements."
}
```

## Example Response Shape

```json
{
  "id": "generated-uuid",
  "serviceName": "checkout-service",
  "assessment": {
    "riskScore": 26,
    "posture": "READY",
    "summary": "Release conditions are healthy with manageable risk.",
    "drivers": [
      {
        "label": "Test confidence",
        "impact": -10
      }
    ],
    "recommendedActions": [
      "Proceed with canary rollout and monitor payment error rates for 30 minutes."
    ]
  }
}
```

## Frontend Experience

The React app includes:

- a strong landing dashboard
- a release checkpoint submission form
- live creation of checkpoints
- score cards and factor chips
- a detail panel for recommendations and reasoning

The visual direction is intentionally more product-like than classroom-like:

- warm neutral background with signal accents
- bold typography
- layered panels
- responsive layout for desktop and laptop walkthroughs

## How to Run Locally

### Backend

```bash
cd backend
mvn spring-boot:run
```

The API runs on `http://localhost:8080`.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend runs on `http://localhost:5173`.

## Testing

### Backend tests

```bash
cd backend
mvn test
```

Backend tests focus on:

- release score calculation
- posture classification
- API behavior

### Frontend

The current frontend is scaffolded for rapid iteration. If expanded, the next test layer would include:

- React Testing Library component tests
- API mocking for checkpoint creation
- visual regression checks for dashboard cards

## What Makes This Strong In An Interview

This project creates room for a concise but impressive walkthrough:

- demo the product in under two minutes
- explain the backend scoring engine
- show clear separation between transport, domain, and service logic
- discuss tradeoffs of deterministic scoring vs learned models
- show how the frontend visualizes reasoning, not just data

It also supports thoughtful engineering discussion:

- Why use immutable records in Java for domain objects?
- Why keep the controller thin?
- How would persistence be added cleanly?
- How would this evolve into event ingestion from CI/CD and observability tools?

## Tradeoffs

This version intentionally uses in-memory storage to keep the project focused on business logic and UI experience. In a production version, I would extend it with:

- PostgreSQL persistence
- authentication and role-aware approval workflows
- historical checkpoint analytics
- webhook ingestion from GitHub Actions, Datadog, or incident systems
- audit trails and release decision history

## Future Improvements

- Persist checkpoints and assessments in PostgreSQL
- Add trend analysis for a service across multiple releases
- Model approval chains and deployment gates
- Add team-specific scoring profiles
- Add release simulation mode to compare rollout strategies
- Stream live status updates with server-sent events or WebSocket events

## Suggested Video Walkthrough Outline

If you use this repository for the interview submission, a good 6 to 8 minute flow is:

1. Start with the problem:
   “Teams often have deployment signals spread across tools. SignalForge centralizes that into one release decision workflow.”
2. Show the product:
   create a checkpoint and review the resulting risk posture
3. Explain the backend:
   controller -> request DTO -> service -> scoring engine -> response DTO
4. Explain one interesting part:
   the weighted release risk model and recommendation generation
5. Close with tradeoffs:
   in-memory today, persistence and event integrations next

## Submission Notes

For the assessment, make sure the recruiter receives:

- the public GitHub repository link
- a 5 to 10 minute walkthrough video
- a short upload note containing your name and links

## License

This project is provided as a portfolio and interview demonstration project.
