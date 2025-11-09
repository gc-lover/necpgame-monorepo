# Task ID: API-TASK-309
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 03:18
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** [API-TASK-299], [API-TASK-302], [API-TASK-304], [API-TASK-305], [API-TASK-300]

---

## 📋 Краткое описание

Спроектировать OpenAPI/AsyncAPI спецификацию подсистемы валидации лодаутов для матчмейкинга (Combat Loadout Matchmaking Integration) в `matchmaking-service`: проверка соответствия ролям/режимам, расчёт рейтинга, синхронизация с событиями и рекомендациями.

**Что нужно сделать:** На основе `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` описать REST/Async контракты, которые матчмейкинг использует для запроса статуса лодаута, проверки требований профилей и обмена данными о матчах и ролях.

---

## 🎯 Цель задания

Гарантировать, что команды формируются с учётом готовности лодаутов: соответствие ролям, событиям, ограничениям и актуальным патчам, а также использовать данные о лодаутах для расчёта рейтингов.

**Зачем это нужно:**
- Проверять лодаут перед матчем/в очереди, блокируя неподходящие конфигурации.
- Подбирать оптимальные роли и профили по текущим событиям и угрозам.
- Сохранять телеметрию лодаутов для последующего анализа комфортности матчей.

---

## 📚 Источники информации

### Основной источник

**Репозиторий:** `.BRAIN`  
**Документ:** `.BRAIN/02-gameplay/combat/combat-loadouts-system.md`  
**Версия:** 0.3.0  
**Дата последнего обновления:** 2025-11-08 00:14  
**Статус документа:** review, `api-readiness: ready`

**Что важно:**
- Раздел «Интеграция с другими системами» — матчмейкинг использует лодауты для подбора команд и рейтинга.
- Разделы про профили, роли, режимы (`profiles`, `PvE экспедиции`, `категории лодаутов`).
- Раздел «Управление недоступными предметами» — необходимость блокировок и рекомендаций.
- Раздел «Метрики и телеметрия» — данные, которые нужно возвращать/снимать во время матчей.

### Дополнительные источники

- `.BRAIN/02-gameplay/combat/combat-roles-detailed.md`
- `.BRAIN/02-gameplay/combat/combat-extract.md`
- `.BRAIN/02-gameplay/world/events/world-events-framework.md`
- `.BRAIN/02-gameplay/progression/progression-skills-mapping.md`
- `.BRAIN/02-gameplay/progression/progression-attributes-matrix.md`
- `.BRAIN/02-gameplay/combat/arena-system.md`
- `.BRAIN/02-gameplay/combat/loot-hunt-system.md`

### Связанные документы/таски

- `API-SWAGGER/tasks/active/queue/task-299-combat-loadouts-api.md`
- `API-SWAGGER/tasks/active/queue/task-302-combat-loadout-profiles-api.md`
- `API-SWAGGER/tasks/active/queue/task-304-combat-loadout-availability-api.md`
- `API-SWAGGER/tasks/active/queue/task-305-combat-loadout-telemetry-api.md`
- `API-SWAGGER/tasks/active/queue/task-300-living-world-hybrid-api.md`
- `API-SWAGGER/api/v1/matchmaking/matchmaking.yaml` (для согласования)

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Целевой файл:** `api/v1/matchmaking/loadouts/loadout-matchmaking.yaml`  
**Формат:** OpenAPI 3.0.3 + AsyncAPI (при необходимости)

