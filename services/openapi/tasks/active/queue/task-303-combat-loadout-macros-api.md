# Task ID: API-TASK-303
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 01:32
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** [API-TASK-299], [API-TASK-301], [API-TASK-302], [API-TASK-038]

---

## 📋 Краткое описание

Спроектировать OpenAPI/AsyncAPI спецификацию подсистемы боевых макрокоманд (Loadout Macros) для `gameplay-service`: редактор, симуляция, выполнение, ограничения UX и аудит.

**Что нужно сделать:** На основе `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` описать REST/Async контракты для CRUD макрокоманд, прогонов в симуляторе, привязки к лодаутам, контроля конфликтов с навыками и синхронизации с UI/аналитикой.

---

## 🎯 Цель задания

Обеспечить игроков мощным, но контролируемым инструментом автоматизации действий в бою, сохраняя баланс, безопасность и отслеживая влияние макросов на геймплей.

**Зачем это нужно:**
- Упростить выполнение сложных комбинаций способностей и гаджетов.
- Гарантировать, что макросы не нарушают UX и ограничения управления.
- Синхронизировать макросы между устройствами, отрядами и аналитикой для балансировки.

---

## 📚 Источники информации

### Основной источник

**Репозиторий:** `.BRAIN`  
**Документ:** `.BRAIN/02-gameplay/combat/combat-loadouts-system.md`  
**Версия:** 0.3.0  
**Дата последнего обновления:** 2025-11-08 00:14  
**Статус документа:** review, `api-readiness: ready`

**Что важно:**
- Блок «Макрокоманды и UX ограничители» — типы макросов (`sequenced`, `conditional`, `sync`), лимиты `maxSteps=8`, валидация конфликтов управления.
- Таблица доменных сущностей — `LoadoutMacro`, `LoadoutMacro.sequence`.
- API черновик — `POST /api/v1/gameplay/loadouts/{id}/macros/preview`, события `combat.loadouts` при активации.
- Описание PvE экспедиций, макрофункций и автоматизации в разделах про роли и экспедиции.
- Информация об аудите и ограничениях в секциях «Управление лодаутами» и «Очереди обновлений».

### Дополнительные источники

- `.BRAIN/02-gameplay/combat/combat-abilities.md` — способности и ограничения кулдаунов.
- `.BRAIN/02-gameplay/combat/combat-roles-detailed.md` — роли и разрешённые комбинации.
- `.BRAIN/02-gameplay/combat/cyberpsychosis-system.md` — риски злоупотреблений (перегрузка имплантов).
- `.BRAIN/02-gameplay/combat/arena-system.md` — требования арен к автоматизации.
- `.BRAIN/02-gameplay/combat/loot-hunt-system.md` — макросы в экстракционном цикле.
- `.BRAIN/02-gameplay/economy/blueprint-market.md` — обмен макросами через blueprint (при необходимости).

### Связанные документы/таски

- `API-SWAGGER/tasks/active/queue/task-299-combat-loadouts-api.md`
- `API-SWAGGER/tasks/active/queue/task-301-combat-loadout-kits-api.md`
- `API-SWAGGER/tasks/active/queue/task-302-combat-loadout-profiles-api.md`
- `API-SWAGGER/tasks/active/queue/task-038-combat-abilities-api.md`

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Целевой файл:** `api/v1/gameplay/combat/loadout-macros.yaml`  
**Формат:** OpenAPI 3.0.3 (вспомогательные файлы при необходимости)

```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── combat/
                ├── loadouts.yaml
                ├── loadout-kits.yaml
                ├── loadout-profiles.yaml
                ├── loadout-macros.yaml             ← создать
                ├── loadout-macros-components.yaml  ← вынести схемы при >400 строк
                └── loadout-macros-events.yaml      ← вынести AsyncAPI при необходимости
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** gameplay-service
- **Порт:** 8083
- **API Base:** `/api/v1/gameplay/combat/loadout-macros*`
- **Зависимости:** abilities-service (кулдауны и конфликты), inventory-service (гаджеты и расходники), realtime-service (live исполнение), notification-service (оповещения), analytics-service (метрики макросов), auth-service (scopes `loadouts:macros.*`).
- **Системы очередей:** Redis Streams / Kafka темы `combat.loadouts.macros.*` для прогонов и изменений.

### Frontend
- **Модуль:** `modules/combat/loadouts/macros`
- **State Store:** `useLoadoutMacrosStore` (macros, simulations, conflicts)
- **UI компоненты:** `MacroEditor`, `MacroStepList`, `ConflictWarningBadge`, `MacroPreviewTimeline`, `MacroLibraryPanel`, `MacroUsageHeatmap`, `MacroAuditLog`
- **Формы:** `MacroDefinitionForm`, `MacroConflictResolutionForm`, `MacroShareForm`
- **Хуки:** `useMacroSimulator`, `useMacroConflicts`, `useMacroLibrary`, `useMacroAnalytics`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: gameplay-service (port 8083)
# - API Base: /api/v1/gameplay/combat/loadout-macros*
# - Dependencies: abilities, inventory, realtime, notification, analytics, auth
# - Frontend Module: modules/combat/loadouts/macros (useLoadoutMacrosStore)
# - UI: MacroEditor, MacroStepList, ConflictWarningBadge, MacroPreviewTimeline, MacroLibraryPanel
# - Forms: MacroDefinitionForm, MacroConflictResolutionForm, MacroShareForm
# - Hooks: useMacroSimulator, useMacroConflicts, useMacroLibrary, useMacroAnalytics
```

