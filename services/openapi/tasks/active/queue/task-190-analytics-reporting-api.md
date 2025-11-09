# Task ID: API-TASK-190
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** queued
**Создано:** 2025-11-07 19:45 | **Создатель:** @АПИТАСК.MD (проактивно) | **Зависимости:** none

---

## 📋 Описание

Создать API для Analytics & Reporting - бизнес-аналитика, user behavior, conversion metrics, reports generation.

---

## 🎯 Обоснование

Critical для business intelligence:
- User behavior analytics
- Conversion tracking
- Revenue analytics
- Player retention metrics
- Custom reports generation

---

## 📁 Целевой файл

**Файл:** `api/v1/technical/analytics-reporting.yaml`

---

## ✅ Endpoints

1. **GET /technical/analytics/users** - User analytics
2. **GET /technical/analytics/revenue** - Revenue metrics
3. **GET /technical/analytics/retention** - Retention rates
4. **POST /technical/analytics/reports/generate** - Generate custom report

---

**Создаю для business intelligence!**


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