```
API-SWAGGER/
└── api/
    └── v1/
        └── matchmaking/
            └── loadouts/
                ├── loadout-matchmaking.yaml          ← создать
                ├── loadout-matchmaking-components.yaml
                └── loadout-matchmaking-events.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** matchmaking-service
- **Порт:** 8091
- **API Base:** `/api/v1/matchmaking/loadouts*`
- **Интеграции:** gameplay-service (loadouts), availability-service, profiles-service, analytics-service (telemetry), notification-service (alert игроков), living-world (события).
- **Очереди:** Kafka `matchmaking.loadouts.*`, подписки на `combat.loadouts.*`, `loadout.maintenance.*`, `loadout.availability.*`, `analytics.loadouts.*`.

### Frontend
- **Модуль:** `modules/matchmaking/loadout-readiness`
- **State Store:** `useMatchmakingLoadoutStore`
- **UI компоненты:** `QueueLoadoutStatus`, `RoleRequirementPanel`, `EventModifierBadge`, `PreMatchChecklist`, `ReplacementSuggestionModal`
- **Формы:** `MatchRequirementOverrideForm`, `FastReplacementForm`
- **Хуки:** `useLoadoutReadiness`, `useMatchmakingRecommendations`, `usePreMatchValidation`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: matchmaking-service (port 8091)
# - API Base: /api/v1/matchmaking/loadouts*
# - Dependencies: gameplay, availability, profiles, analytics, notification, living-world
# - Events: matchmaking.loadouts.*, consumes combat.loadouts.*, loadout.maintenance.*, loadout.availability.*, analytics.loadouts.*
# - Frontend Module: modules/matchmaking/loadout-readiness (useMatchmakingLoadoutStore)
# - UI: QueueLoadoutStatus, RoleRequirementPanel, EventModifierBadge, PreMatchChecklist, ReplacementSuggestionModal
# - Forms: MatchRequirementOverrideForm, FastReplacementForm
# - Hooks: useLoadoutReadiness, useMatchmakingRecommendations, usePreMatchValidation
```

---

## ✅ Что нужно сделать (детальный план)

1. Определить требования к валидаторам и рекомендациям из документа `.BRAIN`.
2. Спроектировать REST endpoints для проверки лодаута, получения требований профилей, расчёта рейтингов и замены игроков.
3. Описать схемы `LoadoutReadiness`, `RoleRequirement`, `MatchRequirement`, `EventModifier`, `ReplacementSuggestion`, `PreMatchChecklist`, `MatchLoadoutReport`.
4. Добавить endpoints для обратной связи матчмейкинга (успешность матчей, нарушения, рекомендации).
5. Спроектировать события (`matchmaking.loadout.ready`, `matchmaking.loadout.blocked`, `matchmaking.loadout.replacement`, `matchmaking.loadout.postmatch-report`) с payload и retry.
6. Прописать безопасность (scopes `matchmaking:loadouts.read/write`), idempotency, rate limits.
7. Подготовить примеры запросов/ответов/событий (pre-match validation, event-based adjustments, replacement workflow).
8. Указать интеграции с availability, profiles, telemetry, maintenance и notification.
9. Сформировать чеклист, критерии приёмки, FAQ, инструкции по обновлению mapping и `.BRAIN`.

---

## 🔀 Требуемые эндпоинты

1. `POST /api/v1/matchmaking/loadouts/validate` — синхронная проверка готовности (вход: loadoutId, matchContext, eventModifiers).
2. `GET /api/v1/matchmaking/loadouts/{loadoutId}/requirements` — требования по роли, профилю, событиям.
3. `GET /api/v1/matchmaking/loadouts/{loadoutId}/replacements` — предложения замены (fallback loadouts, комплектов, игроков).
4. `POST /api/v1/matchmaking/loadouts/{loadoutId}/reserve` — резервирование/блокировка лодаута для очереди.
5. `POST /api/v1/matchmaking/matches/{matchId}/loadouts/report` — отчёт о фактическом использовании (валидность, нарушения).
6. `GET /api/v1/matchmaking/loadouts/events` — актуальные модификаторы событий и их влияние.
7. `POST /api/v1/matchmaking/loadouts/override` — ручной override требований (для админов/организаторов).
8. `GET /api/v1/matchmaking/loadouts/metrics` — метрики очередей, блокировок, замен.
9. `POST /api/v1/matchmaking/loadouts/feedback` — обратная связь от игроков (проблемы, пожелания).
10. `GET /api/v1/matchmaking/loadouts/queue-status` — состояние лодаутов в очереди (для UI/аналитики).

