# Task ID: API-TASK-207
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** completed
**Создано:** 2025-11-07 23:58 | **Завершено:** 2025-11-08 01:20 | **Исполнитель:** @АПИТАСК.MD | **Зависимости:** none

---

## 📋 Описание

Создать OpenAPI спецификацию ядра Battle Pass (`battle-pass-core`).

---

## ✅ Сделано

- Добавлены файлы `api/v1/gameplay/battle-pass/battle-pass-core.yaml` (336 строк), `battle-pass-components.yaml` (322 строки) и `examples.yaml` (87 строк) с 15 endpoint'ами для сезонов, прогресса, премиума, XP-источников и аналитики
- Описаны модели BattlePassSeason, PlayerBattlePassProgress, XpGrant, PremiumPurchase, LevelSkip, Analytics и ошибки `BattlePassError`
- Подготовлены примеры создания сезона, начисления XP, покупки премиума и отчёта аналитики

---

## 🔗 Связанные файлы

- `api/v1/gameplay/battle-pass/battle-pass-core.yaml`
- `api/v1/gameplay/battle-pass/battle-pass-components.yaml`
- `api/v1/gameplay/battle-pass/examples.yaml`
- `.BRAIN/05-technical/backend/battle-pass/part1-core-progression.md`

---

**Статус:** ✅ Завершено. API готово для backend/frontend реализации.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

