# Task ID: API-TASK-168
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:48 | **Создатель:** AI Agent | **Зависимости:** API-TASK-138

---

## 📋 Описание

Создать API для региональных и daily/weekly квестов (9 документов).

---

## 📚 Источники (9 документов)

**Daily/Weekly (2):**
- asia-daily-weekly.md
- europe-daily-weekly.md

**World Regional (7):**
- africa/west-africa-quests.md
- america/south-america-quests.md
- asia/east-asia-quests.md
- cis/russia-quests.md
- europe/western-europe-quests.md
- middle-east/gulf-quests.md
- oceania/oceania-quests.md

**+ Faction world quests:**
- arasaka-world-quests.md

---

## 📁 Целевой файл

```
api/v1/narrative/world-quests/
├── daily-weekly.yaml
└── regional-quests.yaml
```

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** narrative-service  
**Порт:** 8087  
**API пути:** /api/v1/narrative/world-quests/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** narrative  
**Путь:** modules/narrative/daily-quests  
**State Store:** useNarrativeStore (dailyQuests, weeklyQuests, regionalQuests)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- QuestCard, RegionBadge, Timer (reset countdown), RewardDisplay

**Готовые формы (@shared/forms):**
- QuestAcceptForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useRealtime (для daily/weekly reset)
- useDebounce (для фильтра по регионам)

---

## ✅ Endpoints

1. **GET /api/v1/narrative/world-quests/daily** - Daily quests по региону
2. **GET /api/v1/narrative/world-quests/weekly** - Weekly quests
3. **GET /api/v1/narrative/world-quests/regional** - Региональные квесты

**Models:** DailyQuest, WeeklyQuest, RegionalQuest

---

**Источники:** 9 world/regional quest документов

