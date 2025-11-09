# Task ID: API-TASK-189
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** queued
**Создано:** 2025-11-07 19:40 | **Создатель:** @АПИТАСК.MD (проактивно) | **Зависимости:** none

---

## 📋 Описание

Создать API для Deployment Management - управление deployments, rollbacks, versioning, blue-green deployment.

---

## 🎯 Обоснование

Production-critical для DevOps:
- Deployment orchestration
- Version management
- Rollback capabilities
- Blue-green deployment
- Feature flags

---

## 📁 Целевой файл

**Файл:** `api/v1/technical/deployment-management.yaml`

---

## ✅ Endpoints

1. **GET /technical/deployments** - Deployment history
2. **POST /technical/deployments/deploy** - Trigger deployment
3. **POST /technical/deployments/{id}/rollback** - Rollback
4. **GET /technical/deployments/versions** - Version info

---

**Создаю для DevOps готовности!**


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

