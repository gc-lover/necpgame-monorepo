# Task ID: API-TASK-241
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 16:53
**Создатель:** AI Agent (Brain Manager)
**Зависимости:** [API-TASK-173]

---

## 📋 Краткое описание

Создать набор REST и WebSocket спецификаций world-service для игровых интерфейсов: World Pulse, Events Dashboard, Influence Map, Crisis Control Hub и Guild Operations (приказы).

**Что нужно сделать:** На основе `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-world-interaction-ui.md` спроектировать OpenAPI/AsyncAPI-описания мировых эндпоинтов и realtime-событий.

---

## 🎯 Цель задания

Обеспечить API слой для live-сервиса мира: публикация состояния мира, событий, территорий, приказов гильдий и кризисных действий.

**Зачем это нужно:**
- Поддержка UI World Pulse, Events Dashboard, Influence Map, Crisis Hub.
- Синхронизация клиентских приложений через WebSocket события.
- Единый контракт для world-service (порт 8086) и фронтенд модуля `modules/world`.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`
**Путь к документу:** `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-world-interaction-ui.md`
**Версия документа:** 1.0.0
**Дата последнего обновления:** 2025-11-07 16:53
**Статус документа:** approved / api-readiness: ready

**Что важно:**
- Разделы 2, 3, 5, 6, 7, 12, 13 — интерфейсы, механики, UX-флоу, API таблицы, SLA.
- Список WS событий и SLA (таблицы в секции 6 и 7).
- ASCII мокапы (секция 12) для контекстных мотивов UI.

### Дополнительные источники
- `.BRAIN/05-technical/global-state/global-state-operations.md`
- `.BRAIN/02-gameplay/world/world-state/player-impact-mechanics.md`
- `.BRAIN/02-gameplay/world/world-state/player-impact-systems.md`
- `.BRAIN/05-technical/ui/main-game/ui-features.md`
- Существующие спецификации: `api/v1/world/events.yaml`, `api/v1/world/state.yaml` (проверить и расширить/разделить при необходимости).

### Связанные документы
- `.BRAIN/06-tasks/active/CURRENT-WORK/active/backend-audit-compact.md` — ограничения world-service.
- `.BRAIN/02-gameplay/economy/economy-logistics.md` (для гильдейских приказов логистики).

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/world/world-interaction-suite.yaml`
**API версия:** v1
**Тип файла:** OpenAPI 3.0.3 + ссылки на AsyncAPI события (можно оформить в одном файле с компонентом `channels`).

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── world/
            ├── world-interaction-suite.yaml  ← основной файл
            ├── channels/
            │   └── world-interaction-events.yaml  ← при необходимости вынести события
            └── schemas/
                └── world-interaction.yaml        ← общие схемы состояний
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)
- **Микросервис:** world-service
- **Порт:** 8086
- **API пути:** `/api/v1/world/*`
- **Ответственность:** состояние мира, события, территории, кризисы, приказы гильдий.

### Frontend (модули)
- **Модуль:** `modules/world`
- **State Store:** `useWorldStore`, `useGuildOpsStore`
- **UI компоненты:** `WorldStatusCard`, `LiveEventTicker`, `CyberpunkHeatmap`, `OrderTimeline`, `CrisisActionPanel`

---

## 📜 Требуемые эндпоинты и события

### REST (обязательные)
1. `GET /api/v1/world/state`
   - Возвращает текущие показатели (stabilityIndex, modifier, activeEvents, trend, updatedAt).
2. `GET /api/v1/world/events`
   - Параметры: `page`, `size`, `regionId`, `factionId`, `category`, `state`.
3. `GET /api/v1/world/events/{eventId}` и `GET /api/v1/world/events/{eventId}/history`
   - Детали события и историю влияния.
4. `GET /api/v1/world/territories`
   - Параметры: `layer` (control, population, economy, security, events), `tileId` (массив).
5. `GET /api/v1/world/territories/{territoryId}`
   - Информация о конкретной территории, бонусы, pending events.
6. `GET /api/v1/world/orders`
   - Фильтры: `status`, `territoryId`, `role`, `page`, `size`.