---

## 🧱 Модели данных

- **LoadoutReadiness** — `loadoutId`, `status` (`READY`, `WARNING`, `BLOCKED`), `issues[]`, `recommendations[]`, `requiredActions`.
- **MatchRequirement** — `mode`, `eventCode`, `roleRequirements[]`, `minMasteryTier`, `restrictedItems[]`.
- **RoleRequirement** — `roleCode`, `requiredProfiles`, `allowedLoadouts`, `priority`.
- **EventModifier** — `eventCode`, `modifierType`, `impact`, `recommendedAdjustments`.
- **ReplacementSuggestion** — `suggestedLoadoutId`, `reason`, `impactScore`, `fallbackConfidence`.
- **PreMatchChecklist** — `items[]`, `completed`, `warnings`.
- **MatchLoadoutReport** — `matchId`, `loadoutId`, `issues`, `penalties`, `performanceMetrics`.
- **QueueStatus** — `queueId`, `loadoutId`, `position`, `estimatedWait`, `readiness`.
- **MatchmakingMetric** — `time`, `blockedPercent`, `replacementRate`, `avgResolutionTime`, `eventImpactScore`.
- **Async Events** — payloads `matchmaking.loadout.ready`, `matchmaking.loadout.blocked`, `matchmaking.loadout.replacement`, `matchmaking.loadout.postmatch-report`.

---

## 🧭 Принципы и правила

- Соблюдать OpenAPI 3.0.3 и AsyncAPI (вынос компонентов при необходимости).
- Использовать `$ref` на loadouts, profiles, availability, telemetry схемы.
- Идемпотентность: повторная валидация с теми же параметрами возвращает одинаковый результат; `reserve` запросы используют `Idempotency-Key`.
- События должны содержать `correlationId` (матч) и `causationId` (инициирующее событие).
- Обрабатывать ошибки `409`, `423`, `451`, `460 LOADOUT_EVENT_BLOCK`.
- Документировать SLA: макс время ответа < 150мс; fallback маршруты для деградированных режимов.

---

## ✅ Критерии приемки

1. Все эндпоинты описаны с параметрами, схемами, примерами.
2. Поддержка событий (pre-match, replacement, postmatch) документирована.
3. Интеграции с availability, profiles, telemetry, living-world отражены.
4. Метрики и отчёты для аналитики матчмейкинга присутствуют.
5. Security, idempotency, rate limits описаны.
6. Checklist и FAQ заполнены; указаны шаги обновления mapping и `.BRAIN`.

---

## 📎 Checklist перед сдачей

- [ ] Проверить, что все секции шаблона заполнены.
- [ ] Провести lint OpenAPI/AsyncAPI, вынести общие компоненты.
- [ ] Примеры покрывают проверки разных режимов и замен.
- [ ] События согласованы с matchmaking, notification и analytics шинами.
- [ ] Инструкции по обновлению mapping и `.BRAIN` подготовлены.

---

## ❓ FAQ

**Q:** Что делать, если лодаут блокирован из-за события?  
**A:** Endpoint `/validate` вернёт `status: BLOCKED` и список `requiredActions`. Можно вызвать `/replacements` для быстрого предложения альтернатив.

**Q:** Как учитывать гибридные роли?  
**A:** Профили из `loadout-profiles` включают гибридные схемы. Возвращать `roleRequirements` с приоритетами и конфликтами.

**Q:** Возможно ли ручное подтверждение администратором?  
**A:** Endpoint `/override` позволяет временно разрешить участие с записью аудита и события `matchmaking.loadout.ready` с флагом `overridden`.

---

## 🔗 Связность и последующие шаги

- Добавить запись в `tasks/config/brain-mapping.yaml`, обновить `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` (API-TASK-309).
- Согласовать спецификацию с существующими matchmaking API.
- После подготовки спецификации инициировать задачи по интеграции в backend и UI очередей.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

