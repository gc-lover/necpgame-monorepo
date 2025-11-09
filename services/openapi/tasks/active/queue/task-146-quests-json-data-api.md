# Task ID: API-TASK-146
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** queued
**Создано:** 2025-11-07 10:54 | **Создатель:** AI Agent | **Зависимости:** API-TASK-138

---

## 📋 Описание

Создать API для Quest JSON Data (20 JSON файлов). Квестовые данные из mvp-data-json.

---

## 📚 Источники (20 документов)

**Expanded (6):**
- quests-expanded-2020-2030.json
- quests-expanded-2030-2045.json
- quests-expanded-2045-2060.json
- quests-expanded-2060-2077.json
- quests-expanded-2078-2090.json
- quests-expanded-2090-2093.json

**Additional (7):**
- quests-2020-2030-ADDITIONAL.json (4 квеста)
- quests-2020-2030-ADDITIONAL-2.json (6 квестов)
- quests-2030-2045-ADDITIONAL.json (15 квестов)
- quests-2045-2060-ADDITIONAL.json (15 квестов)
- quests-2060-2077-ADDITIONAL.json (15 квестов)
- quests-2078-2090-ADDITIONAL.json (15 квестов)
- quests-2090-2093-ADDITIONAL.json (10 квестов)

**Всего:** ~100+ квестов с полными структурами

---

## 📁 Целевая структура

```
api/v1/narrative/quests-data/
├── quests-2020-2030.yaml
├── quests-2030-2045.yaml
├── quests-2045-2060.yaml
├── quests-2060-2077.yaml
├── quests-2078-2090.yaml
└── quests-2090-2093.yaml
```

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** narrative-service  
**Порт:** 8087  
**API пути:** /api/v1/narrative/quests-data/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** narrative  
**Путь:** modules/narrative/quests  
**State Store:** useNarrativeStore (allQuests, questsByPeriod)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- QuestCard, PeriodFilter, DialoguePreview, LootPreview

**Готовые формы (@shared/forms):**
- QuestAcceptForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useDebounce (для поиска квестов по периоду)

---

## ✅ Задача

Преобразовать JSON квестовые данные в OpenAPI спецификацию. Использовать единую структуру для всех квестов.

**Models:** Quest, DialogueNode, LootTable, RandomEvent, ReputationFormula

---

**Источники:** 13 JSON файлов с квестами

