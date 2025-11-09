# Task ID: API-TASK-164
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:40 | **Создатель:** AI Agent | **Зависимости:** API-TASK-138

---

## 📋 Описание

Создать API для полной системы романтических отношений (23 документа). MEGA-ROMANCE-SYSTEM-1000 + все события + региональные NPC.

---

## 📚 Источники (23 документа)

**Основная система (3):**
- `MEGA-ROMANCE-SYSTEM-1000.md` - полная система на 1000 NPC
- `ROMANCE-EVENTS-INDEX-1000.md` - индекс всех событий
- `hanako-tanaka-tokyo.md` - пример полного романтического арка

**События по стадиям (9):**
- `events/01-meeting-events.md` - встреча
- `events/02-friendship-events.md` - дружба
- `events/03-flirting-events.md` - флирт
- `events/04-dating-events.md` - свидания
- `events/05-intimacy-events.md` - близость
- `events/06-conflict-events.md` - конфликты
- `events/07-reconciliation-events.md` - примирение
- `events/08-commitment-events.md` - обязательства
- `events/09-crisis-breakup-events.md` - кризис/расставание

**Региональные NPC (6):**
- `regions/africa-romance-events-100.md` (100 NPC)
- `regions/america-romance-events-150.md` (150 NPC)
- `regions/asia-romance-events-150.md` (150 NPC)
- `regions/cis-romance-events-100.md` (100 NPC)
- `regions/europe-romance-events-150.md` (150 NPC)
- `regions/middleeast-romance-events-100.md` (100 NPC)

**Техническая документация (5):**
- `romance-events-complete-library.md`
- `npc-profile-generator.md`
- `ROMANCE-SYSTEM-TECHNICAL-OVERVIEW.md`
- `romance-event-engine.md`
- `npc-personality-romance-ai.md`

**ВСЕГО: 1000+ романтических NPC, 1000+ событий**

---

## 📁 Целевая структура

```
api/v1/romance/
├── romance-system.yaml
├── romance-events.yaml
├── romance-npcs-regions.yaml
└── romance-engine.yaml
```

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** social-service  
**Порт:** 8084  
**API пути:** /api/v1/romance/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** social  
**Путь:** modules/social/romance  
**State Store:** useSocialStore (romances, romanceEvents, npcRelationships)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- NPCCard, RomanceCard, EventCard, RelationshipBar, HeartMeter

**Готовые формы (@shared/forms):**
- DialogueChoiceForm, RomanceActionForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useRealtime (для romance event triggers)
- useDebounce

---

## ✅ Endpoints

1. **GET /api/v1/romance/npcs** - Романтические NPC по региону
2. **GET /api/v1/romance/events** - События по стадии отношений
3. **POST /api/v1/romance/event/trigger** - Trigger романтическое событие
4. **GET /api/v1/romance/relationship/{npc_id}** - Статус отношений с NPC
5. **POST /api/v1/romance/action** - Романтическое действие

**Models:** RomanceNPC, RomanceEvent, RomanceRelationship, RomanceStage

---

**Источники:** 23 romance документа

