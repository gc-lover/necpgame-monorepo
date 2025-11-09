# Task ID: API-TASK-178
**Тип:** API Generation | **Приоритет:** критический | **Статус:** queued
**Создано:** 2025-11-07 17:30 | **Создатель:** AI Agent ДУАПИТАСК | **Зависимости:** API-TASK-138

---

## 📋 Описание

Создать API для системы ветвления квестов (4 микрофичи). Database schema, ER diagram, branching logic, shooter-based skill challenges, dialogue choices, consequences.

---

## 📚 Источники (4 документа)

**Quest Branching Parts:**
- `06-tasks/active/CURRENT-WORK/active/quest-branching-db-schema.md` - Database schema (~480 строк)
- `06-tasks/active/CURRENT-WORK/active/quest-branching-er-part1.md` - ER diagram part 1
- `06-tasks/active/CURRENT-WORK/active/quest-branching-er-part2.md` - ER diagram part 2
- `06-tasks/active/CURRENT-WORK/active/quest-branching-logic.md` - Branching logic (~498 строк)

**Оригиналы:**
- `06-tasks/active/CURRENT-WORK/active/2025-11-06-quest-branching-database-design.md`
- `06-tasks/active/CURRENT-WORK/active/2025-11-06-quest-branching-er-diagram.md`

---

## 🎯 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/gameplay/quests/branching.yaml`
**API версия:** v1
**Тип файла:** OpenAPI 3.0 Specification (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── quests/
                ├── branching.yaml  ← Создать этот файл
                └── quest-system.yaml
```

---

## ✅ Что нужно сделать

### Шаг 1: Создание базовой структуры файла

**Действия:**
1. Создать файл `api/v1/gameplay/quests/branching.yaml`.
2. Добавить базовую информацию OpenAPI (openapi, info, servers, tags).
3. Определить теги: `Quest Branching`, `Skill Checks`, `Dialogue Choices`.

**Ожидаемый результат:**
- Файл `branching.yaml` с корректной базовой структурой OpenAPI.

### Шаг 2: Реализация Endpoints для ветвления

**Действия:**
1. Добавить endpoint `POST /gameplay/quests/{quest_id}/choices` для создания выбора в квесте.
   - Request body: `QuestChoiceRequest` (choice_id, choice_text, skill_checks, consequences)
   - Responses: `200 OK` (QuestChoiceResponse), `400 BadRequest` (Error)
2. Добавить endpoint `POST /gameplay/quests/{quest_id}/choices/{choice_id}/execute` для выполнения выбора.
   - Request body: `ExecuteChoiceRequest` (player_id, skill_roll_results)
   - Responses: `200 OK` (BranchResult), `400 BadRequest` (Error)
3. Добавить endpoint `GET /gameplay/quests/{quest_id}/branches` для получения возможных веток.
   - Responses: `200 OK` (QuestBranchesResponse), `404 NotFound` (Error)
4. Добавить endpoint `POST /gameplay/quests/{quest_id}/skill-check` для проверки навыка.
   - Request body: `SkillCheckRequest` (skill_name, difficulty, modifiers)
   - Responses: `200 OK` (SkillCheckResult), `400 BadRequest` (Error)

**Ожидаемый результат:**
- Endpoints для управления ветвлением квестов и skill checks.

### Шаг 3: Определение моделей данных

**Действия:**
1. Создать схемы для моделей:
   - `QuestChoiceRequest` (choice_id, choice_text, skill_checks[], consequences[])
   - `QuestChoiceResponse` (choice_id, available, skill_check_results[])
   - `ExecuteChoiceRequest` (player_id, skill_roll_results[])
   - `BranchResult` (success, new_quest_state, consequences_applied[])
   - `QuestBranchesResponse` (current_node_id, available_choices[])
   - `SkillCheckRequest` (skill_name, difficulty_class, modifiers[])
   - `SkillCheckResult` (success, roll, total, required)
   - `Consequence` (type, target, value, description)
2. Использовать `PascalCase` для имен моделей.
3. Добавить примеры для каждой модели.

**Ожидаемый результат:**
- Все модели данных определены в секции `components/schemas`.

### Шаг 4: Определение схем безопасности

**Действия:**
1. Использовать `BearerAuth` из `shared/security/security.yaml`.
2. Определить `security` для каждого защищенного эндпоинта.

**Ожидаемый результат:**
- Корректное применение схем безопасности.

### Шаг 5: Валидация и правила

**Действия:**
1. Добавить валидацию для skill checks (difficulty_class от 1 до 30).
2. Указать ограничения для consequences.
3. Определить бизнес-правила для ветвления.

**Ожидаемый результат:**
- Валидация и бизнес-правила отражены в схемах.

---

## 📚 Дополнительная информация

См. дополнительный файл: **[api-generation-task-template-details.md](../../templates/api-generation-task-template-details.md)**

---

**ВНИМАНИЕ:** Это задание для АПИТАСК агента. Выполняй пошагово. КРИТИЧЕСКИЙ приоритет!


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

