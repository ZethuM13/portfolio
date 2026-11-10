# ServicePlus Architecture

## Target architecture

ServicePlus will use a modular architecture with clear boundaries between presentation, domain logic, persistence and analytics.

```text
React and TypeScript client
        |
Spring Boot REST API
        |
PostgreSQL database ---- Audit history
        |
Python trend-analysis service
```

## Current runnable slice

The first milestone uses Java 17's built-in HTTP server and an in-memory repository. It preserves the intended API boundary and domain model while avoiding unverified external dependencies in the initial workspace.

```text
HTML, CSS and JavaScript client
        |
Java HTTP API
        |
In-memory service repository
```

## Planned modules

- Identity: authentication, users and roles.
- Catalogue: services, owners, dependencies and lifecycle state.
- Incidents: intake, assignment, workflow and activity history.
- SLA: policies, deadlines, escalation and breach calculation.
- Health: service scoring and availability summaries.
- Intelligence: recurring-pattern detection and trend reporting.
- Audit: immutable record of important actions.

## Core entities

- User
- Role
- Service
- ServiceDependency
- Incident
- IncidentActivity
- SlaPolicy
- HealthSnapshot
- AuditEvent
