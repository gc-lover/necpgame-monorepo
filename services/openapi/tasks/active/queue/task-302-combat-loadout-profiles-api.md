# Task ID: API-TASK-302
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 01:05
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** [API-TASK-038], [API-TASK-140], [API-TASK-244], [API-TASK-299]

---

## 📋 Краткое описание

Спроектировать OpenAPI/AsyncAPI спецификацию управления боевыми профильными требованиями (Loadout Profiles), адаптацией к событиям и угрозам, а также валидацией ролей, мастерства и условий PvE/PvP.

**Что нужно сделать:** На основе `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` описать REST/Async контракты для каталога профилей, требований к ролям, адаптации под мировые события, проверок соответствия и аналитики угроз.

---

## 🎯 Цель задания

Создать прозрачную систему требований к лодаутам, которая гарантирует соответствие ролям, событиям и угрозам, обеспечивая баланс и динамическую адаптацию мира.

**Зачем это нужно:**
- Удерживать баланс PvE/PvP через проверку ролей, мастерства и ограничений событий.
- Давать матчмейкингу и рейдовым сценариям детальные профили, упрощающие подбор команд.
- Реализовать адаптивные рекомендации и предупреждения игрокам при изменении угроз или событий.

---

## 📚 Источники информации

### Основной источник

**Репозиторий:** `.BRAIN`  
**Документ:** `.BRAIN/02-gameplay/combat/combat-loadouts-system.md`  
**Версия:** 0.3.0  
**Дата последнего обновления:** 2025-11-08 00:14  
**Статус документа:** review, `api-readiness: ready`

**Что важно:**
- Блок «Loadouts для навыков и способностей» — роли, гибридные сетки, макрофункции.
- «PvE экспедиции и лутинг» — профили по ролям, динамические условия и модификаторы.
- «Связь с прогрессией и классами» — роль-схемы, mastery tiers, skill synergy.
- «Интеграция с событиями и картами» — live events, threatLevel, адаптация лодаута.
- «Политики фракционных комплектов» и «Управление недоступными предметами» — проверки совместимости.

### Дополнительные источники

- `.BRAIN/02-gameplay/combat/combat-roles-detailed.md` — описание ролей и synergy.
- `.BRAIN/02-gameplay/combat/combat-extract.md` — карты, экспедиции, эвак сценарии.
- `.BRAIN/02-gameplay/world/events/world-events-framework.md` — live events и modifiers.
- `.BRAIN/02-gameplay/world/world-state/living-world-kenshi-hybrid.md` — динамика мира и threat adaptation.
- `.BRAIN/02-gameplay/progression/progression-skills-mapping.md` — связи перков и mastery tiers.
- `.BRAIN/02-gameplay/progression/progression-attributes-matrix.md` — формулы скорингов.

### Связанные документы/таски

- `API-SWAGGER/tasks/active/queue/task-299-combat-loadouts-api.md`
- `API-SWAGGER/tasks/active/queue/task-038-combat-abilities-api.md`
- `API-SWAGGER/tasks/active/queue/task-140-progression-backend-api.md`
- `API-SWAGGER/tasks/active/queue/task-244-arena-system-api.md`
- `API-SWAGGER/tasks/active/queue/task-300-living-world-hybrid-api.md`

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Целевой файл:** `api/v1/gameplay/combat/loadout-profiles.yaml`  
**Формат:** OpenAPI 3.0.3 (при необходимости вынести компоненты/события)

```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── combat/
                ├── loadouts.yaml
                ├── loadout-profiles.yaml         ← создать
                ├── loadout-profiles-components.yaml
                └── loadout-profiles-events.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** gameplay-service
- **Порт:** 8083
- **API Base:** `/api/v1/gameplay/combat/loadout-profiles*`
- **Зависимости:** progression-service (skill tiers, mastery), world-service (events, threat levels), matchmaking-service (роль требования), economy-service (стоимость адаптаций), analytics-service (метрики угроз).
- **Ассоциированные сервисы:** auth-service (scopes `loadouts:profiles.*`), notification-service (предупреждения игрокам), realtime-service (live адаптации).

### Frontend
- **Модуль:** `modules/combat/loadouts/profiles`
- **State Store:** `useLoadoutProfilesStore` (profiles, requirements, events)
- **UI компоненты:** `ProfileRequirementMatrix`, `RoleCompatibilityBadge`, `ThreatAdaptationPanel`, `EventModifierTimeline`, `ValidationIssueList`, `ProfileRecommendationCard`
- **Формы:** `ProfileDefinitionForm`, `ThreatAdaptationForm`, `EventModifierForm`
- **Хуки:** `useProfileValidation`, `useThreatForecast`, `useEventSync`, `useProfileRecommendations`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: gameplay-service (port 8083)
# - API Base: /api/v1/gameplay/combat/loadout-profiles*
# - Dependencies: progression, world, matchmaking, economy, analytics, auth
# - Frontend Module: modules/combat/loadouts/profiles (useLoadoutProfilesStore)
# - UI: ProfileRequirementMatrix, ThreatAdaptationPanel, EventModifierTimeline
# - Forms: ProfileDefinitionForm, ThreatAdaptationForm, EventModifierForm
# - Hooks: useProfileValidation, useThreatForecast, useEventSync, useProfileRecommendations
```

