# Task ID: API-TASK-145
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** queued
**Создано:** 2025-11-07 10:52 | **Создатель:** AI Agent | **Зависимости:** API-TASK-138

---

## 📋 Описание

Создать API для Faction Quests (9 JSON файлов). Фракционные квесты с глубокими ветвлениями.

---

## 📚 Источники (9 документов)

- `quests-FACTION-NCPD-MAXTAC.json` (v3.0.0) - 2 квеста, 12 концовок
- `quests-FACTION-ARASAKA.json` (v3.0.0) - корпо-интриги
- `quests-FACTION-GANGS.json` (v3.0.0) - 6th Street, Voodoo Boys
- `quests-FACTION-NOMADS-REGIONS.json` (v3.0.0) - Aldecaldos, Pacifica
- `quests-FACTION-MILITECH-BIOTECHNICA.json` (v3.0.0) - корпо-квесты
- `quests-FACTION-VALENTINOS-MAELSTROM.json` (v3.0.0) - культурные квесты
- `quests-FACTION-FIXERS-RIPPERS.json` (v3.0.0) - Роуг, Риппердоки
- `quests-FACTION-TRAUMA-NETRUNNERS.json` (v3.0.0) - Trauma Team, Бартмосс
- `quests-FACTION-MEDIA-POLITICS.json` (v3.0.0) - журналистика, выборы
- `quests-FACTION-ANIMALS-MOX-WRAITHS.json` (v3.0.0) - малые банды

---

## 📁 Целевая структура

```
api/v1/narrative/faction-quests/
├── faction-ncpd-maxtac.yaml
├── faction-arasaka.yaml
├── faction-gangs.yaml
├── faction-nomads.yaml
├── faction-corpo.yaml
├── faction-cultural.yaml
├── faction-specialists.yaml
├── faction-tech.yaml
├── faction-politics.yaml
└── faction-minor-gangs.yaml
```

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** narrative-service  
**Порт:** 8087  
**API пути:** /api/v1/narrative/faction-quests/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** narrative  
**Путь:** modules/narrative/faction-quests  
**State Store:** useNarrativeStore (factionQuests, factionReputation, endings)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- QuestCard, FactionBadge, DialogueBox, EndingPreview (12+ endings)

**Готовые формы (@shared/forms):**
- DialogueChoiceForm, FactionChoiceForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useDebounce (для фильтра по фракциям)
- useRealtime

---

## ✅ Endpoints

1. **GET /api/v1/narrative/faction-quests** - Список по фракциям
2. **GET /api/v1/narrative/faction-quests/{quest_id}** - Детали квеста
3. **GET /api/v1/narrative/faction-quests/{quest_id}/branches** - Ветвления

**Models:** FactionQuest, QuestBranch, QuestEnding (12+ endings)

---

**Источники:** 9 faction quests JSON файлов

