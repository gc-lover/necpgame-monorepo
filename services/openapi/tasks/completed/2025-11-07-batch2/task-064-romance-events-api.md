# Task ID: API-TASK-064
**Тип:** API Generation
**Приоритет:** средний
**Статус:** queued
**Создано:** 2025-11-07 01:15
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-060 (relationships.yaml)

---

## 📋 Краткое описание

Создать API для системы романтических событий.

**Что нужно сделать:** Создать модульную систему романтических событий (10 категорий, 100+ событий) для динамических романтических отношений с NPC/игроками.

---

## 🎯 Цель задания

Создать API для romance events:
- 10 категорий: Meeting, Friendship, Flirting, Dating, Intimacy, Conflict, Reconciliation, Commitment, Crisis, Special
- Структура события: triggers, skill checks, outcomes, next events
- Стадии отношений: 0 (Stranger) → 100 (Soulmate)
- Процедурная генерация историй
- Региональное разнообразие (адаптация под локацию)
- Динамические конфликты (ссоры, ревность, разрывы)

---

## 📚 Источники информации

**Путь:** `.BRAIN/04-narrative/quests/romantic/romance-events-system.md`
**Статус:** ready

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/narrative/romance-events.yaml`

---

## ✅ Endpoints

1. **GET `/api/v1/narrative/romance-events`** - Все события
2. **GET `/api/v1/narrative/romance-events/available`** - Доступные события
3. **POST `/api/v1/narrative/romance-events/trigger`** - Запустить событие
4. **GET `/api/v1/narrative/romance-events/relationship-status`** - Статус отношений

---

**История:** 2025-11-07 01:15 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` с актуальными данными:
  - name: narrative-service
  - port: 8087
  - domain: narrative
  - base-path: /api/v1/narrative
  - package: com.necpgame.narrativeservice
- В секции `servers` используй gateway:
  - https://api.necp.game/v1/narrative
  - http://localhost:8080/api/v1/narrative
- WebSocket маршруты публикуй только через wss://api.necp.game/v1/narrative/...

