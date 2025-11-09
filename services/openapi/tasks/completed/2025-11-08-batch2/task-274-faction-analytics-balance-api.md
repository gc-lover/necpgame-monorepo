# Task ID: API-TASK-274
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** completed
**Создано:** 2025-11-08 01:40
**Завершено:** 2025-11-09 00:12
**Исполнитель:** GPT-5 Codex (API Executor)
**Зависимости:** API-TASK-266 (specter-helios balance API), API-TASK-272 (faction quest chains API)

## 📦 Результат

- Добавлены `faction-balance.yaml`, `faction-balance-components.yaml`, `faction-balance-examples.yaml` (метрики, авто-тюнинг, алерты, WebSocket, <400 строк).
- Описаны метрики, sandbox-режим, интеграции с world/economy/social, observability (`analytics_job_latency`, `autotune_actions_total`, `alerts_open_total`, PagerDuty `AnalyticsJobLag`).
- Обновлены `brain-mapping.yaml`, `.BRAIN/05-technical/analytics/faction-analytics-balance.md`, `.BRAIN/06-tasks/config/implementation-tracker.yaml`.

---

## 📋 Краткое описание

Нужно реализовать OpenAPI контракт `faction-analytics-balance.yaml` для analytics-service: метрики фракций, авто-тюнинг параметров, алерты и интеграции с world/economy/social сервисами.

**Что нужно сделать:** Создать REST/WS спецификацию для получения метрик, применения auto-tuning действий и публикации алертов, согласно документу `faction-analytics-balance.md`.

---

## 🎯 Цель задания

Организовать:
- Сбор и агрегацию ключевых метрик (`contractSuccessRate`, `raidClearTime`, `ecoAssetVelocity`, `legacyImpactScore`, `affinityGrowthRate`, `climateStabilityIndex`, `metanetComplianceRate`)
- Применение авто-тюнинга (корректировка требований, HP/урона, налогов, событий)
- Взаимодействие с world-service, economy-service, social-service
- Push алертов и предоставление данных для аналитических дашбордов

---

## 📚 Источники информации

- `.BRAIN/05-technical/analytics/faction-analytics-balance.md` — метрики, авто-тюнинг, REST/WS карта, SQL.
- Дополнительно:
  - `.BRAIN/02-gameplay/world/factions/faction-quest-chains.md`
  - `.BRAIN/02-gameplay/world/raids/specter-surge-loot.md`
  - `.BRAIN/02-gameplay/world/economy-specter-helios-balance.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/technical/analytics/faction-balance.yaml`  
**Микросервис:** analytics-service (8090)  
**Порт:** 8090 (REST + WebSocket)

---

## 🧩 Обязательные секции

1. `GET /api/v1/analytics/factions/metrics` — агрегированные метрики (фильтры по faction, metric, period).
2. `GET /api/v1/analytics/factions/metrics/{metricId}` — детализация (time buckets, metadata).
3. `POST /api/v1/analytics/factions/autotune` — применение изменений (payload с adjustments, источником).
4. `GET /api/v1/analytics/factions/alerts` — активные алерты и рекомендации.
5. `POST /api/v1/analytics/factions/alerts/ack` — подтверждение/закрытие алертов.
6. WebSocket `/ws/analytics/factions` — `MetricUpdate`, `AutotuneApplied`, `AlertRaised`, `AlertResolved`.
7. Схемы: `MetricSnapshot`, `MetricDetail`, `AutotuneRequest`, `AutotuneResult`, `Alert`, `AlertAck`, `TelemetrySnapshot`.
8. Интеграции: world-service (`/world/raids/{id}/balance`), economy-service (`/economy/factions/trade-modifiers`), social-service (`/social/factions/reputation`).
9. Observability: опиши метрики `analytics_job_latency`, `autotune_actions_total`, `alerts_open_total`; PagerDuty `AnalyticsJobLag`.
10. FAQ: конфликтующие autotune действия, rollback, rate limits, sandbox/test режим.

---

## ✅ Критерии приемки

1. Префикс `/api/v1/analytics/factions` соблюдён.
2. Target Architecture комментарий описывает analytics-service + frontend `modules/analytics/dashboard`.
3. Метрики возвращаются с единицами измерения и источником.
4. AutotuneRequest проверяет допустимые диапазоны и использовать optimistic locking.
5. Alerts содержат severity, recommendedAction, impactedSystems.
6. WebSocket payload включает metricId, newValue, actionId.
7. Телеметрия покрывает все ключевые метрики из документа.
8. Ошибки используют `shared/common/responses.yaml` + `422` для валидации.
9. Поддержан sandbox режим (описать query param / header).
10. FAQ охватывает откат изменений, bulk autotune, ручные правки админов.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

