# Task ID: API-TASK-283
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 04:00  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** API-TASK-272 (faction quest chains API), API-TASK-279 (factions history timeline API), API-TASK-280 (faction social dialogues API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `quest-branching-database.yaml`, описывающую REST/WS контуры для управления ветвящимися квестами NECPGAME: хранение дерева квестов, диалоговых узлов, проверок навыков, прогресса игроков, флагов, мирового состояния и последствий.

**Что нужно сделать:** На основе SQL-схем из `.BRAIN/06-tasks/active/CURRENT-WORK/active/quest-branching-database/*` определить аудит API для gameplay-service (основной), с интеграциями в social-service, world-service, analytics-service и narrative-service.

---

## 🎯 Цель задания

Обеспечить:
- CRUD и выборку для core таблиц (`quests`, `quest_branches`, `dialogue_nodes`, `dialogue_choices`, `skill_checks`)
- Операции для прогресса игроков (`quest_progress`, `player_quest_choices`, `player_flags`)
- Управление глобальным состоянием (`world_state`, `quest_consequences`) и синхронизацию с другими сервисами
- SQL-инспирированные модели данных + DTO для фронтендов (`modules/gameplay/quests`, `modules/narrative/dialogue`)
- Телеметрию и события (quest started, branch commit, consequence applied)

---

## 📚 Источники информации

- `.BRAIN/06-tasks/active/CURRENT-WORK/active/quest-branching-database/README.md` — обзор, покрытие, зависимости  
- `.BRAIN/06-tasks/active/CURRENT-WORK/active/quest-branching-database/part1-analysis-core.md` — core таблицы и индексы  
- `.BRAIN/06-tasks/active/CURRENT-WORK/active/quest-branching-database/part2-advanced-examples.md` — advanced таблицы, SQL-примеры  
- Дополнительно:
  - `.BRAIN/06-tasks/active/CURRENT-WORK/active/quest-system-tech-questions-compact.md`
  - `.BRAIN/04-narrative/dialogues/faction-social-lines.md`
  - `.BRAIN/02-gameplay/world/factions/faction-quest-chains.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/quests/branching-database.yaml`  
**Микросервисы:** gameplay-service (ядро квестов), narrative-service (диалоги), social-service (репутация/отношения), world-service (world flags), analytics-service (телеметрия), economy-service (награды)  
**Frontend:** `modules/gameplay/quests`, `modules/narrative/dialogue`, state store `quests/branching`

---

## 🧩 Обязательные секции

1. **Каталог и выдача квестов**
   - `GET /api/v1/quests` — фильтры по типу, уровню, фракции, статусу
   - `GET /api/v1/quests/{questId}` — подробная метадата, требования, корневой диалог, связанные ветки
   - `POST /api/v1/quests` / `PATCH /api/v1/quests/{questId}` — управление в GM/LiveOps режимах (админ безопасность)

2. **Ветвление и диалоги**
   - `GET /api/v1/quests/{questId}/branches`
   - `GET /api/v1/quests/{questId}/dialogue-nodes/{nodeId}`
   - `POST /api/v1/quests/{questId}/dialogue-choices/{choiceId}/resolve` (применяет выбор, выполняет проверки и последствия)

3. **Проверки навыков**
   - `POST /api/v1/quests/{questId}/skill-checks/{checkId}/roll` — входные данные: статы, модификаторы, предметы
   - Ответ содержит исход (success/failure/crit) и следующий узел

4. **Прогресс игрока**
   - `GET /api/v1/quests/progress` (для игрока) / `GET /api/v1/quests/progress/{characterId}` (GM/analytics)
   - `POST /api/v1/quests/{questId}/progress` — старт/обновление, синхронизация `objectives_state`
   - `POST /api/v1/quests/{questId}/progress/reset` — soft reset / failover с логированием

5. **История выборов и флаги**
   - `GET /api/v1/quests/{questId}/choices/history`
   - `POST /api/v1/quests/{questId}/choices` — запись выбора (для внешних сценариев)
   - `GET /api/v1/players/{characterId}/flags`
   - `POST /api/v1/players/{characterId}/flags` — установка / обновление флагов

6. **Глобальное состояние и последствия**
   - `GET /api/v1/world/quests/state` — агрегированное `world_state`
   - `POST /api/v1/world/quests/state` — обновление (GM/automation), валидация конфликтов
   - `GET /api/v1/quests/{questId}/consequences`
   - `POST /api/v1/quests/{questId}/consequences/apply` — триггеры для world-service, social-service, economy-service

7. **WebSocket / Event Bus**
   - `/ws/quests/{questId}` — `QuestStarted`, `BranchUnlocked`, `ChoiceCommitted`, `ConsequenceApplied`, `QuestFailed`, `QuestCompleted`
   - События в event bus (`quest.progress.updated`, `quest.branch.locked`, `world.state.changed`)

8. **Схемы и модели**
   - `Quest`, `QuestBranch`, `DialogueNode`, `DialogueChoice`, `SkillCheck`, `QuestProgress`, `PlayerChoice`, `PlayerFlag`, `WorldStateEntry`, `QuestConsequence`, `SkillCheckResult`, `QuestOutcome`
   - Reuse `shared/common/pagination.yaml` и `shared/common/responses.yaml`

9. **Безопасность и ограничения**
   - RBAC (`player`, `gm`, `automation`)
   - Rate limits для write endpoints (prevent spam)
   - Ограничения на размер payload (JSONB поля)

10. **Observability**
    - Метрики: `quest_active_count`, `branch_distribution`, `skill_check_success_rate`, `flag_set_rate`, `consequence_latency`
    - Логи и трассировка: correlation id (`questSessionId`), `characterId`

---

## ✅ Критерии приемки

1. Префикс `/api/v1/quests` используется для игровых операций; `/api/v1/world/quests` — для глобального состояния; `/api/v1/players` — для флагов.
2. Схемы отражают поля и ограничения из SQL (тип данных, уникальные ключи, JSONB структуры, индексы).
3. Поддержаны условные требования (флаги, репутация, класс, происхождение) и модификаторы проверок.
4. Прогресс и история выборов возвращают аудит (timestamp, roll, ветка, последствия).
5. Quest consequences генерируют события для world-service и social-service (через event bus + REST callbacks).
6. Все ошибки используют общую схему `Error` (`shared/common/responses.yaml#/components/schemas/Error`).
7. WebSocket payload включает `questId`, `characterId`, `branchId`, `choiceId`, `consequenceId`, `worldStateKey`.
8. Документированы ограничения: максимальный размер `objectives_state`, TTL для флагов, политика синхронизации world state.
9. Target Architecture описывает взаимодействия gameplay-service ↔ social/world/economy/narrative ↔ frontend (`modules/gameplay/quests`, `modules/narrative/dialogue`).
10. Приведен FAQ: миграции (uuid-ossp, pgcrypto), порядок создания таблиц, обработка конфликтов при одновременных обновлениях, откат веток.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

