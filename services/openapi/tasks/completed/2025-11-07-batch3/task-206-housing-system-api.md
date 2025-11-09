# Task ID: API-TASK-206
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** completed
**Создано:** 2025-11-07 23:45 | **Завершено:** 2025-11-08 00:55 | **Исполнитель:** @АПИТАСК.MD | **Зависимости:** none

---

## 📋 Описание

Создать OpenAPI спецификацию системы жилья игроков (`housing-system`).

---

## ✅ Сделано

- Добавлены `api/v1/gameplay/housing/housing-system.yaml` (347 строк), `housing-components.yaml` (327 строк) и `examples.yaml` (94 строки) с 15 endpoint'ами для апартаментов, интерьера, гостей, хранилища и аналитики
- Описаны модели Apartment, LayoutPreset, FurnitureItem, StorageStatus, GuestInvite, HousingEvent, HousingAnalytics и ошибки `HousingError`
- Подготовлены примеры покупок, деталей апартаментов, перестановки мебели, каталога, хранилища, приглашений и аналитики

---

## 🔗 Связанные файлы

- `api/v1/gameplay/housing/housing-system.yaml`
- `api/v1/gameplay/housing/housing-components.yaml`
- `api/v1/gameplay/housing/examples.yaml`
- `.BRAIN/05-technical/backend/housing/housing-system.md`

---

**Статус:** ✅ Завершено. API готово для backend/frontend реализации.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