---

## ✅ Что нужно сделать (детальный план)

1. Собрать требования из документа: роль-схемы, mastery tiers, ограничения событий, threat адаптация.
2. Спроектировать REST endpoints для управления профилями, требований, адаптаций, динамических модификаторов и рекомендаций.
3. Описать схемы `LoadoutProfile`, `ProfileRequirement`, `MasteryGate`, `ThreatAdaptationProfile`, `EventModifier`, `ValidationIssue`, `Recommendation`.
4. Добавить endpoints для проверок соответствия (`validate`), генерации рекомендаций, выгрузки threat analytics, обновления профилей при событиях.
5. Смоделировать асинхронные события (`loadout.profile.updated`, `loadout.profile.violated`, `loadout.profile.event-adjusted`, `loadout.profile.recommendation-issued`) с payload и приоритетами.
6. Прописать безопасность, idempotency, аудит, включая `Idempotency-Key`, `X-Audit-Id`, ограничения на обновления профилей (approval workflow).
7. Подготовить примеры запросов/ответов/событий, описать интеграцию с progression/world сервисами (через `$ref`), учесть формулы `skillSynergyScore`, `masteryTier`.
8. Сформировать чеклист, критерии приёмки, FAQ, инструкции по обновлению mapping и `.BRAIN`.

---

## 🔀 Требуемые эндпоинты

1. `GET /api/v1/gameplay/combat/loadout-profiles` — каталог профилей (фильтры: роль, событие, masteryTier, статус).
2. `POST /api/v1/gameplay/combat/loadout-profiles` — создание профиля с требованиями и метаданными.
3. `GET /api/v1/gameplay/combat/loadout-profiles/{profileCode}` — подробности профиля, связанные роли, ограничения, history.
4. `PATCH /api/v1/gameplay/combat/loadout-profiles/{profileCode}` — обновление требований, адаптаций, статусов (approval workflow).
5. `POST /api/v1/gameplay/combat/loadout-profiles/{profileCode}/validate` — проверка конкретного лодаута на соответствие (возвращает issues, warnings).
6. `GET /api/v1/gameplay/combat/loadout-profiles/{profileCode}/requirements` — выдача требований и капов для UI и матчмейкинга.
7. `POST /api/v1/gameplay/combat/loadout-profiles/{profileCode}/events/apply` — применение модификаторов live events (вход: eventCode, threatDelta).
8. `POST /api/v1/gameplay/combat/loadout-profiles/{profileCode}/threat/adapt` — генерация адаптационного профиля для новых уровней угрозы.
9. `GET /api/v1/gameplay/combat/loadout-profiles/threat-analytics` — метрики угроз (threatLevel, violationRate, adaptationSuccess).
10. `GET /api/v1/gameplay/combat/loadout-profiles/recommendations` — рекомендации по ролям/комплектам для заданного события/роли.
11. `POST /api/v1/gameplay/combat/loadout-profiles/{profileCode}/lock` — блокировка/разблокировка профиля (при нарушениях).
12. `GET /api/v1/gameplay/combat/loadout-profiles/audit` — аудит изменений (кто утвердил, когда применены события).

Все POST/PATCH endpoints требуют `Authorization`, `Idempotency-Key`, `X-Audit-Id`, а ответы используют общие `$ref` для ошибок и пагинации.

---

## 🧱 Модели данных

