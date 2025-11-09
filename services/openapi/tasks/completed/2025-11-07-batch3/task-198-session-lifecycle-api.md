# Task ID: API-TASK-198
**Тип:** API Generation | **Приоритет:** критический | **Статус:** completed
**Создано:** 2025-11-07 21:05 | **Завершено:** 2025-11-07 21:45 | **Исполнитель:** @АПИТАСК.MD | **Зависимости:** API-TASK-106

---

## 📋 Описание

Создать детализированную спецификацию `session-management/lifecycle` (создание, heartbeat, AFK, concurrent sessions).

---

## ✅ Сделано

- Добавлен `api/v1/technical/session-management/lifecycle.yaml` с 9 endpoint'ами и state machine статусов
- Описаны сценарии heartbeat (30s SLA), AFK, force logout, concurrent session handling, метрики, policies
- Добавлены схемы событий `session.*`, ошибки (`SESSION_NOT_FOUND`, `HEARTBEAT_TOO_SOON`, `CONCURRENT_SESSION_ACTIVE`)

---

## 🔗 Связанные файлы

- `api/v1/technical/session-management/lifecycle.yaml`
- `.BRAIN/05-technical/backend/session-management/part1-lifecycle-heartbeat.md`

---

**Статус:** ✅ Завершено. API готово для backend/frontend реализации.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

