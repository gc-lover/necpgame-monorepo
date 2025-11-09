# Task ID: API-TASK-165
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** queued
**Создано:** 2025-11-07 11:42 | **Создатель:** AI Agent | **Зависимости:** API-TASK-138

---

## 📋 Описание

Создать API для конкретных SQ/MQ квестов (36 документов). Детальные side quests и main quest nodes по периодам.

---

## 📚 Источники (36 документов)

**Main Quest Nodes MQ- (6):**
- MQ-2020-002, MQ-2030-001, MQ-2045-001, MQ-2060-001, MQ-2077-001, MQ-2078-001

**Side Quest SQ- периоды (30):**
- **2020s (6):** SQ-2020-001 до SQ-2020-006
- **2030s (4):** SQ-2030-001 до SQ-2030-004
- **2045s (5):** SQ-2045-001 до SQ-2045-005
- **2060s (5):** SQ-2060-001 до SQ-2060-005
- **2077 (5):** SQ-2077-001 до SQ-2077-005
- **2078s (5):** SQ-2078-001 до SQ-2078-005

Каждый квест с dialogue tree, skill checks, rewards.

---

## 📁 Целевая структура

```
api/v1/narrative/quests-specific/
├── main-quests-mq.yaml
└── side-quests-sq.yaml
```

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** narrative-service  
**Порт:** 8087  
**API пути:** /api/v1/narrative/quests-specific/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** narrative  
**Путь:** modules/narrative/quests  
**State Store:** useNarrativeStore (specificQuests, mqNodes, sqList)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- QuestCard, DialogueBox, ChoiceButton, SkillCheckDisplay

**Готовые формы (@shared/forms):**
- QuestAcceptForm, DialogueChoiceForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useDebounce (для поиска квестов)
- useRealtime (для quest progress)

---

## ✅ Endpoints

1. **GET /api/v1/narrative/quests-specific/sq** - Side quests SQ-XXXX
2. **GET /api/v1/narrative/quests-specific/mq** - Main nodes MQ-XXXX
3. **GET /api/v1/narrative/quests-specific/{quest_id}** - Детали

**Models:** SpecificQuest, QuestNode, QuestDialogue

---

**Источники:** 36 SQ/MQ конкретных квестов

