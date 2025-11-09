# Task ID: API-TASK-176
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 12:56 | **Создатель:** AI Agent | **Зависимости:** API-TASK-138

---

## 📋 Описание

Создать API для нарративных split документов (5 документов). Narrative coherence phase1, backend integration, side quests split.

---

## 📚 Источники (5 документов)

**Narrative Coherence:**
- phase1-architecture.md
- phase6-documentation/dev-guides/backend-integration-complete.md

**Side Quests Split:**
- side-quests-2020-2030-part1.md
- side-quests-2020-2030-part2.md

**NPC Generator:**
- npc-generator-core.md
- npc-generator-templates.md

---

## 📁 Целевой файл

`api/v1/narrative/narrative-systems-extended.yaml`

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** narrative-service  
**Порт:** 8087  
**API пути:** /api/v1/narrative/systems/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** narrative  
**Путь:** modules/narrative/systems  
**State Store:** useNarrativeStore (narrativeCoherence, generatedNpcs, sideQuests)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- Card, NPCCard, QuestCard, SystemIndicator

**Готовые формы (@shared/forms):**
- DialogueChoiceForm, QuestAcceptForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useRealtime (для narrative coherence)
- useDebounce

---

## ✅ Endpoints

Интеграция с существующими narrative API.

---

**Источников:** 5 narrative split документов