---

## ✅ Что нужно сделать (детальный план)

1. Выделить из документа требования к макрокомандам, типам, лимитам и конфликтам управления.
2. Спроектировать REST endpoints для CRUD макросов, версионирования, привязки к лодаутам, симуляции, публикации и обмена внутри аккаунта/отряда.
3. Описать схемы `LoadoutMacro`, `MacroStep`, `MacroTrigger`, `MacroConstraint`, `MacroVersion`, `MacroSimulationResult`, `MacroConflict`, `MacroShareToken`, `MacroAuditEntry`.
4. Добавить endpoints для симуляции (`preview`), авто-починки конфликтов, экспорта/импорта макросов (в рамках аккаунта), публикации в библиотеку.
5. Смоделировать асинхронные события (`loadout.macro.created`, `loadout.macro.updated`, `loadout.macro.executed`, `loadout.macro.conflict-detected`, `loadout.macro.shared`) с payload и retry.
6. Прописать безопасность, idempotency, аудит, лимиты (`maxSteps=8`, cooldown, запрещённые действия), связь с ролями и профилями.
7. Подготовить примеры запросов/ответов/событий (создание, симуляция, конфликт, публикация, отзыв).
8. Описать интеграцию с loadouts (`API-TASK-299`), kits (`API-TASK-301`), profiles (`API-TASK-302`) и abilities (`API-TASK-038`) через `$ref`.
9. Сформировать чеклист, критерии приёмки, FAQ, инструкции по обновлению mapping и `.BRAIN`.

---

## 🔀 Требуемые эндпоинты

1. `GET /api/v1/gameplay/combat/loadout-macros` — список макросов игрока/сквада (фильтры по типу, роли, статусу).
2. `POST /api/v1/gameplay/combat/loadout-macros` — создание макроса с шагами, триггерами и ограничениями.
3. `GET /api/v1/gameplay/combat/loadout-macros/{macroId}` — подробности макроса, история версий, связанные лодауты.
4. `PATCH /api/v1/gameplay/combat/loadout-macros/{macroId}` — обновление шагов, триггеров, ограничений, привязок.
5. `DELETE /api/v1/gameplay/combat/loadout-macros/{macroId}` — архивирование/удаление с возможностью отката.
6. `POST /api/v1/gameplay/combat/loadout-macros/{macroId}/preview` — симуляция макроса (возвращает таймлайн, конфликты, потребление ресурсов).
7. `POST /api/v1/gameplay/combat/loadout-macros/{macroId}/bind` — привязка макроса к лодауту/набору слотов.
8. `DELETE /api/v1/gameplay/combat/loadout-macros/{macroId}/bind/{loadoutId}` — снятие привязки.
9. `POST /api/v1/gameplay/combat/loadout-macros/{macroId}/share` — выдача share-token для сквада/гильдии (TTL, permissions).
10. `POST /api/v1/gameplay/combat/loadout-macros/share/import` — импорт макроса по токену (валидация ролей, конфликтов).
11. `GET /api/v1/gameplay/combat/loadout-macros/conflicts` — коллекция конфликтов (hotkeys, cooldowns, человечность) с действиями по исправлению.
12. `POST /api/v1/gameplay/combat/loadout-macros/{macroId}/resolve` — полуавтоматическое устранение конфликта (предлагает альтернативные шаги).
13. `GET /api/v1/gameplay/combat/loadout-macros/audit` — аудит изменений и выполнения (связь с `X-Audit-Id`).
14. `GET /api/v1/gameplay/combat/loadout-macros/metrics` — агрегированные метрики использования макросов, успешность, конфликтность.

Все мутационные операции требуют заголовков `Authorization`, `Idempotency-Key`, `X-Audit-Id`, а ответы должны ссылаться на общие компоненты (`shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`).

---

## 🧱 Модели данных

