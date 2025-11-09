# Task ID: API-TASK-181
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 18:30 | **Создатель:** @АПИТАСК.MD | **Зависимости:** API-TASK-164

---

## 📋 Описание

Создать API для Romance Event Engine - продвинутый алгоритм генерации романтических событий. Filtering, weighting, scoring, selection.

---

## 📚 Источники (3 документа)

**Romance Event Engine:**
- `05-technical/algorithms/romance-event-engine/README.md` - Обзор
- `05-technical/algorithms/romance-event-engine/part1-filtering-weighting.md` - Filtering & Weighting
- `05-technical/algorithms/romance-event-engine/part2-scoring-selection.md` - Scoring & Selection

---

## 🎯 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/gameplay/social/romance-event-engine.yaml`
**API версия:** v1

---

## ✅ Что нужно сделать

### Endpoints:

1. **POST /gameplay/social/romance/events/generate** - Генерация романтических событий
2. **GET /gameplay/social/romance/events/available** - Доступные события
3. **POST /gameplay/social/romance/events/{event_id}/trigger** - Триггер события
4. **GET /gameplay/social/romance/algorithms/filters** - Активные фильтры
5. **GET /gameplay/social/romance/algorithms/weights** - Веса параметров

### Models:

- RomanceEventGenerationRequest
- RomanceEventGenerationResponse
- RomanceEventInfo
- FilterCriteria
- WeightingParameters
- ScoringResult

---

**ВНИМАНИЕ:** Выполняю немедленно как @АПИТАСК.MD!


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

