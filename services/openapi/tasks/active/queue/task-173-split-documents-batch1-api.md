# Task ID: API-TASK-173
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 12:50 | **Создатель:** AI Agent | **Зависимости:** API-TASK-163, API-TASK-169

---

## 📋 Описание

Создать API для SPLIT документов - технические системы (35 документов). Global State (5), UI Systems (8), Player Market (4), Auction House (3), World State (3), Data Models (3), MVP Endpoints (4), Endpoints Reference (2), AI Systems (2), Backend Player (1).

---

## 📚 Источники (35 split документов)

**Global State Split (5):**
- global-state/global-state-core.md
- global-state/global-state-events.md
- global-state/global-state-management.md
- global-state/global-state-operations.md
- global-state/global-state-sync.md

**UI Split (8):**
- ui/character-creation/creation-flow.md, appearance-editor.md
- ui/game-start/login-screen.md, server-selection.md, character-select.md
- ui/main-game/ui-features.md, ui-hud-core.md, ui-system.md

**Player Market Split (4):**
- player-market/player-market-core.md, player-market-api.md
- player-market/player-market-analytics.md, player-market-database.md

**Auction House Split (3):**
- auction-house/auction-database.md, auction-mechanics.md, auction-operations.md

**World State Split (3):**
- world-state/player-impact-mechanics.md, player-impact-persistence.md, player-impact-systems.md

**Data Models Split (3):**
- api-specs/data-models/core-models.md, gameplay-models.md, social-models.md

**MVP Endpoints Split (4):**
- mvp-endpoints/auth-endpoints.md, content-endpoints.md, gameplay-endpoints.md, system-endpoints.md

**Endpoints Reference (2):**
- endpoints-reference/auth-social-endpoints.md, gameplay-endpoints.md

**AI Systems Split (2):**
- ai-systems/npc-personality/personality-engine.md, romance-ai.md

**Backend Player (2):**
- backend/player-character/character-crud.md, character-systems.md

---

## 📁 Целевая структура

Интегрировать с существующими API или создать дополнительные endpoints.

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** Разные сервисы (split документы относятся к разным доменам)
- Global State → gameplay-service (8083)
- UI Systems → character-service (8082)
- Player Market / Auction → economy-service (8085)
- World State → world-service (8086)
- MVP Endpoints → разные сервисы
- AI Systems → narrative-service (8087)

**Порт:** Зависит от домена  
**API пути:** Разные (см. выше)

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** Разные модули (зависит от split документа)

**Примечание:** Это интеграционные endpoints для расширения существующих API. Определить конкретный микросервис и модуль для каждого split документа отдельно.

---

```
api/v1/technical/
├── global-state-extended.yaml
├── ui-systems.yaml
└── split-systems.yaml

api/v1/economy/
├── player-market-extended.yaml
└── auction-house-extended.yaml

api/v1/world/
└── world-state-extended.yaml
```

---

## ✅ Задача

Интегрировать split документы с уже существующими API задачами или создать дополнительные секции в соответствующих API файлах.

---

**Источников:** 35 split документов

