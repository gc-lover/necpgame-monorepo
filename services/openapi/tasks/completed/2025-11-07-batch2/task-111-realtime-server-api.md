# Task ID: API-TASK-111
**Тип:** API Generation
**Приоритет:** КРИТИЧЕСКИЙ (BACKEND)
**Статус:** queued
**Создано:** 2025-11-07 05:55
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-106 (session-management.yaml)

---

## 📋 Краткое описание

Создать API для архитектуры real-time сервера.

**Что нужно сделать:** Создать API для real-time геймплея (game server instances, zone/instance management, player position sync, network protocol, lag compensation).

---

## 🎯 Цель задания

Создать API для Real-Time Server (КРИТИЧЕСКИЙ):
- **Game Server Instances:** Масштабируемые инстансы
- **Zone/Instance Management:** Управление зонами
- **Player Position Sync:** Синхронизация позиций (30-60 FPS)
- **Network Protocol:** TCP + WebSocket
- **Lag Compensation:**
  - Client Prediction (локальное предсказание)
  - Server Reconciliation (коррекция от сервера)
  - Entity Interpolation (сглаживание)
- **Interest Management:** Area of Interest (видимость других игроков)
- **Bandwidth Optimization:**
  - Delta Compression (только изменения)
  - Priority System (важность данных)
  - Update Rate Scaling (по расстоянию)
- **WebSocket:** Real-time updates

**КРИТИЧЕСКИ ВАЖНО:** Основа для MMORPG shooter геймплея! (1000+ строк документа)

---

## 📚 Источники информации

**Путь:** `.BRAIN/05-technical/backend/realtime-server-architecture.md`
**Версия:** v1.0.0
**Статус:** approved (ready)

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/technical/realtime-server.yaml`

**ВАЖНО:** Большая система. ОБЯЗАТЕЛЬНО разбить:
- realtime-server-core.yaml - основные endpoints
- realtime-server-sync.yaml - синхронизация
- realtime-server-zones.yaml - управление зонами
- realtime-server-ws.yaml - WebSocket протокол

---

## ✅ Endpoints

1. **POST `/api/v1/technical/realtime/join-zone`** - Войти в зону
2. **POST `/api/v1/technical/realtime/update-position`** - Обновить позицию
3. **GET `/api/v1/technical/realtime/zone-players`** - Игроки в зоне
4. **WebSocket `/ws/realtime/{zone_id}`** - Real-time синхронизация

---

**История:** 2025-11-07 05:55 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

