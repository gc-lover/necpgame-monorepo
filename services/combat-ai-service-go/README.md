# Combat AI Service

Микросервис для управления AI врагов в NECPGAME.

## Основные возможности

- **AI Profiles** - профили AI врагов (Street, Tactical, Mythic, Raid)
- **Encounters** - управление встречами с врагами
- **Raids** - рейдовые сценарии и фазы боссов
- **Telemetry** - телеметрия AI для баланса

## Архитектура

- **OpenAPI спецификация**: `proto/openapi/combat-ai-service.yaml`
- **Code generation**: `oapi-codegen` (chi-server)
- **База данных**: PostgreSQL
- **Метрики**: Prometheus (`/metrics`)
- **Health check**: `/health`

## Разработка

### Генерация кода из OpenAPI

```bash
make generate-api
```

### Проверка OpenAPI спецификации

```bash
make verify-api
```

### Запуск локально

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_USER=postgres
export DB_PASSWORD=postgres
export DB_NAME=necpgame
export HTTP_ADDR=:8090

go run main.go
```

### Сборка

```bash
go build -o combat-ai-service .
```

### Docker сборка

```bash
docker build -t combat-ai-service:latest .
```

## API Endpoints

### Profiles

- `GET /api/v1/gameplay/combat/ai/profiles` - список профилей AI
- `GET /api/v1/gameplay/combat/ai/profiles/{id}` - профиль AI по ID

### Encounters

- `POST /api/v1/gameplay/combat/ai/encounter` - создать встречу
- `GET /api/v1/gameplay/combat/ai/encounter/{encounterId}` - получить встречу
- `POST /api/v1/gameplay/combat/ai/encounter/{encounterId}/start` - начать встречу
- `POST /api/v1/gameplay/combat/ai/encounter/{encounterId}/end` - завершить встречу

### Raids

- `POST /api/v1/gameplay/combat/raids/{raidId}/phase` - переход фазы рейда
- `GET /api/v1/gameplay/combat/raids/{raidId}/phases` - список фаз рейда

### Telemetry

- `POST /api/v1/gameplay/combat/ai/telemetry` - записать телеметрию AI

## База данных

### Миграции

Миграции находятся в `infrastructure/liquibase/migrations/`.

```sql
-- ai_profiles
-- encounters
-- raid_phases
-- ai_telemetry
```

## Метрики

- `combat_ai_http_requests_total` - общее количество HTTP запросов
- `combat_ai_http_request_duration_seconds` - длительность HTTP запросов
- `combat_ai_profiles_total` - общее количество профилей AI
- `combat_ai_encounters_active` - активные встречи
- `combat_ai_raids_active` - активные рейды
- `combat_ai_tick_duration_seconds` - длительность AI tick

## Интеграции

- **Combat Sessions Service** - боевые действия
- **Economy Service** - лут и награды
- **Analytics Service** - телеметрия и баланс
- **World Events Service** - мировые события
- **Quest Service** - триггеры квестов

## Event Bus

- `combat.ai.state` - состояние AI врага
- `raid.phase.started` - начало фазы рейда
- `raid.phase.completed` - завершение фазы рейда
- `raid.boss.defeated` - поражение босса
- `encounter.started` - начало встречи
- `encounter.completed` - завершение встречи