- **LoadoutProfile** — `profileCode`, `name`, `roleCode`, `category`, `description`, `status`, `masteryTier`, `skillSynergyScore`, `eventTags[]`, `threatLevelRange`, `requirements`, `adaptations`, `approvers[]`, `createdAt`, `updatedAt`.
- **ProfileRequirement** — `attributeCaps`, `skillTags`, `requiredKits[]`, `forbiddenKits[]`, `implantBudget`, `consumableBudget`, `macroLimits`, `movementProfile`.
- **MasteryGate** — `tier`, `minMasteryScore`, `requiredAchievements[]`, `respecTokenRequired`, `cooldown`.
- **EventModifier** — `eventCode`, `modifierType`, `delta`, `duration`, `recommendedKits[]`, `blockedAbilities[]`, `environmentalHazards[]`.
- **ThreatAdaptationProfile** — `threatLevel`, `recommendedAdjustments`, `fallbackKits[]`, `sensorConfig`, `mobilityAdjustments`, `countermeasureFlags`.
- **ValidationIssue** — `type`, `severity`, `message`, `affectedSlots`, `resolutionHint`, `blocked`.
- **Recommendation** — `profileCode`, `roleCode`, `context`, `suggestedKits[]`, `skillAdjustments`, `expectedOutcome`.
- **ProfileAuditEntry** — `entryId`, `profileCode`, `action`, `changedBy`, `changes[]`, `previousVersion`, `approvedBy`, `timestamp`.
- **ThreatMetric** — `profileCode`, `threatLevel`, `violationRate`, `activationSuccess`, `averageResolutionTime`.
- **Async Events** — payloads для `loadout.profile.updated`, `loadout.profile.violated`, `loadout.profile.event-adjusted`, `loadout.profile.recommendation-issued`.

---

## 🧭 Принципы и правила

- OpenAPI 3.0.3, соблюдение лимита 400 строк: схемы/события выносить при необходимости.
- Использовать `$ref` на общие компоненты (security, responses, pagination, errors).
- Проверки профилей должны ссылаться на loadouts API (`API-TASK-299`) и progression API (`API-TASK-140`) через `$ref`.
- Поддерживать approval workflow: обновления профиля требуют подтверждения; описать состояние `draft`, `in-review`, `approved`, `suspended`.
- Threat adaptation должна учитывать данные world-service (live events, угрозы) и публиковать события для analytics.
- Обязательно документировать ошибки `409 PROFILE_CONFLICT`, `423 PROFILE_LOCKED`, `428 PROFILE_APPROVAL_REQUIRED`.
- Метрики и рекомендации отражаются в analytics-service: указать поля и frequency обновления.

---

## ✅ Критерии приемки

1. Все 12 эндпоинтов описаны с параметрами, схемами, примерами запросов/ответов.
2. Прописаны состояния профиля и требования approval workflow.
3. Асинхронные события описаны с payload, каналами, приоритетами и ретраями.
4. Валидация возвращает структурированные `ValidationIssue[]` с классификацией (BLOCKER/WARNING/INFO).
5. Threat adaptation учитывает уровни угрозы и live events; описаны поля `threatLevel`, `eventCode`, `adaptationActions`.
6. Рекомендации включают связи с навыками, комплектами и событиями, указаны источники данных.
7. Для всех мутаций задокументированы `Idempotency-Key`, `X-Audit-Id`, `ETag`, коды ошибок `409`, `423`, `428`.
8. Интеграции с progression/world/matchmaking сервисами отражены через `$ref` и соответствующие поля.
9. Подготовлены примеры JSON для валидации, адаптации, рекомендаций, блокировок.
10. Checklist, FAQ и инструкции по обновлению mapping/.BRAIN заполнены.
11. Комментарий архитектуры присутствует в YAML спецификации.

---

## 📎 Checklist перед сдачей

- [ ] Все разделы шаблона заполнены, ссылки на `.BRAIN` и связанные API корректны.
- [ ] OpenAPI проходит валидацию, длина файла ≤400 строк, при превышении — вынести компоненты.
- [ ] Асинхронные события согласованы с realtime/notification сервисами.
- [ ] Примеры покрывают сценарии: проверка соответствия, адаптация к угрозе, live events, рекомендации, блокировка.
- [ ] Аппрув workflow и статусы профиля документированы.
- [ ] Инструкции по обновлению `brain-mapping.yaml` и `.BRAIN` подготовлены.

---

## ❓ FAQ

**Q:** Что происходит при несоответствии профилю перед матчем?  
**A:** Возвращается ошибка `409 PROFILE_CONFLICT` с `ValidationIssue` списка. Событие `loadout.profile.violated` отправляется в matchmaking и notification сервисы.

**Q:** Как профили реагируют на live events?  
**A:** Через endpoint `/events/apply` и событие `loadout.profile.event-adjusted`. Профиль обновляет модификаторы, рекомендации пересчитываются и рассылаются UI/notification.

**Q:** Можно ли временно приостановить профиль?  
**A:** Да, endpoint `/lock` переводит профиль в `suspended`, блокируя использование до решения. Документируйте коды ошибок и события `loadout.profile.locked`.

---

## 🔗 Связность и последующие шаги

- Добавить запись о задаче в `tasks/config/brain-mapping.yaml` и обновить `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` (API-TASK-302).
- Согласовать спецификацию с заданиями progression (`API-TASK-140`), living world (`API-TASK-300`) и арен (`API-TASK-244`).
- После подготовки спецификации инициировать задачи для UI (profile dashboard) и analytics (threat scoring) при необходимости.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

