# Task ID: API-TASK-158
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:20 | **Создатель:** AI Agent | **Зависимости:** none

---

## 📋 Описание

Создать API для детализации социальных механик (22 документа). Mentorship, NPC hiring, Player orders детализации.

---

## 📚 Источники (22 документа)

**Mentorship (6):**
- mentorship-types.md, mentorship-mechanics.md, mentorship-abilities.md
- mentorship-relationships.md, mentorship-special.md, mentorship-world-impact.md

**NPC Hiring (8):**
- npc-hiring-types.md, npc-hiring-process.md, npc-hiring-management.md, npc-hiring-effectiveness.md
- npc-hiring-limits.md, npc-hiring-economy.md, npc-hiring-advanced.md, npc-hiring-world-impact.md

**Player Orders (8):**
- player-orders-types.md, player-orders-creation.md, player-orders-execution.md, player-orders-via-npc.md
- player-orders-economy.md, player-orders-reputation.md, player-orders-advanced.md, player-orders-world-impact.md

---

## 📁 Целевая структура

```
api/v1/social/
├── mentorship/
│   ├── mentorship-types.yaml
│   ├── mentorship-mechanics.yaml
│   └── mentorship-abilities.yaml
├── npc-hiring/
│   ├── npc-hiring-types.yaml
│   ├── npc-hiring-process.yaml
│   └── npc-hiring-management.yaml
└── player-orders/
    ├── player-orders-types.yaml
    ├── player-orders-creation.yaml
    └── player-orders-execution.yaml
```

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** social-service  
**Порт:** 8084  
**API пути:** /api/v1/social/mentorship/*, /api/v1/social/npc-hiring/*, /api/v1/social/player-orders/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** social  
**Путь:** modules/social/  
**State Store:** useSocialStore (mentorships, hiredNpcs, playerOrders)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- Card, NPCCard, MentorCard, OrderCard, AbilityDisplay

**Готовые формы (@shared/forms):**
- MentorshipForm, NpcHiringForm, OrderCreationForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useDebounce
- useRealtime (для order status)

---

## ✅ Задача

Создать детальные API для социальных механик, разбить по логическим файлам (не более 400 строк каждый).

**Models:** Mentorship, NPCHire, PlayerOrder, SocialImpact

---

**Источники:** 22 социальных документа

