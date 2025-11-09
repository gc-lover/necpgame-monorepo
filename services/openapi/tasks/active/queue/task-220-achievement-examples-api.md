# Task ID: API-TASK-220
**Тип:** API Generation
**Приоритет:** средний
**Статус:** queued
**Создано:** 2025-11-08 03:05
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-218, API-TASK-219

---

## 📋 Краткое описание

Подготовить примеры и вспомогательные API для реализации достижений: эталонные ответы, JSON payload, тестовые сценарии и демонстрационные endpoints.

**Что нужно сделать:** Создать `api/v1/achievements/examples/examples-api.yaml`, описав примерные запросы/ответы, вспомогательные endpoints для QA/Dev и документацию.

---

## 🎯 Цель задания

Обеспечить разработчиков и QA наглядными шаблонами для интеграции с Achievement Core/Tracking.

**Зачем это нужно:**
- Предоставить готовые payload для common cases (kill, quests, crafting)
- Покрыть edge cases (meta, hidden, streak achievements)
- Облегчить тестирование UI и LiveOps инструментов
- Синхронизировать примеры с core/tracking API (Tasks 218, 219)

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/backend/achievement/achievement-examples-api.md`
**Версия:** v1.0.0 (2025-11-07 01:59)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- JSON примеры для боевых, квестовых, социальный достижений
- Тестовые события, payload для tracking
- Структура API ответов (progress, unlock notifications)
- Sample WebSocket messages и cron сценарии
- Таблица наград и cosmetic IDs

### Дополнительные источники

- `.BRAIN/05-technical/backend/achievement/achievement-core.md`
- `.BRAIN/05-technical/backend/achievement/achievement-tracking.md`
- `.BRAIN/05-technical/backend/notification-system.md`
- `.BRAIN/05-technical/backend/analytics/analytics-reporting.md`

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-218-achievement-core-api.md`
- `API-SWAGGER/tasks/active/queue/task-219-achievement-tracking-api.md`
- `API-SWAGGER/tasks/active/queue/task-209-achievement-ui-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/achievements/examples/examples-api.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3 (documentation-oriented)

```
API-SWAGGER/api/v1/achievements/examples/
 └── examples-api.yaml  ← создать/заполнить
```

---

## 🏗️ Целевая архитектура

### Backend
- **Микросервис:** documentation/QA tooling (небоевой сервис)
- **Назначение:** поставка примеров и моков; не влияет на прод среду

### Frontend
- Используется разработчиками для тестов (Storybook, Swagger UI) и QA автоматизации

### Комментарий для YAML

```yaml
# Target Architecture:
# - Not a runtime microservice: documentation helpers
# - Consumers: developers, QA, integration tests
# - Related APIs: achievement-core, achievement-tracking
```

---

## ✅ Что нужно сделать (детальный план)

1. Сформировать разделы: "Боевые достижения", "Квестовые", "Социальные", "Коллекции".
2. Приложить примеры REST запросов (GET/POST), ответов, WebSocket сообщений.
3. Добавить примеры batch payload и idempotency ключей.
4. Подготовить таблицы наград, cosmetic IDs, notification payload.
5. Добавить тестовые сценарии (Given/When/Then) и cron примеры.
6. Согласовать структуры с core/tracking API.
7. Пройти чеклист и указать использование в QA/Docs.

---

## 🔀 Разделы (пример структуры)

- `combat-achievements` – примеры для боевых событий (kills, headshots, combo)
- `quest-achievements` – прогресс по квестам, branching outcomes
- `social-achievements` – party, friend, chat interactions
- `collection-achievements` – meta achievements, hidden, collector items
- `batch-updates` – batch события JSON, retries
- `websocket-events` – `achievement-unlocked`, `progress-updated`
- `reward-distribution` – sample выдача titles, cosmetics, currency
- `qa-scenarios` – автотесты, postman/locust примеры

---

## 🧱 Примеры, которые нужно включить

- Боевой achievement "Cyber Slayer" (kill 100 enemies)
- Quest achievement "Narrative Explorer" (complete branching quest)
- Social achievement "Party Leader" (lead 10 successful raids)
- Collection achievement "Legendary Collector" (obtain all legendary implants)
- Hidden achievement "Secret Hacker" (под условиями)
- WebSocket message `achievement-unlocked` (payload)
- Batch payload с тремя событиями и `idempotencyKey`
- Notification JSON + UI toast пример

---

## 📎 Checklist

- [ ] Структура документа следует шаблону examples (markdown + code blocks)
- [ ] Примеры соответствуют схемам из задач 218/219
- [ ] Добавлены сценарии QA и ссылки на связанные API
- [ ] Обновить `tasks/config/brain-mapping.yaml`
- [ ] Пометить `.BRAIN` документ статусом задачи

---

## ❓FAQ

**Q:** Это рабочий API?**
**A:** Нет, это документационный файл с примерами; фактическая реализация в `achievement-core`/`tracking`.

**Q:** Нужна ли генерация данных?**
**A:** Можно добавить мок эндпоинты для QA, но основной фокус – примеры и гайды.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

