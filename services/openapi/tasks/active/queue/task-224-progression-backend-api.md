# Task ID: API-TASK-224
**Тип:** API Generation
**Приоритет:** критический
**Статус:** queued
**Создано:** 2025-11-08 03:48
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-138, API-TASK-214, API-TASK-219

---

## 📋 Краткое описание

Реализовать прогрессионный backend: расчёт опыта, уровней, навыков, атрибутов и метрик.

**Что нужно сделать:** Создать `api/v1/progression/progression-engine.yaml`, описав систему Level XP, skill progression, attribute distribution, buffs.

---

## 🎯 Цель задания

Обеспечить центральный сервис прокачки, влияющий на все игровые системы.

**Зачем это нужно:**
- Управлять опытом и уровнями персонажей и клана
- Обрабатывать навыки, перки, атрибуты, unlocks
- Предоставить API для квестов, боёв, экономики и достижений
- Синхронизировать прогрессию с UI и аналитикой

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/backend/progression-backend.md`
**Версия:** v1.0.0 (2025-11-07)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Level XP таблицы, формулы и кривые роста
- Skill progression, skill trees, mastery
- Attribute points распределение
- Buff/Perk системы
- Analytics & leaderboards

### Дополнительные источники

- `.BRAIN/05-technical/backend/quest-engine-backend.md`
- `.BRAIN/05-technical/backend/combat-session-backend.md`
- `.BRAIN/05-technical/backend/achievement-system.md`
- `.BRAIN/05-technical/backend/economy-system.md`
- `.BRAIN/05-technical/backend/party-system.md`

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-140-progression-backend-api.md`
- `API-SWAGGER/tasks/active/queue/task-214-inventory-advanced-api.md`
- `API-SWAGGER/tasks/active/queue/task-219-achievement-tracking-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/progression/progression-engine.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3

```
API-SWAGGER/api/v1/progression/
 ├── progression-engine.yaml  ← создать/обновить
 ├── skill-trees.yaml         (возможное расширение)
 └── progression-examples.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** progression-service (или gameplay-service module)
- **Порт:** 8083
- **API Base Path:** `/api/v1/progression`
- **Зависимости:**
  - auth-service – проверка аккаунта/персонажа
  - quest-service – выдача XP за квесты
  - combat-service – XP за бои
  - economy-service – XP boosters, consumables
  - inventory-service – предметы, влияющие на прогрессию
  - notification-service – уведомления о level up
  - analytics-service – статистика progression

