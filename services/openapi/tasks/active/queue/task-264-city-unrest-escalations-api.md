# Task ID: API-TASK-264
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 00:25
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-260 (stock-exchange management API), API-TASK-241 (world interaction suite API)

---

## 📋 Краткое описание

Поставить полную спецификацию для управления эскалациями `City Unrest`: уровни беспорядков, сценарии, триггеры, экономические последствия и телеметрию.

**Что нужно сделать:** Создать OpenAPI файл `city-unrest-escalations.yaml`, описывающий REST и события для world-service (ядро), economy-service (налоги, расходы), social-service (рассылки) и narrative-service (кат-сцены).

---

## 🎯 Цель задания

Обеспечить управляемый цикл беспорядков в мире:
- Отслеживание уровня `city.unrest.level` и автоматический запуск сценариев
- Публикация/управление сценариями (`street-protest`, `logistics-sabotage`, `neon-riot`, `blackwall-breach`)
- Снятие/применение экономических модификаторов и социальных эффектов
- Интеграция с UI Crisis Hub и World Interaction Suite
- Поддержка телеметрии и SLA (KPIs, latency, PagerDuty)

---

## 📚 Источники информации

### Основной документ
- `.BRAIN/02-gameplay/world/city-unrest-escalations.md` (v1.0.0, готов к API)

**Ключевые элементы:**
- Таблицы уровней беспорядков, сценариев и фаз (`neon-riot`)
- Триггеры `specter.overlay.alertLevel`, `helios.alert`, результаты рейдов
- Экономические/социальные последствия для каждого уровня
- API карта (world/economy/social/narrative), телеметрия и KPI

### Дополнительные документы
- `.BRAIN/02-gameplay/world/economy-specter-helios-balance.md` — связка модификаторов
- `.BRAIN/02-gameplay/world/helios-countermesh-ops.md` — источники роста unrest
- `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-world-interaction-ui.md` — требования UI

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/gameplay/world/city-unrest.yaml`  
**Версия:** v1  
**Формат:** OpenAPI 3.0.3 (≤400 строк)

```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── world/
                └── city-unrest.yaml
```

Если файл создан ранее — обновить до v1.1.0, сохранив обратную совместимость.

---

## 🏗️ Целевая архитектура (⚠️)

### Backend
- **Микросервис:** world-service (ядро эскалаций)
- **Порт:** 8086
- **Base path:** `/api/v1/world/city-unrest/*`
- **Партнёры:**
  - economy-service (налоги, транспортные расходы)
  - social-service (оповещения, репутации)
  - narrative-service (кат-сцены/ветки)
  - analytics-service (телеметрия)

### Frontend
- **Модуль:** `modules/world/crisis-hub`
- **State Store:** `useWorldStore` (`cityUnrestState`, `activeScenarios`, `rewards`, `telemetry`)
- **UI:** `UnrestGauge`, `ScenarioCard`, `CrisisTimeline`, `RewardBreakdown`
- **Forms:** `ScenarioTriggerForm` (админ), `SupportActionForm` (social)
- **Hooks:** `useRealtime`, `useScenarioPlayback`, `useWorldAnnouncement`

### Gateway маршрут
```yaml
- id: world-service
  uri: lb://WORLD-SERVICE
  predicates:
    - Path=/api/v1/world/**
```

### Event bus
- `CITY_UNREST_LEVEL_CHANGED`, `CITY_UNREST_SCENARIO_STARTED`, `CITY_UNREST_SCENARIO_RESOLVED`, `CITY_UNREST_REWARD_APPLIED`

---

## 🧩 План выполнения

1. Описать модель состояния (`city.unrest.level`, thresholds, timers).
2. Реализовать CRUD сценариев и их расписаний (админ операции).
3. Добавить эндпоинты триггеров/результатов (`/scenario/trigger`, `/scenario/complete`).
4. Связать экономические модификаторы (`transport_surcharge`, `market_tax`).
5. Описать взаимодействие с social/narrative сервисами.
6. Добавить телеметрию и KPI в спецификации.
7. Описать WebSocket обновления для UI.
8. Прописать ошибки (conflict при параллельных сценариях, rate limits).
9. Пройти чеклист (Target Architecture, shared responses, примеры).

---

## 🧪 API Endpoints

- `GET /state` — текущий уровень, активные модификаторы, таймеры.
- `POST /update` — изменение уровня (source, delta, reason).
- `GET /scenarios` / `POST /scenarios` — управление сценариями и расписанием.
- `POST /scenarios/{scenarioId}/trigger` — запуск события (с проверками).
- `POST /scenarios/{scenarioId}/complete` — завершение, награды, последствия.
- `GET /history` — журнал изменений unrest (пагинация, фильтры).
- `POST /effects/economy` — применение экономических модификаторов.
- `POST /effects/social` — рассылка уведомлений, репутации.
- `POST /effects/narrative` — запуск кат-сцен.
- `GET /telemetry` — KPI, SLA, активные алерты.
- WebSocket `/ws/world/city-unrest` — realtime обновления.

Ошибки: использовать `shared/common/responses.yaml` (400/401/403/404/409/422/500).

---

## 🗄️ Схемы

- **CityUnrestState**, **Scenario**, **ScenarioSchedule**, **ScenarioTriggerRequest**, **ScenarioOutcome**, **EconomicEffect**, **SocialBroadcast**, **NarrativeBranch**.
- **UnrestHistoryEntry** — timestamp, source, delta, level, scenarioId.
- **TelemetrySnapshot** — metrics, thresholds, alertStatus.

---

## 🔄 Интеграции

- economy-service (`POST /economy/city-unrest/apply`)
- social-service (`POST /social/city-unrest/broadcast`)
- narrative-service (`POST /narrative/city-unrest/branch`)
- analytics (`POST /analytics/city-unrest/event`)

---

## 📊 Observability

- Метрики: `city_unrest_level`, `scenario_active_total`, `unrest_duration`, `response_rate`.
- Алерты: `CityUnrestQueueLag`, `ScenarioTimeout`, `EconomyModifierStale`.
- Трейсы: `unrest-trigger`, `unrest-resolve`, `unrest-economy-apply`.

---

## ✅ Критерии приемки

1. Префикс `/api/v1/world/city-unrest` соблюдён.
2. Target Architecture указан в комментарии шапки.
3. Поддержана идемпотентность `POST /update` (Idempotency-Key).
4. Сценарии проверяют конфликты (одно активное событие на район).
5. Экономические эффекты возвращают детализацию налогов/стоимости.
6. Social/narrative вызовы описаны с payload и ссылками.
7. История поддерживает фильтры по `source`, `scenario`, `level`.
8. WebSocket payload включает `eventType`, `level`, `scenario`, `effects`.
9. Телеметрия содержит KPI и SLA из документа.
10. FAQ покрывает edge cases (обratный ход, cancel, manual override).

---

## ❓ FAQ

- **Как отменить сценарий?** Использовать `POST /scenarios/{id}/cancel` (описать 409, audit).
- **Что если уровень упал ниже порога во время сценария?** Задокументировать `auto_complete = false`, требуется ручное завершение/перевод.
- **Как обрабатывать одновременный рост от разных источников?** Покрыть `update` с batch payload и приоритетами.
- **Как синхронизировать с UI?** Через WebSocket и события `CITY_UNREST_UPDATE`.
- **Как ограничить спам триггеров?** Rate limit + cooldown, описать 429.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

