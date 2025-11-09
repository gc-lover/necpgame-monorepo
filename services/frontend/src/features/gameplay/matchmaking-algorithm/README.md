# Matchmaking Algorithm Feature
Оперативный центр матчмейкинга: очереди, тикеты, ready-check, качество и телеметрия.

**OpenAPI:** matchmaking/matchmaking-algorithm.yaml | **Роут:** /gameplay/matchmaking

## UI
- `MatchmakingAlgorithmPage` — SPA (380 / flex / 320), фильтры режима/региона, auto requeue toggle
- Компоненты:
  - `QueueStatusCard`
  - `MatchTicketCard`
  - `ReadyCheckCard`
  - `QualityMetricsCard`
  - `TelemetryCard`
  - `AnalyticsCard`

## Возможности
- Мониторинг очередей и SLA (population, wait, ready-check)
- Просмотр активных тикетов и латентности
- Ready-check таймеры и распределение подтверждений
- Контроль KPI качества (skill gap, latency spread)
- Телеметрия латентности по перцентилям
- Ежедневная аналитика матчей, отмен и dodge
- Компактный киберпанк интерфейс на одном экране

## Тесты
- Юнит-тесты для карточек в `components/__tests__`
- Написаны, **не запускались**