- **LoadoutMacro** — `macroId`, `ownerId`, `name`, `description`, `type` (`SEQUENCED`, `CONDITIONAL`, `SYNC`), `roleTags[]`, `steps[]`, `triggers[]`, `constraints`, `boundLoadouts[]`, `status`, `version`, `createdAt`, `updatedAt`.
- **MacroStep** — `stepId`, `order`, `actionType` (`ABILITY`, `ITEM`, `ABILITY_GROUP`, `WAIT`, `CUSTOM`), `abilityId`, `itemId`, `duration`, `conditions`, `cooldownImpact`, `resourceCost`.
- **MacroTrigger** — `triggerId`, `triggerType` (`MANUAL`, `EVENT`, `HEALTH_THRESHOLD`, `TEAM_SIGNAL`), `params`.
- **MacroConstraint** — `maxSteps`, `maxDuration`, `allowedAbilities[]`, `forbiddenAbilities[]`, `requiredImplants[]`, `cooldownBuffer`, `inputLock`.
- **MacroVersion** — `version`, `changelog`, `diff`, `approvedBy`, `approvedAt`, `rollbackTo`.
- **MacroSimulationResult** — `timeline[]`, `resourceConsumption`, `conflicts[]`, `successProbability`, `estimatedDps`.
- **MacroConflict** — `conflictId`, `macroId`, `type` (`COOLDOWN`, `INPUT`, `HUMANITY`, `ABILITY_LOCK`, `ARENA_RULE`), `severity`, `description`, `affectedSteps[]`, `suggestedFix`.
- **MacroShareToken** — `tokenId`, `macroId`, `issuedBy`, `scope` (`ACCOUNT`, `SQUAD`, `CLAN`), `permissions`, `ttl`, `usageLimit`, `status`.
- **MacroAuditEntry** — `entryId`, `macroId`, `action`, `initiator`, `context`, `result`, `timestamp`.
- **MacroMetric** — `macroId`, `usageCount`, `winRateDelta`, `conflictRate`, `averageExecutionTime`, `arenaBanRate`.
- **Async Events** — payloads для `loadout.macro.created`, `loadout.macro.updated`, `loadout.macro.executed`, `loadout.macro.conflict-detected`, `loadout.macro.shared`, `loadout.macro.revoked`.

---

## 🧭 Принципы и правила

- Соблюдать OpenAPI 3.0.3, лимит 400 строк — схемы и события выносить в подфайлы.
- Использовать `$ref` на общие компоненты и на контракты loadouts/kits/profiles/abilities.
- Лимиты: `maxSteps=8`, запрет на одновременные действия в одном кадре; описать ошибки `422 MACRO_STEP_INVALID`, `409 MACRO_CONFLICT`, `429 MACRO_RATE_LIMIT`.
- Макросы должны проходить валидацию конфликта способностей, имплантов, арен и прогрессии.
- Прописать аудит, idempotency, версионирование макросов (immutable versions + rollback).
- События публиковать в `combat.loadouts.macros.*` с `correlationId` и `causationId`.
- Учесть безопасность: scopes `loadouts:macros.read`, `loadouts:macros.write`, `loadouts:macros.share`, `loadouts:macros.execute`.

---

## ✅ Критерии приемки

1. Все 14 эндпоинтов описаны с параметрами, схемами, примерами (создание, симуляция, конфликты, шаринг).
2. Описаны состояния макросов (`draft`, `active`, `suspended`, `archived`) и workflow утверждения.
3. Макросы валидации учитывают cooldown, человечность, арену, ограничения ролей и профилей.
4. Симуляция возвращает таймлайн, конфликты, потребление ресурсов и рекомендации.
5. Обмен (share/import) покрывает валидацию, права доступа, TTL, аудит, события.
6. Асинхронные события перечислены с payload, каналами, retry-политикой.
7. Документированы ошибки `409`, `422`, `423`, `451` и требования к idempotency и `X-Audit-Id`.
8. Интеграции с нагрузкой/аналитикой (`analytics-service`) отражены через поля и ссылки.
9. Checklist и FAQ заполнены, указаны шаги обновления mapping и `.BRAIN`.

---

## 📎 Checklist перед сдачей

- [ ] Все разделы шаблона заполнены, ссылки на `.BRAIN` и связанные API корректны.
- [ ] OpenAPI и AsyncAPI проходят lint, длина файла ≤400 строк (или вынесены части).
- [ ] Примеры покрывают ключевые сценарии: создание, симуляция, конфликт, шаринг, импорт, отзыв.
- [ ] Асинхронные события синхронизированы с realtime/notification и analytics.
- [ ] Архитектурный комментарий присутствует и корректен.
- [ ] Инструкции по обновлению `brain-mapping.yaml` и `.BRAIN` подготовлены.

---

## ❓ FAQ

**Q:** Как предотвратить злоупотребления макросами на аренах?  
**A:** Проверки арен через `MacroConstraint` и события `loadout.macro.conflict-detected`. В случае запрета возвращать `423 MACRO_BLOCKED_ARENA_RULE`.

**Q:** Можно ли делиться макросами между аккаунтами?  
**A:** Только внутри одного аккаунта (share scope `ACCOUNT`) или через гильдейский канал с временным токеном. Публичный рынок запрещён; система создаёт share-token с TTL.

**Q:** Что происходит при конфликте с кулдауном?  
**A:** Симуляция/валидация возвращает `MacroConflict` с типом `COOLDOWN`, предлагает альтернативу или увеличенный `Wait` шаг. Событие `loadout.macro.conflict-detected` отправляется в аналитку.

---

## 🔗 Связность и последующие шаги

- Добавить задачу в `tasks/config/brain-mapping.yaml` и обновить `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` (включить API-TASK-303).
- Согласовать спецификацию с заданиями loadouts/kits/profiles (`API-TASK-299/301/302`) и abilities (`API-TASK-038`).
- После создания спецификации инициировать задачи для UI редактора макросов и realtime-исполнения по необходимости.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

