# Task ID: API-TASK-108
**Тип:** API Generation
**Приоритет:** КРИТИЧЕСКИЙ (BACKEND)
**Статус:** queued
**Создано:** 2025-11-07 05:30
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-106 (session-management.yaml)

---

## 📋 Краткое описание

Создать API для системы matchmaking (подбор игроков).

**Что нужно сделать:** Создать API для Matchmaking System (queue system, match criteria, party formation, team balancing, MMR/ELO rating).

---

## 🎯 Цель задания

Создать API для Matchmaking (КРИТИЧЕСКИЙ):
- **Queue system:** Очереди для разных активностей (PvP, raids, dungeons)
- **Типы активностей:**
  - PvP: Arena 3v3, 5v5, 10v10
  - PvE: Raids (10/25 players), Dungeons (5 players)
  - Extraction Zones (4-6 players)
- **Match criteria:**
  - Level range
  - Role (tank, dps, healer, support)
  - Rating (MMR/ELO)
  - Region/language
- **Party formation:** Автоматическое создание групп
- **Team balancing:** Баланс сил команд
- **MMR/ELO:** Рейтинговая система
- **Cross-server:** Объединение серверов
- **Queue time:** Минимизация времени ожидания

**КРИТИЧЕСКИ ВАЖНО:** Подбор игроков для групповых активностей! (1000+ строк документа)

---

## 📚 Источники информации

**Путь:** `.BRAIN/05-technical/backend/matchmaking-system.md`
**Версия:** v1.0.0
**Статус:** approved (ready)

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/technical/matchmaking.yaml`

---

## ✅ Endpoints

1. **POST `/api/v1/technical/matchmaking/join-queue`** - Войти в очередь
2. **POST `/api/v1/technical/matchmaking/leave-queue`** - Покинуть очередь
3. **GET `/api/v1/technical/matchmaking/queue-status`** - Статус очереди
4. **POST `/api/v1/technical/matchmaking/accept-match`** - Принять матч
5. **WebSocket `/ws/matchmaking/updates`** - Real-time обновления

---

**История:** 2025-11-07 05:30 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

