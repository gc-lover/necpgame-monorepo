# Task ID: API-TASK-182
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** queued
**Создано:** 2025-11-07 19:00 | **Создатель:** @АПИТАСК.MD | **Зависимости:** none

---

## 📋 Описание

Создать Management API для API Gateway - управление routing, load balancing, health checks.

---

## 📚 Источники

**Infrastructure:**
- `05-technical/infrastructure/api-gateway-architecture.md` - API Gateway architecture

---

## 🎯 Целевой файл

**Файл:** `api/v1/technical/api-gateway-management.yaml`

---

## ✅ Endpoints

1. **GET /technical/gateway/routes** - Список маршрутов
2. **POST /technical/gateway/routes** - Добавить route
3. **GET /technical/gateway/health** - Health check всех сервисов
4. **GET /technical/gateway/metrics** - Gateway metrics
5. **POST /technical/gateway/cache/invalidate** - Очистить cache

---

**Выполняю немедленно!**


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