7. `POST /api/v1/world/orders`
   - Создание приказа (leader). Idempotency key обязателен.
8. `POST /api/v1/world/orders/{orderId}/approve`
   - Подтверждение со стороны ролей (`strategist`, `logistics`).
9. `POST /api/v1/world/orders/{orderId}/complete`
   - Завершение приказа и фиксация KPI.
10. `GET /api/v1/world/crisis`
    - Текущая фаза, активные действия, ETA.
11. `POST /api/v1/world/crisis/actions`
    - Запуск кризисного действия (двойное подтверждение).
12. `POST /api/v1/world/crisis/actions/{actionId}/resolve`
    - Фиксация результата, эффект.

### WebSocket / SSE события
- `WORLD_STATE_TICK`
- `WORLD_EVENT_UPDATE`
- `TERRITORY_CONTROL_CHANGED`
- `ORDER_STATUS_CHANGED`
- `CRISIS_STATE_UPDATE`

Для каждого события описать payload в разделе `components/messages` (payload → схемы из `components/schemas`).

---

## 📦 Модели данных (минимальный набор)
- `WorldState` — stabilityIndex, modifier, activeEvents[], alertLevel, trend, updatedAt.
- `WorldEvent` — id, type, regionId, factionId, difficulty, state, timers, rewards, objectives[].
- `EventHistoryEntry` — timestamp, participation, outcome, worldImpact.
- `TerritoryState` — territoryId, controllerFactionId, controlPercent, bonuses[], pendingEvents[].
- `GuildOrder` — id, title, description, territoryId, status, timeline[], approvals[], metrics.
- `CrisisState` — phase, eta, mitigationLevel, activeActions[].
- `CrisisAction` — id, type, status, startedAt, cooldownEndsAt, effects[].
- Общие: enums, pagination компоненты (reuse `api/v1/shared/pagination.yaml`).

---

## ✅ Acceptance Criteria (минимум 10)
1. Все REST эндпоинты описаны с параметрами, примерами запросов/ответов.
2. Используются общие компоненты (`shared/pagination.yaml`, `shared/responses.yaml`).
3. WebSocket события описаны в формате AsyncAPI или разделах `components/messages` + `x-realtime`.
4. Каждое событие содержит пример payload.
5. Указаны требования к безопасности (JWT, scopes `world:read`, `world:write`, `world:orders`, `world:crisis`).
6. Описаны коды ошибок (400, 401, 403, 404, 409, 422, 500) через общие `components`.
7. Присутствуют схемы для idempotency ключа (header `Idempotency-Key`).
8. Прописаны SLA/RateLimits в `description` или `x-sla` согласно секции 7 источника.
9. Документ содержит раздел `x-target-architecture` (микросервис, фронтенд модуль, UI компоненты).
10. Примеры запросов/ответов для минимум 70% эндпоинтов.
11. Финальный файл валидируется `spectral` / CI lint без ошибок.
12. Размер файла ≤ 400 строк (при необходимости вынести схемы/каналы в подфайлы).

---

## 🧩 FAQ / Примечания
- Гильдейские приказы требуют двух ролей (leader + strategist/logistics) — отразить в схемах approvals.
- Кризисные действия запускаются только при `alertLevel=crisis`; опишите проверку.
- Для `GET /world/territories` учесть ответ «tile matrix» (список объектов, не матрица).
- World Pulse fallback REST и realtime описать как взаимодополняющие (SLA).
- Подумать о кеш-контроле (`Cache-Control: max-age=5`) для snapshot эндпоинтов.
- Для WS событий указать требование подключаться через `/ws/world` с параметром `authToken`.

---

## 🔄 Checklist перед сдачей
- [ ] Проведён анализ исходного документа.
- [ ] Все эндпоинты и события описаны.
- [ ] Схемы вынесены в `components/schemas` или подпапки.
- [ ] Обновлены `api/v1/shared` ссылки при необходимости.
- [ ] Файл прошёл lint.
- [ ] Добавлено `x-target-architecture`.
- [ ] Документация ≤400 строк (или разбита на части).
- [ ] Обновлён `brain-mapping.yaml` после завершения.


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

