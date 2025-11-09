# Task ID: API-TASK-185
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** queued
**Создано:** 2025-11-07 19:15 | **Создатель:** @АПИТАСК.MD | **Зависимости:** none

---

## 📋 Описание

Создать API для Database Management - sharding, replication, migrations, backups.

---

## 📚 Источники

- `05-technical/infrastructure/database-architecture.md` - Database architecture

---

## 🎯 Целевой файл

**Файл:** `api/v1/technical/database-management.yaml`

---

## ✅ Endpoints

1. **GET /technical/database/status** - Database health & stats
2. **GET /technical/database/shards** - Sharding info
3. **POST /technical/database/migrations/run** - Run migration
4. **POST /technical/database/backup** - Trigger backup

---

**Выполняю!**


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

