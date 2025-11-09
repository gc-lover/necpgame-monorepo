# Task ID: API-TASK-074
**Тип:** API Generation
**Приоритет:** критический
**Статус:** queued
**Создано:** 2025-11-07 02:05
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-070 (world-state.yaml)

---

## 📋 Краткое описание

Создать API для технической системы Global State (Event Sourcing).

**Что нужно сделать:** Создать техническую API для Event Sourcing системы: Event Store, State Management, синхронизация MMORPG, аудит, time travel.

---

## 🎯 Цель задания

Создать техническую API для Global State System:
- Event Sourcing: регистрация ВСЕХ событий
- Event Store: хранение полной истории
- State Reconstruction: восстановление состояния
- Синхронизация: MMORPG real-time updates
- Аудит: полная история действий
- Time Travel: откат состояния
- Архитектура: Event Bus (Kafka/RabbitMQ), Event Store, State Store, Cache

**Критически важно:** Техническая основа всей игры!

---

## 📚 Источники информации

**Путь:** `.BRAIN/05-technical/global-state-system.md`
**Версия:** v1.0.0
**Статус:** approved (критический)

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/technical/global-state.yaml`

**ВАЖНО:** Огромный технический файл (2400+ строк). ОБЯЗАТЕЛЬНО разбить:
- global-state.yaml - основные endpoints
- global-state-events.yaml - event sourcing
- global-state-sync.yaml - синхронизация

---

## ✅ Endpoints

1. **POST `/api/v1/technical/global-state/events`** - Зарегистрировать событие
2. **GET `/api/v1/technical/global-state/events/{event_id}`** - Получить событие
3. **GET `/api/v1/technical/global-state/reconstruct`** - Восстановить состояние
4. **GET `/api/v1/technical/global-state/sync`** - Синхронизация

---

**История:** 2025-11-07 02:05 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` с актуальными данными:
  - name: admin-service
  - port: 8088
  - domain: technical
  - base-path: /api/v1/technical
  - package: com.necpgame.adminservice
- В секции `servers` используй gateway:
  - https://api.necp.game/v1/technical
  - http://localhost:8080/api/v1/technical
- WebSocket маршруты публикуй только через wss://api.necp.game/v1/technical/...

