# Task ID: API-TASK-226
**Тип:** API Generation
**Приоритет:** критический
**Статус:** queued
**Создано:** 2025-11-08 04:12
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-224, API-TASK-225, API-TASK-196

---

## 📋 Краткое описание

Сформировать OpenAPI спецификацию для backend движка квестов: стейт-машина, ветвления, диалоги, skill checks, награды.

**Что нужно сделать:** Создать `api/v1/quests/quest-engine.yaml`, описав REST и WebSocket контракты исполнителя квестов на базе `.BRAIN/05-technical/backend/quest-engine-backend.md`.

---

## 🎯 Цель задания

Обеспечить единую точку управления квестами для PvE/PvP контента, сценариев и live-ops.

**Зачем это нужно:**
- Управлять жизненным циклом квестов (accept → progress → complete → fail → reset)
- Поддерживать диалоги с branching и skill checks
- Интегрировать квесты с progression, inventory, achievements, reputation
- Предоставить API для UI, сценаристов, GM инструментов

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/backend/quest-engine-backend.md`
**Версия:** v1.0.0 (2025-11-07)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Quest state machine и статусы
- Dialogue tree, branching, skill checks
- Condition/Requirement система (items, репутация, таймеры)
- Rewards, scripts, instancing
- GM/Designer инструменты, тестовые режимы

### Дополнительные источники

- `.BRAIN/05-technical/backend/progression-backend.md`
- `.BRAIN/05-technical/backend/inventory-system/part1-core-system.md`
- `.BRAIN/05-technical/backend/achievement-system.md`
- `.BRAIN/05-technical/backend/reputation-system.md`
- `.BRAIN/05-technical/backend/dialogue-system.md`

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-211-leaderboards-ui-api.md`
- `API-SWAGGER/tasks/active/queue/task-224-progression-backend-api.md`
- `API-SWAGGER/tasks/active/queue/task-225-leaderboard-system-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/quests/quest-engine.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3

```
API-SWAGGER/api/v1/quests/
 ├── quest-engine.yaml          ← создать/обновить
 ├── quest-engine-components.yaml
 └── quest-engine-examples.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** quest-service (в составе world-service)
- **Порт:** 8087
- **API Base Path:** `/api/v1/quests`
- **Зависимости:**
  - auth-service – валидация аккаунта и персонажа
  - progression-service – выдача XP, skill progression
  - inventory-service – выдача и проверка предметов
  - economy-service – награды, штрафы, платежи
  - achievement-service – триггеры достижений
  - reputation-service – изменение отношений
  - dialogue-service – подготовка узлов диалога
  - analytics-service – отчёты по выполнению квестов
  - notification-service – уведомления
  - realtime-service – live updates

