# Task ID: API-TASK-204
**Тип:** API Generation | **Приоритет:** критический | **Статус:** completed
**Создано:** 2025-11-07 23:05 | **Завершено:** 2025-11-07 23:55 | **Исполнитель:** @АПИТАСК.MD | **Зависимости:** none

---

## 📋 Описание

Создать OpenAPI спецификацию системы клановых войн, территорий и осад (`clan-war-system`).

---

## ✅ Сделано

- Добавлены `api/v1/gameplay/clans/clan-war-system.yaml` (376 строк), `clan-war-components.yaml` (346 строк) и `examples.yaml` (78 строк) с полной документацией 15 endpoint'ов
- Описаны модели WarDeclaration, ClanWar, SiegePlan, Territory, WarAnalytics, Penalty, Broadcast, включая ограничения, анти-абуз и интеграции с economy/notification/realtime
- Подготовлены примеры для объявления войны, плана осады, карты территорий, аналитики и broadcast-сообщений

---

## 🔗 Связанные файлы

- `api/v1/gameplay/clans/clan-war-system.yaml`
- `api/v1/gameplay/clans/clan-war-components.yaml`
- `api/v1/gameplay/clans/examples.yaml`
- `.BRAIN/05-technical/backend/clan-war/clan-war-system.md`

---

**Статус:** ✅ Завершено. API готово для backend/frontend реализации.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

