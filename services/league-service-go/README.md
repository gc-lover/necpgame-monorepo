# League Service

League Service управляет системой лиг: сезоны, прогресс игроков, мета-прогрессия, зал славы, события.

## Issue

Related to: #44

## OpenAPI Specifications

- `proto/openapi/league-core-service.yaml` (272 строки) - Core league operations
- `proto/openapi/league-meta-progression-service.yaml` - Meta-progression
- `proto/openapi/league-hall-of-fame-service.yaml` - Hall of Fame
- `proto/openapi/league-events-service.yaml` - League events

## API Endpoints

### League Core

- `GET /api/v1/league/current` - Получить текущую активную лигу
- `GET /api/v1/league/{leagueId}` - Получить информацию о лиге
- `GET /api/v1/league/players/{playerId}/progress` - Получить прогресс игрока

## Database Tables

- `league_seasons` - Сезоны лиг
- `player_league_progress` - Прогресс игроков
- `meta_progression` - Мета-прогрессия
- `league_events` - События лиг
- `hall_of_fame` - Зал славы

## Build

```bash
# Generate API code
make generate-api

# Build
go build -o league-service .

# Run
./league-service

# Docker
docker build -t league-service:latest .
```

## Environment Variables

- `DATABASE_URL` - PostgreSQL connection string (default:
  `postgres://postgres:postgres@localhost:5432/necpgame?sslmode=disable`)
- `PORT` - Service port (default: `8093`)

## Architecture

- **SOLID compliant** - разделение на layers (handlers, service, repository)
- **Generated code** - API types/server сгенерированы через oapi-codegen
- **Health checks** - `/health` endpoint
- **Metrics** - `/metrics` endpoint (Prometheus)

