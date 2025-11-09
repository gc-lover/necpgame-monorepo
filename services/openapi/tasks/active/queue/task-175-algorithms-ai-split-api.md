# Task ID: API-TASK-175
**Тип:** API Generation | **Приоритет:** низкий | **Статус:** queued
**Создано:** 2025-11-07 12:54 | **Создатель:** AI Agent | **Зависимости:** API-TASK-164

---

## 📋 Описание

Создать API для алгоритмов и AI систем (5 split документов). Romance algorithms (3), NPC personality (2).

---

## 📚 Источники (5 документов)

**Romance Algorithms (3):**
- algorithms/romance/romance-dialogue.md
- algorithms/romance/romance-relationship.md
- algorithms/romance/romance-triggers.md

**AI Systems (2):**
- ai-systems/npc-personality/personality-engine.md
- ai-systems/npc-personality/romance-ai.md

---

## 📁 Целевой файл

`api/v1/internal/algorithms/romance-ai-algorithms.yaml`

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** social-service (internal algorithms)  
**Порт:** 8084  
**API пути:** /api/v1/internal/algorithms/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** N/A (это внутренние алгоритмы backend, не public API)

**Примечание:** Это internal service-to-service API для AI алгоритмов romance system. Не требует frontend реализации.

---

## ✅ Endpoints

Это внутренние алгоритмы, не требуют public API endpoints. Только для internal services.

---

**Источников:** 5 algorithms/AI документов

