# Task ID: API-TASK-199
**Тип:** API Generation | **Приоритет:** критический | **Статус:** completed
**Создано:** 2025-11-07 21:35 | **Завершено:** 2025-11-07 22:05 | **Исполнитель:** @АПИТАСК.MD | **Зависимости:** API-TASK-198

---

## 📋 Описание

Разработать спецификацию `session-management/reconnection-monitoring` для быстрого переподключения и мониторинга стабильности сессий.

---

## ✅ Сделано

- Добавлен `api/v1/technical/session-management/reconnection-monitoring.yaml` (<300 строк) с REST API для reconnect и мониторинга disconnect rate
- Описаны токены reconnect (окно 5 минут, до 3 попыток), история disconnect событий, нестабильные игроки, диагностика
- Добавлены события `session.disconnect/reconnect/instability`, интеграции с incident-service, telemetry и realtime сервисами

---

## 🔗 Связанные файлы

- `api/v1/technical/session-management/reconnection-monitoring.yaml`
- `.BRAIN/05-technical/backend/session-management/part2-reconnection-monitoring.md`

---

**Статус:** ✅ Завершено. API готово для backend/frontend реализации.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