### Frontend
- **Модуль:** `modules/quests/engine`
- **State Store:** `useQuestStore`
- **State:** `activeQuests`, `questDetails`, `dialogueNodes`, `skillChecks`, `timers`
- **UI компоненты:** `QuestJournal`, `QuestDetailView`, `DialoguePanel`, `SkillCheckPrompt`, `QuestTracker`, `QuestRewardModal`
- **Формы:** `QuestDecisionForm`, `SkillCheckForm`, `QuestAbandonForm`
- **Хуки:** `useQuestProgress`, `useDialogueRunner`, `useSkillCheck`, `useQuestTimers`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: quest-service (port 8087)
# - API Base: /api/v1/quests
# - Dependencies: auth, progression, inventory, economy, achievement, reputation, dialogue, analytics, notification, realtime
# - Frontend Module: modules/quests/engine (useQuestStore)
# - UI: QuestJournal, QuestDetailView, DialoguePanel, SkillCheckPrompt, QuestTracker, QuestRewardModal
# - Forms: QuestDecisionForm, SkillCheckForm, QuestAbandonForm
# - Hooks: useQuestProgress, useDialogueRunner, useSkillCheck, useQuestTimers
```

---

## ✅ Что нужно сделать (детальный план)

1. Описать модели квестов, состояний, шагов, диалогов, условий и наград.
2. Добавить эндпоинты для поиска квестов, принятия, обновления прогресса, завершения, провала, сброса.
3. Реализовать контракты для диалоговых узлов, вариантов выбора, skill checks.
4. Описать систему условий (items, reputation, flags, timers, co-op).
5. Добавить REST/WS события progression, reward выдачи, таймеров, world impacts.
6. Поддержать GM/Designer endpoints для тестирования, сборки и катсцен.
7. Подготовить примеры, тестовые кейсы, чеклист.

---

## 🔀 Endpoints

1. **GET `/api/v1/quests/catalog`** – список доступных квестов с фильтрами (region, level, faction).
2. **GET `/api/v1/quests/{questId}`** – описание квеста, требования, шаги, награды.
3. **POST `/api/v1/quests/{questId}/accept`** – принять квест (проверка условий, выдача initial state).
4. **POST `/api/v1/quests/{questId}/progress`** – обновление прогресса (step completion, skill check result, branch).
5. **POST `/api/v1/quests/{questId}/complete`** – завершение, выдача наград, триггер событий.
6. **POST `/api/v1/quests/{questId}/fail`** – провал, штрафы, запись причин.
7. **POST `/api/v1/quests/{questId}/abandon`** – отказ, логирование, optional penalties.
8. **GET `/api/v1/quests/{questId}/dialogue`** – активный диалоговый узел, опции, skill checks.
9. **POST `/api/v1/quests/{questId}/dialogue`** – выбор варианта, переход к следующему узлу.
10. **POST `/api/v1/quests/{questId}/skill-check`** – запуск/проверка skill check (dice roll, modifiers, advantage).
11. **GET `/api/v1/quests/players/{playerId}/active`** – активные квесты, статус, таймеры.
12. **GET `/api/v1/quests/players/{playerId}/history`** – история выполненных/проваленных квестов.
13. **POST `/api/v1/quests/{questId}/reset`** – GM reset (audit, причины).
14. **POST `/api/v1/quests/{questId}/simulate`** – дизайнерский симулятор ветвлений/skill checks.
15. **WS `/api/v1/quests/stream`** – события: `quest-updated`, `step-completed`, `dialogue-node`, `skill-check`, `quest-completed`, `quest-failed`.

---

## 🧱 Модели данных

- **Quest** – `questId`, `title`, `category`, `description`, `region`, `levelRange`, `factions`, `rewards`, `flags`.
- **QuestState** – `playerId`, `questId`, `status`, `currentStep`, `branch`, `progress`, `startedAt`, `expiresAt`.
- **QuestStep** – `stepId`, `type`, `objectives`, `targets`, `requirements`, `timers`.
- **DialogueNode** – `nodeId`, `speaker`, `text`, `options[]`, `skillCheck`, `conditions`.
- **DialogueOption** – `optionId`, `text`, `requires`, `effects`, `nextNode`.
- **SkillCheck** – `skill`, `difficulty`, `baseRoll`, `modifiers`, `outcome`, `failureBranch`.
- **QuestReward** – `xp`, `currency`, `items`, `reputation`, `unlock`, `achievements`, `branchRewards`.
- **QuestCondition** – `type`, `parameters`, `comparison`, `value`, `source`.
- **RealtimeEventPayload** – `questUpdated`, `stepCompleted`, `dialogueNode`, `skillCheck`, `questCompleted`, `questFailed`.
- **Error Schema (`QuestError`)** – codes (`QUEST_LOCKED`, `CONDITION_FAILED`, `SKILL_CHECK_REQUIRED`, `TIMED_OUT`, `BRANCH_INCONSISTENT`, `SIMULATION_FAILED`, `RESET_DENIED`).

---

## 🧭 Принципы и правила

- Авторизация: `BearerAuth` (players), `ServiceToken` (ingest), `GMToken` (admin ops).
- Idempotency: progression updates и skill-checks должны иметь `idempotencyKey`.
- Таймеры: поддержка countdown и grace periods.
- Совместные квесты: предусмотреть party/guild scopes (расширение).
- Аудит: GM операции логируются в admin-tools.
- Локализация: предусмотреть поля локализованных текстов (использовать shared компоненты).
- Безопасность: защитить от читинга (server authoritative, anti-rollback).

---

## 🧪 Примеры

- Приём квеста с условиями по репутации и предметам.
- Прохождение skill check с Advantage и branching outcome.
- Завершение квеста с выдачей наград и уведомлениями.
- Провал таймерного шага и автоматический переход в fail state.
- GM reset с audit логом.

---

## 🔗 Связности и зависимости

- Используется progression, inventory, reputation, achievements, analytics.
- Интеграция с UI (`QuestJournal`, `DialoguePanel`), realtime и уведомлениями.
- Связан с clan war events, world state и live events (через hooks).

---

## ✅ Критерии приемки

1. `quest-engine.yaml` описывает все жизненные циклы и ветвления.
2. Прописаны модели, ошибки, события, зависимости.
3. Добавлены примеры, тест-кейсы, чеклист.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Определены микросервис, UI модуль, зависимости
- [ ] Эндпоинты и события покрывают все сценарии квестов
- [ ] Добавлены модели, ошибки, примеры, критерии
- [ ] Обновить `tasks/config/brain-mapping.yaml`

---

## ❓FAQ

**Q:** Как хранить ветвления и зависимости?**
**A:** Использовать DAG/graph структуру с версионированием узлов; описать в `components/schemas/QuestNode`.

**Q:** Нужны ли batch-эндпоинты для прогресса?**
**A:** Да, предусмотреть возможность batch ingestion через будущий endpoint `progress/batch`; отметить в расширениях.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

