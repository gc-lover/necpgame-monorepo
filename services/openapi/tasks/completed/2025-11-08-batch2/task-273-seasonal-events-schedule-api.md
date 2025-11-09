# Task ID: API-TASK-273
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** completed
**Создано:** 2025-11-08 01:32
**Завершено:** 2025-11-08 23:55
**Исполнитель:** GPT-5 Codex (API Executor)
**Зависимости:** API-TASK-264 (city unrest API), API-TASK-270 (specter surge loot API)

## 📦 Результат

- Добавлены `seasonal-schedule.yaml`, `seasonal-schedule-components.yaml`, `seasonal-schedule-examples.yaml` (расписание, триггеры, модификаторы, WS поток, <400 строк каждый).
- Задокументированы интеграции world/economy/social/gameplay, KPI (`EventSchedulerLag`, participation/retention) и коды ошибок `BIZ_WORLD_EVENT_*`, `VAL_*`, `INT_*`.
- Обновлены `brain-mapping.yaml`, `.BRAIN/02-gameplay/world/seasonal-events-2020-2093.md`, `.BRAIN/06-tasks/config/implementation-tracker.yaml`.

---

## 📋 Краткое описание

Нужно описать календарь регулярных и уникальных событий 2020–2093, включая REST/WS интерфейсы для world-service, economy-service, social-service и gameplay-service.

**Что нужно сделать:** Создать OpenAPI файл `seasonal-events-schedule.yaml`, покрывающий расписание, триггеры, эффекты, награды и телеметрию.

---

## 🎯 Цель задания

Обеспечить:
- Извлечение и управление расписаниями событий (weekly, seasonal, unique)
- Триггеры и условия (war_meter, city_unrest, proxy war, research)
- Экономические и социальные эффекты (налоги, скидки, репутации, мемы)
- Интеграцию с Helios/Specter системами, глобальными исследованиями
- Телеметрию и KPI (participation rate, retention, impact summaries)

---

## 📚 Источники информации

- `.BRAIN/02-gameplay/world/seasonal-events-2020-2093.md` — регулярные/уникальные события, YAML расписания, API карта, телеметрия.
- Дополнительно:
  - `.BRAIN/02-gameplay/world/global-research-2020-2093.md`
  - `.BRAIN/02-gameplay/world/city-unrest-escalations.md`
  - `.BRAIN/02-gameplay/world/economy-specter-helios-balance.md`
  - `.BRAIN/02-gameplay/world/raids/specter-surge-loot.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/world/events/seasonal-schedule.yaml`  
**Микросервисы:** world-service (расписание), economy-service (модификаторы), social-service (broadcast), gameplay-service (activities)

---

## 🧩 Обязательные секции

1. `GET /api/v1/world/events/schedule` — полный календарь с фильтрами (frequency, season, year).
2. `GET /api/v1/world/events/schedule/current` — активные события (regular, seasonal, unique).
3. `POST /api/v1/world/events/{eventId}/trigger` — принудительный запуск (админ/GM).
4. `POST /api/v1/world/events/{eventId}/complete` — завершение, награды, последствия.
5. `POST /api/v1/economy/events/apply-modifier` — экономические эффекты (налоги, цены).
6. `POST /api/v1/social/events/broadcast` — уведомления, NightHub мемы.
7. `POST /api/v1/gameplay/events/register-activity` — участие игроков в активностях.
8. WebSocket `/ws/world/events/seasonal` — `EventTriggered`, `EventProgress`, `EventCompleted`, `ModifierApplied`, `MemeShared`.
9. Схемы: `EventDefinition`, `RecurringEvent`, `SeasonalEvent`, `UniqueEvent`, `TriggerPayload`, `OutcomeEffect`, `EconomicModifier`, `SocialBroadcast`, `TelemetrySnapshot`.
10. KPI & Observability: `event_participation_rate`, `event_retention`, `unique_event_completion`, PagerDuty `EventSchedulerLag`.

---

## ✅ Критерии приемки

1. Префикс `/api/v1/world/events` соблюдён.
2. Target Architecture (комментарий) описывает world/economy/social/gameplay + frontend `modules/world/events`.
3. Расписание поддерживает YAML модель из документа (recurring/seasonal/unique).
4. Триггеры учитывают external flags (`war_meter`, `city_unrest`, `research`).
5. Экономические модификаторы описываются и возвращают детали (discounts, surcharges).
6. Social broadcasts хранят мемы/пасхалки и целевые аудитории.
7. Telemetry события (`EVENT_TRIGGERED`, `EVENT_COMPLETED`, `EVENT_MEME_SHARED`) задокументированы.
8. API поддерживает timezone и localization (UTC stamps + translations).
9. Ошибки используют общие responses; 409 при конфликте расписаний.
10. FAQ: overlapping events, cancellation, emergency triggers, offline rewards.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

