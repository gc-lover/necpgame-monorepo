# Task ID: API-TASK-169
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** queued
**Создано:** 2025-11-07 11:50 | **Создатель:** AI Agent | **Зависимости:** API-TASK-127

---

## 📋 Описание

Создать API для MVP контента (6 документов). MVP endpoints, data models, initial data, content overview, text version plan, ui-main-game.

---

## 📚 Источники (6 документов)

- `mvp-endpoints.md` - список MVP endpoints
- `mvp-data-models.md` - модели данных для MVP
- `mvp-initial-data.md` - начальные данные игры
- `mvp-content/content-overview-2020-2093.md` - обзор контента
- `mvp-text-version-plan.md` - план текстовой версии
- `ui-main-game.md` - основной UI игры

---

## 📁 Целевой файл

```
api/v1/mvp/
├── mvp-endpoints.yaml
├── mvp-models.yaml
└── mvp-content.yaml
```

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** Разные сервисы (это cross-cutting API для MVP)  
**Порт:** N/A (маршрутизируется через API Gateway на разные сервисы)  
**API пути:** /api/v1/mvp/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** N/A (MVP endpoints используются во всех модулях)  
**Путь:** src/features/mvp/  
**State Store:** Multiple stores (зависит от endpoint)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- Все базовые компоненты MVP

**Готовые формы (@shared/forms):**
- Все базовые формы MVP

**Layouts (@shared/layouts):**
- Все layouts MVP

**Хуки (@shared/hooks):**
- Все базовые хуки MVP

**Примечание:** MVP endpoints - это упрощенная версия всех систем для быстрого запуска.

---

## ✅ Endpoints

1. **GET /api/v1/mvp/endpoints** - Список MVP endpoints
2. **GET /api/v1/mvp/models** - Data models для MVP
3. **GET /api/v1/mvp/initial-data** - Начальные данные

**Models:** MVPEndpoint, MVPModel, InitialGameData

---

**Источники:** 6 MVP документов

