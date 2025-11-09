# Task ID: API-TASK-205
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** completed
**Создано:** 2025-11-07 23:25 | **Завершено:** 2025-11-08 00:20 | **Исполнитель:** @АПИТАСК.MD | **Зависимости:** none

---

## 📋 Описание

Создать OpenAPI спецификацию системы объявлений/новостей (`announcement-system`).

---

## ✅ Сделано

- Добавлены файлы `api/v1/admin/announcements/announcement-system.yaml` (357 строк), `announcement-components.yaml` (251 строк) и `examples.yaml` (114 строк) с 15 endpoint'ами для черновиков, расписаний, каналов, локализаций, аналитики и emergency сообщений
- Определены схемы Announcement, ChannelConfig, AudienceRules, ScheduleRequest, AnalyticsResponse, HistoryEntry и др. с валидациями и анти-абуз ограничениями
- Подготовлены примеры черновиков, расписания, превью, аналитики, emergency предупреждения и журналов версий для LiveOps UI и QA

---

## 🔗 Связанные файлы

- `api/v1/admin/announcements/announcement-system.yaml`
- `api/v1/admin/announcements/announcement-components.yaml`
- `api/v1/admin/announcements/examples.yaml`
- `.BRAIN/05-technical/backend/announcement/announcement-system.md`

---

**Статус:** ✅ Завершено. API готово для backend/frontend реализации.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

