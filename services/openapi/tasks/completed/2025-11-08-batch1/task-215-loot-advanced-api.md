# Task ID: API-TASK-215
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** completed
**Создано:** 2025-11-08 01:52 | **Завершено:** 2025-11-08 06:45 | **Исполнитель:** @АПИТАСК.MD | **Зависимости:** API-TASK-129, API-TASK-214

---

## 📋 Описание

Расширить API системы лута, добавив поддержку продвинутых механик Part 2 (`loot-advanced`).

---

## ✅ Сделано

- Подготовлены `api/v1/loot/loot-advanced.yaml` (330 строк), `loot-advanced-components.yaml` (296 строк) и `loot-advanced-examples.yaml` (46 строк) с эндпоинтами/событиями для Need/Greed роллов, smart loot, boss loot, bad luck protection и дубликат-проверок
- Описаны модели `LootDrop`, `LootRoll`, `RollParticipant`, `SmartLootSetting`, `BossLootInfo`, `BadLuckProtection`, события realtime и ошибки `LootAdvancedError`
- Добавлены примеры запуска ролла, отправки ставки, распределения босcового лута и обновления smart loot настроек

---

## 🔗 Связанные файлы

- `api/v1/loot/loot-advanced.yaml`
- `api/v1/loot/loot-advanced-components.yaml`
- `api/v1/loot/loot-advanced-examples.yaml`
- `.BRAIN/05-technical/backend/loot-system/part2-advanced-loot.md`

---

**Статус:** ✅ Завершено. API готово для backend/frontend реализации.


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

