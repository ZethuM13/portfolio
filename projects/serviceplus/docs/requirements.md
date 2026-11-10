# ServicePlus MVP Requirements

## Product objective

ServicePlus gives support teams one place to understand which business applications exist, who owns them, whether they are healthy and which incidents require attention.

## Primary users

- Support analyst: records incidents, updates progress and monitors SLA deadlines.
- Application owner: maintains service information and reviews recurring failures.
- Operations manager: monitors service health, workload, SLA performance and risk.
- Administrator: manages access, roles and platform configuration.

## MVP capabilities

### Service catalogue

- Register a service with a name, owner, business domain, criticality and health status.
- View and search all registered services.
- Distinguish healthy, degraded and unavailable services visually.
- Show ownership and business criticality clearly.

### Incident management

- Record an incident against a registered service.
- Assign severity, owner and current status.
- Track acknowledgement and resolution timestamps.
- Preserve a chronological activity history.

### SLA monitoring

- Calculate response and resolution deadlines from incident severity.
- Show approaching and breached deadlines.
- Escalate overdue incidents to the appropriate role.

### Intelligence and reporting

- Calculate an application health score.
- Identify recurring incident categories and affected services.
- Show management metrics and trend summaries.

### Security and quality

- Authenticate users and enforce role-based permissions.
- Validate all client and server inputs.
- Record changes in an audit log.
- Provide automated API, domain and interface tests.

## First vertical slice acceptance criteria

1. A user can open the dashboard and see the current service totals.
2. A user can search the service catalogue by name, owner or business domain.
3. A user can register a valid service and see it immediately in the catalogue.
4. Invalid or incomplete submissions show a clear message and are not stored.
5. The API returns valid JSON and appropriate HTTP status codes.
6. The interface remains usable on desktop and mobile screens.
