# Task ID: API-TASK-316
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** completed  
**Создано:** 2025-11-08 09:51  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-314 (population), API-TASK-315 (schedules)

---

## 📋 Краткое описание

Спроектировать `District Infrastructure Monitoring API` для economy-service: мониторинг инфраструктуры и SLA загрузки по районам.  
**Файл:** `api/v1/economy/districts/infrastructure.yaml`

---

## 🎯 Цель задания

Экономический сервис должен:
- учитывать инфраструктуру города (жильё, сервисы, транспорт, нелегальные объекты);
- рассчитывать загрузку/потребление/дефицит, поддерживать SLA;
- принимать пересчёты из population pipeline и отдавать состояние world/narrative сервисам;
- уведомлять о перегрузках и блокировках (power, security, logistics);
- давать инспекционный API для оперативного реагирования.

---

## 📚 Источники

- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md`
- `.BRAIN/02-gameplay/economy/economy-infrastructure.md`
- `.BRAIN/02-gameplay/economy/economy-logistics.md`
- `.BRAIN/02-gameplay/world/world-state/player-impact-systems.md`
- `.BRAIN/05-technical/backend/maintenance/maintenance-mode-system.md`
- `.BRAIN/05-technical/backend/analytics/monitoring-sla.md`

---

## 🏗️ Целевая архитектура

### Backend
- **Микросервис:** economy-service (port 8085)
- **Events:** `economy.infrastructure.updated`, `economy.infrastructure.alert`, `economy.infrastructure.ticket`
- **Интеграции:** world-service (district definitions), social-service (NPC demand), gameplay-service (player structures), maintenance-service (downtime)

### Frontend
- **Модуль:** `modules/economy/infrastructure`
- **State Store:** `useEconomyStore(infrastructure)`
- **UI:** `InfrastructureDashboard`, `SlaGauge`, `AlertTimeline`, `StatusPill`, `Heatmap`
- **Forms:** `InfrastructureFilterForm`, `MitigationPlanForm`
- **Layouts:** `OperationsSplitView`, `GameLayout`
- **Hooks:** `useRealtime`, `useInfrastructureFilters`, `useDebounce`

Комментарий в YAML:
```
# Target Architecture:
# - Microservice: economy-service (8085)
# - Frontend Module: modules/economy/infrastructure
# - State: useEconomyStore(infrastructure)
# - UI: InfrastructureDashboard, SlaGauge, AlertTimeline, StatusPill, Heatmap
# - Forms: InfrastructureFilterForm, MitigationPlanForm
# - Layouts: OperationsSplitView, GameLayout
# - Hooks: useRealtime, useInfrastructureFilters, useDebounce
# - Events: economy.infrastructure.updated, economy.infrastructure.alert, economy.infrastructure.ticket
# - API Base: /api/v1/economy/districts/*
```

---

## ✅ План

1. Собрать требования: типы инфраструктуры, метрики SLA, зависимости из population/schedules.  
2. Схемы:
   - `DistrictInfrastructureState`
   - `InfrastructureAsset`
   - `SlaIndicator`
   - `InfrastructureAlert`
   - `MitigationAction`
   - `InfrastructureRecalcRequest`
   - `Ticket`
3. Эндпоинты:
   - `GET /infrastructure` — агрегированное состояние (фильтры по city, type, severity).
   - `GET /infrastructure/{districtId}` — детальное состояние, активы, потребление.
   - `POST /infrastructure/recalculate` — пересчёт (связано с population diff).
   - `POST /infrastructure/mitigate` — запуск плана (например, распределить нагрузку).
   - `GET /infrastructure/alerts` — текущие/исторические предупреждения (пагинация).
   - `GET /infrastructure/tickets/{ticketId}` — статус компенсирующих мероприятий.
4. Подключить общие ошибки и пагинацию.
5. Примеры: район Watson North, перегрузка power grid, mitigation через генератор.
6. Проверка: OpenAPI 3.0.3, линтер, вынести компоненты при необходимости.

---

## 🧱 Модели

- `DistrictInfrastructureState`
  - `districtId`, `cityId`, `timestamp`, `assets[]`, `slaIndicators[]`, `alerts[]`, `capacityUsage`, `maintenanceTickets[]`
- `InfrastructureAsset`
  - `assetId`, `type` (housing, transport, security, illegal, entertainment, industrial), `capacity`, `currentLoad`, `status`, `dependencies[]`
- `SlaIndicator`
  - `indicatorId`, `metric`, `value`, `target`, `breach`, `trend`, `unit`
- `InfrastructureAlert`
  - `alertId`, `type`, `severity`, `description`, `detectedAt`, `resolvedAt`, `relatedAssets[]`
- `MitigationAction`
  - `actionId`, `plan`, `resources`, `expectedEffect`, `duration`, `status`
- `InfrastructureRecalcRequest`
  - `districtIds[]`, `trigger`, `priority`, `eventContext`, `dryRun`
- `RecalcJob`
  - `jobId`, `status`, `progress`, `submittedAt`, `logs[]`
- `Ticket`
  - `ticketId`, `type`, `severity`, `status`, `openedAt`, `assignedTo`, `history[]`
- `InfrastructureDiff`
  - `districtId`, `baseline`, `current`, `changes[]`

---

## 📊 Критерии

1. Файл `api/v1/economy/districts/infrastructure.yaml` создан с минимум 6 эндпоинтами.
2. Комментарий об архитектуре присутствует.
3. Схемы `DistrictInfrastructureState`, `InfrastructureAsset`, `SlaIndicator`, `InfrastructureAlert`, `InfrastructureRecalcRequest`, `RecalcJob`, `Ticket` оформлены.
4. Пагинация и ошибки подключены через общие компоненты.
5. Примеры отражают перегрузку power grid и mitigation план.
6. Предусмотрены события и ticket-система.
7. Линтер проходит без ошибок; лимит строк соблюдён.
8. `brain-mapping.yaml` и `.BRAIN` документ обновлены.

---

## ❓ FAQ

- **Как учитывать нелегальные объекты?** — `InfrastructureAsset.type = illegal`, отдельные SLA и оповещения (можно скрывать от некоторых ролей).
- **Можно ли автоматизировать mitigation?** — Через `MitigationAction.actionId` и `auto:true`, возвращать план и статус.
- **Нужен экспорт CSV?** — Нет, REST достаточно; BI выгрузки реализует analytics-service.
- **Как реагировать на player housing?** — Принимаем `trigger=player-impact`, в `eventContext` передаются детали; API должен отдавать дифф.
- **Интеграция с maintenance?** — Использовать `Ticket` и связывать с maintenance-service (ticketId).


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

