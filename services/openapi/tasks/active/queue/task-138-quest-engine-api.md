# Task ID: API-TASK-138
**Тип:** API Generation  
**Приоритет:** критический  
**Статус:** queued  
**Создано:** 2025-11-07 10:36  
**Создатель:** AI Agent  
**Зависимости:** API-TASK-127

---

## 📋 Краткое описание
**MVP блокер.** Требуется спецификация квестового движка: state machine, диалоги, skill-checks, награды.

**Что нужно сделать:** описать API gameplay-service по документу `.BRAIN/05-technical/backend/quest-engine-backend.md`.

---

## 🎯 Цель задания
Предоставить контракт для управления жизненным циклом квестов, ветвлениями диалогов и обработкой outcomes, чтобы фронтенд и gameplay могли синхронно выполнять сценарии.

**Зачем это нужно:**
- Запуск и контроль всех сюжетных и побочных квестов.  
- Возможность D&D skill checks, выбора веток, выдачи наград.  
- Связь с progression, narration, combat лутом и аналитикой.

---

## 📚 Источники информации

### Основной источник
**Путь:** `.BRAIN/05-technical/backend/quest-engine-backend.md`  
**Версия:** v1.0.0 · **Статус:** ready · **Дата:** 2025-11-07  

**Ключевые аспекты:**
- Quest state machine (start → progress → completion/branching).  
- Диалоговый движок (nodes, choices, skill checks).  
- Reward pipeline и интеграция с progression/achievement systems.

### Дополнительные источники
- `.BRAIN/05-technical/backend/dialogue-system.md` — структура диалогов.  
- `.BRAIN/05-technical/backend/progression-backend.md` — exp, награды.  
- `.BRAIN/05-technical/backend/achievement-system.md` — триггеры ачивок.  
- `.BRAIN/05-technical/backend/event-bus-overview.md` — события, на которые подписывается движок.

### Связанные документы
- `.BRAIN/02-gameplay/narrative/quest-design-guidelines.md` — дизайн квестов.  
- `.BRAIN/04-narrative/dialogues/DIALOGUE-TEMPLATE.md` — шаблон диалогов.  
- `.BRAIN/05-technical/backend/save-system.md` — сохранение состояния.

---

## 📁 Целевая структура API
### Репозиторий: `API-SWAGGER`
**Целевой файл:** `api/v1/gameplay/quests/quest-engine.yaml`  
> ⚠️ Серверы: `https://api.necp.game/v1/gameplay` и `http://localhost:8080/api/v1/gameplay`.

**Тип:** OpenAPI 3.0.3 · **Версия:** v1

```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── quests/
                └── quest-engine.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** gameplay-service  
- **Порт:** 8083  
- **API Base:** `/api/v1/gameplay/quests`  
- **Интеграции:** narrative-service (диалоги), world-service (локации), economy-service (награды), social-service (репутация), achievement-service.  
- **Комментарий для спецификации:**
  ```yaml
  # Target Architecture:
  # - Microservice: gameplay-service (port 8083)
  # - API Base: /api/v1/gameplay/quests
  # - Dependencies: narrative-service, world-service, economy-service, social-service, world-service
  # - Frontend Module: modules/narrative/quests
  # - UI: QuestJournal, DialoguePanel, SkillCheckModal
  # - Hooks: useNarrativeStore, useRealtime, useDiceRoll
  ```

### OpenAPI требования
- `info.x-microservice`:
  ```yaml
  x-microservice:
    name: gameplay-service
    port: 8083
    domain: gameplay
    base-path: /api/v1/gameplay/quests
    directory: api/v1/gameplay/quests
    package: com.necpgame.gameplayservice
  ```
- `servers` как указано.  
- `x-websocket`: `wss://api.necp.game/v1/gameplay/quests/instances/{characterId}/stream` — realtime обновления прогресса.

### Frontend
- **Модуль:** `modules/narrative/quests`.  
- **State Store:** `useNarrativeStore` (`activeQuests`, `questLog`, `dialogueState`, `skillChecks`).  
- **UI:** QuestJournal, DialoguePanel, SkillCheckModal, ChoiceList, QuestSummary.  
- **Формы:** QuestAcceptForm, ChoiceSelectionForm, SkillCheckInputForm.  
- **Хуки:** useRealtime, useDiceRoll, useLocalization.  
- **Layouts:** GameLayout (основной интерфейс приключений).

