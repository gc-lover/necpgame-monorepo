# Task ID: API-TASK-144
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** queued
**Создано:** 2025-11-07 10:50 | **Создатель:** AI Agent | **Зависимости:** API-TASK-138

---

## 📋 Описание

Создать API для Side Quests EXPANDED (6 периодов). Расширенные квесты с диалогами и skill-checks.

---

## 📚 Источники (6 документов)

- `.BRAIN/04-narrative/quests/side/side-quests-2020-2030-EXPANDED.md` (v2.0.0)
- `.BRAIN/04-narrative/quests/side/side-quests-2030-2045-EXPANDED.md` (v2.0.0)
- `.BRAIN/04-narrative/quests/side/side-quests-2045-2060-EXPANDED.md` (v2.0.0)
- `.BRAIN/04-narrative/quests/side/side-quests-2060-2077-EXPANDED.md` (v2.0.0)
- `.BRAIN/04-narrative/quests/side/side-quests-2078-2090-EXPANDED.md` (v2.0.0)
- `.BRAIN/04-narrative/quests/side/side-quests-2090-2093-EXPANDED.md` (v2.0.0)

**Общие элементы:** Диалоговые деревья (20-30 узлов), skill-checks, лут-таблицы, события перемещений, репутационные формулы.

---

## 📁 Целевая структура

```
api/v1/narrative/side-quests/
├── side-quests-2020-2030.yaml
├── side-quests-2030-2045.yaml
├── side-quests-2045-2060.yaml
├── side-quests-2060-2077.yaml
├── side-quests-2078-2090.yaml
└── side-quests-2090-2093.yaml
```

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** narrative-service  
**Порт:** 8087  
**API пути:** /api/v1/narrative/side-quests/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** narrative  
**Путь:** modules/narrative/side-quests  
**State Store:** useNarrativeStore (sideQuests, questsByPeriod)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- QuestCard, DialogueBox, ChoiceButton, PeriodFilter

**Готовые формы (@shared/forms):**
- DialogueChoiceForm, QuestAcceptForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useDebounce (для поиска квестов)
- useRealtime

---

## ✅ Endpoints (унифицированные)

1. **GET /api/v1/narrative/side-quests** - Список квестов по периоду
2. **GET /api/v1/narrative/side-quests/{quest_id}** - Детали квеста
3. **GET /api/v1/narrative/side-quests/{quest_id}/dialogue-tree** - Dialogue tree

**Models:** SideQuest, QuestDialogueTree, QuestReward, QuestEvent

---

**Источники:** 6 side-quests EXPANDED документов

