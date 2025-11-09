# Task ID: API-TASK-167
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** queued
**Создано:** 2025-11-07 11:46 | **Создатель:** AI Agent | **Зависимости:** API-TASK-138

---

## 📋 Описание

Создать API для стартового контента (24 документа). Origin stories, quest-class, quest-faction, quest-main.

---

## 📚 Источники (24 документа)

**Origin stories (1):**
- origin-solo-military-veteran.md

**Class quests (5):**
- quest-class-fixer-2035-network-builder.md
- quest-class-netrunner-2011-netwatch-signal.md
- quest-class-nomad-2055-clan-unification.md
- quest-class-rockerboy-2077-final-stand.md
- quest-class-techie-2025-repair-grid.md

**Faction quests (2):**
- quest-faction-arasaka-2055-blackwall-breach.md
- quest-faction-valentinos-honor-2000s.md

**Main quests периодов (14):**
- quest-main-2023 до quest-main-2093 (по разным периодам)
- Includes: shattered-city, rebuild-protocol, free-city-charter, red-dawn и др.

**Merchant defense (1):**
- quest-merchant-defense-002.md

**Side special (1):**
- quest-side-2075-reality-artifact.md, quest-side-2088-archive-expedition.md

---

## 📁 Целевая структура

```
api/v1/narrative/start-content/
├── origin-stories.yaml
├── class-quests.yaml
├── faction-quests.yaml
└── main-quests-periods.yaml
```

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** narrative-service  
**Порт:** 8087  
**API пути:** /api/v1/narrative/start-content/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** narrative  
**Путь:** modules/narrative/start-content  
**State Store:** useNarrativeStore (originStory, classQuests, startQuests)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- Card, QuestCard, OriginCard, ClassBadge

**Готовые формы (@shared/forms):**
- OriginSelectionForm, QuestAcceptForm

**Layouts (@shared/layouts):**
- AuthLayout (для origin selection), GameLayout

**Хуки (@shared/hooks):**
- useCharacter (для class quests)

---

## ✅ Endpoints

1. **GET /api/v1/narrative/start-content/origins** - Origin stories
2. **GET /api/v1/narrative/start-content/class-quests** - Классовые квесты
3. **GET /api/v1/narrative/start-content/faction-quests** - Фракционные
4. **GET /api/v1/narrative/start-content/main-periods** - Main quests по периодам

**Models:** OriginStory, ClassQuest, PeriodQuest

---

**Источники:** 24 start-content документа