### Frontend
- **Модуль:** `modules/progression/core`
- **State Store:** `useProgressionStore`
- **State:** `level`, `xp`, `skillTrees`, `attributes`, `boosts`
- **UI компоненты:** `ProgressionDashboard`, `LevelProgressBar`, `SkillTreeView`, `AttributeAllocationPanel`, `BoostsPanel`
- **Формы:** `SkillAllocationForm`, `AttributeResetForm`, `BoostConsumptionForm`
- **Хуки:** `useProgression`, `useSkillTrees`, `useAttributePlanner`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: progression-service (port 8083)
# - API Base: /api/v1/progression
# - Dependencies: auth, quest, combat, economy, inventory, notification, analytics
# - Frontend Module: modules/progression/core (useProgressionStore)
# - UI: ProgressionDashboard, LevelProgressBar, SkillTreeView, AttributeAllocationPanel, BoostsPanel
# - Forms: SkillAllocationForm, AttributeResetForm, BoostConsumptionForm
# - Hooks: useProgression, useSkillTrees, useAttributePlanner
```

---

## ✅ Что нужно сделать (детальный план)

1. Определить модели: `PlayerProgression`, `LevelInfo`, `SkillProgress`, `AttributeState`, `BoostState`.
2. Описать XP таблицы, формулы, modifiers (rested XP, bonus events).
3. Реализовать эндпоинты получения/обновления прогресса, skill trees, атрибутов.
4. Добавить управление бустами (активация, длительность, эффекты).
5. Описать API reset/refund, allocation presets, analytics endpoints.
6. Настроить события (`progression:level-up`, `progression:skill-updated`, `progression:boost-applied`).
7. Связать API с achievement tracking, quest rewards и economy boosts.
8. Подготовить примеры, тестовые сценарии, чеклист.

---

## 🔀 Endpoints

1. **GET `/api/v1/progression/players/{playerId}`** – текущий уровень, XP, атрибуты, бусты.
2. **POST `/api/v1/progression/players/{playerId}/xp`** – начисление XP (quest/combat/event), с источник данным.
3. **POST `/api/v1/progression/players/{playerId}/level/reset`** – reset/rollback уровня (GM/incident).
4. **GET `/api/v1/progression/players/{playerId}/skills`** – дерево навыков, прогресс.
5. **POST `/api/v1/progression/players/{playerId}/skills`** – апгрейд навыка (расход Skill Points).
6. **POST `/api/v1/progression/players/{playerId}/skills/reset`** – сброс навыков (сбор ресурсов/плата).
7. **GET `/api/v1/progression/players/{playerId}/attributes`** – атрибуты и доступные очки.
8. **POST `/api/v1/progression/players/{playerId}/attributes`** – распределение атрибутов.
9. **POST `/api/v1/progression/players/{playerId}/attributes/reset`** – сброс (дорогая операция, логирование).
10. **GET `/api/v1/progression/players/{playerId}/boosts`** – активные бусты, оставшееся время.
11. **POST `/api/v1/progression/players/{playerId}/boosts`** – активация/удаление буста.
12. **GET `/api/v1/progression/tables/levels`** – XP таблица (для UI/analytics).
13. **GET `/api/v1/progression/tables/skills`** – справочник навыков, веток, зависимостей.
14. **GET `/api/v1/progression/leaderboard`** – топ игроков по уровню, skill mastery, progression score.
15. **WS `/api/v1/progression/stream`** – события: `xp-gained`, `level-up`, `skill-unlocked`, `attribute-changed`, `boost-activated`.

---

## 🧱 Модели данных

- **PlayerProgression** – `playerId`, `level`, `currentXp`, `xpToNextLevel`, `totalXp`, `prestigeLevel`, `lastLevelUpAt`.
- **XpEvent** – `source` (`QUEST|COMBAT|CRAFT|SOCIAL|EVENT`), `amount`, `bonus`, `idempotencyKey`, `metadata`.
- **SkillTree** – `skillId`, `branch`, `tier`, `requirements`, `currentRank`, `maxRank`, `effects`.
- **AttributeState** – `attribute`, `allocatedPoints`, `baseValue`, `bonusValue`, `maxValue`.
- **BoostState** – `boostId`, `type`, `multiplier`, `source`, `activatedAt`, `expiresAt`, `stackingRule`.
- **LeaderboardEntry** – `playerId`, `nickname`, `level`, `skillScore`, `prestige`, `trend`.
- **RealtimeEventPayload** – `xpGained`, `levelUp`, `skillUnlocked`, `attributeChanged`, `boostActivated`.
- **Error Schema (`ProgressionError`)** – codes (`LEVEL_CAP`, `INSUFFICIENT_POINTS`, `SKILL_LOCKED`, `BOOST_CONFLICT`, `RESET_LIMIT`, `IDEMPOTENCY_CONFLICT`).

---

## 🧭 Принципы и правила

- Авторизация: `BearerAuth` (players), `ServiceToken` (internal events), `GM` (reset).
- Idempotency: XP начисления и reset операции должны поддерживать `idempotencyKey`.
- Лимиты: cap на XP/уровень, cooldown на resets.
- Экономика: some resets/boosts требуют оплаты; логировать операции.
- Инциденты: ошибки критического уровня отправлять в incident-service.
- DRY: использовать shared компоненты (`responses`, `pagination`, `security`).

---

## 🧪 Примеры

- Начисление 500 XP за рейд с rested bonus.
- Level-up событие с уведомлением и выдачей атрибутов.
- Распределение skill points и unlock навыка.
- Сброс атрибутов через GM команду.
- WebSocket событие `boost-activated`.

---

## 🔗 Связности и зависимости

- Используется квестами, боями, достижениями, клановыми системами.
- Интегрируется с inventory/economy для бустов, наград.
- Поддерживает аналитические отчёты progression.

---

## ✅ Критерии приемки

1. `progression-engine.yaml` создан, содержит все модели и эндпоинты.
2. Прописаны XP формулы, уровни, навыки, атрибуты, boost механики.
3. Описаны события, интеграции, аудит, ограничения.
4. Примеры и тест-кейсы подготовлены, чеклист выполнен.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Определены микросервис, модуль, зависимости, UI компоненты
- [ ] Эндпоинты и события покрывают весь прогрессионный функционал
- [ ] Добавлены модели, ошибки, примеры, критерии
- [ ] Обновить `tasks/config/brain-mapping.yaml`

---

## ❓FAQ

**Q:** Как обрабатывать prestige?**
**A:** В progression-core указать механизмы prestiging (сброс уровня в обмен на очки), требуются отдельные эндпоинты или флаги для future tasks.

**Q:** Нужен ли offline xp gain?**
**A:** Можно добавить cron event ingestion через batch endpoint; отметить в разделах интеграции.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