---

## ✅ Что нужно сделать

### Шаг 1. Анализ
- Составить модель state machine (states, transitions, triggers).  
- Определить payload для диалоговых узлов и skill checks.  
- Описать reward pipeline (experience, items, reputation).

### Шаг 2. Проектировать endpoints
1. **POST `/api/v1/gameplay/quests/{questId}/start`** — запуск, валидация условий, выдача initial state.  
2. **POST `/api/v1/gameplay/quests/{questId}/progress`** — обновление по событиям (objective completed).  
3. **POST `/api/v1/gameplay/quests/{questId}/complete`** — выдача наград, финализация.  
4. **POST `/api/v1/gameplay/quests/{questId}/dialogue/{nodeId}`** — выполнение диалогового узла.  
5. **POST `/api/v1/gameplay/quests/{questId}/choice`** — выбор ветки.  
6. **POST `/api/v1/gameplay/quests/{questId}/skill-check`** — проверка навыка (результат броска).  
7. **GET `/api/v1/gameplay/quests/instances/active`** — активные квесты персонажа.  
8. **GET `/api/v1/gameplay/quests/instances/history`** — история завершённых.  
9. **GET `/api/v1/gameplay/quests/{questId}`** — детальная информация (nodes, rewards).  
10. **POST `/api/v1/gameplay/quests/{questId}/reset`** — аварийный сброс (admin/service token).

### Шаг 3. Модели
- `QuestDefinition`, `QuestInstance`, `QuestObjective`, `DialogueNode`, `SkillCheckRequest`, `SkillCheckResult`, `QuestReward`, `ChoiceOutcome`.  
- Ошибки: `QuestError` (`VAL_REQUIREMENTS_NOT_MET`, `BIZ_ALREADY_COMPLETED`, `BIZ_INVALID_NODE`, `BIZ_INVALID_CHOICE`).  
- WebSocket payload: `questUpdated`, `questCompleted`, `dialogueAdvanced`, `skillCheckResult`.

### Шаг 4. OpenAPI оформление
- `paths` со всеми маршрутами, параметры (`questId`, `nodeId`, `choiceId`).  
- Ссылки на `shared/common` для ответов/безопасности.  
- `security`: `BearerAuth`; для internal/ admin — `ServiceToken`.  
- Примеры: запуск квеста, выбор ветки, skill check.  
- Схемы вынести в `components`, указать enums (QuestState, SkillCheckType).

### Шаг 5. Проверки
- `scripts/validate-swagger.ps1 -ApiDirectory API-SWAGGER/api/v1/gameplay/quests/`.  
- Убедиться, что файл ≤ 400 строк, README обновлён.  
- Обновить `brain-mapping.yaml`, документ `.BRAIN`, README `gameplay/quests`.

---

## 🔍 Критерии приемки
1. `info.x-microservice` выставлен (`gameplay-service`, 8083, `gameplay`).  
2. Все публичные пути под `/api/v1/gameplay/quests`.  
3. Поддержаны state machine, диалоги, skill checks, rewards, history.  
4. WebSocket события описаны для realtime UI.  
5. Ошибки используют общую модель `Error`.  
6. Примеры покрывают ключевые сценарии (start/progress/choice/skill-check).  
7. Валидаторы проходят без ошибок.  
8. Обновлены brain-mapping и `.BRAIN` документ.  
9. README каталога содержит описание API.  
10. Ограничения (конкурентные обновления, idempotency) описаны в `x-notes`.  
11. Internal endpoints защищены `ServiceToken`.

---

## FAQ
- **Как обрабатывать отказ skill check?** Возвращается результат + последствия, описать в `SkillCheckResult`.  
- **Можно ли повторить квест?** Через reset endpoint (admin) либо флаг `repeatable`.  
- **Как сохраняется прогресс?** Через сохранение `QuestInstance` (см. save-system).  
- **Поддерживается кооператив?** Указать в описании, что party-события идут через `party-sync`.  
- **Нужны ли analytics hooks?** Да, включить `x-analytics` раздел (event names).

---

**Источник:** `.BRAIN/05-technical/backend/quest-engine-backend.md` (v1.0.0, ready)

