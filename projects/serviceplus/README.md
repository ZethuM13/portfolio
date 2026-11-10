# ServicePlus

ServicePlus is an intelligent application health and incident-management platform for technology support teams, application owners and operational managers.

This repository begins with a runnable vertical slice of the Service Catalogue. It demonstrates the product direction without external dependencies while the production architecture is prepared for React, Spring Boot and PostgreSQL.

## Current milestone

- Executive dashboard with live service counts
- Searchable service catalogue
- Health and lifecycle status indicators
- Add-service workflow with validation
- Java REST API with in-memory persistence
- Responsive, accessible browser interface

## Run locally

### Windows

Read [START_HERE.md](START_HERE.md), then run:

```powershell
scripts\START_SERVICEPLUS.bat
```

### Linux or macOS

```bash
./scripts/run.sh
```

Open `http://localhost:8080`.

Run the smoke tests in a second terminal:

```bash
./scripts/test.sh
```

## Production direction

The verified prototype will be migrated in stages to:

- React and TypeScript frontend
- Spring Boot REST API
- PostgreSQL persistence
- JWT authentication and role-based access control
- Python trend-analysis service
- Docker, GitHub Actions and cloud deployment

See [requirements](docs/requirements.md) and [architecture](docs/architecture.md).
