# Task ID: API-TASK-180
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 18:00 | **Создатель:** AI Agent ДУАПИТАСК | **Зависимости:** none

---

## 📋 Описание

Создать API для технической сводки всех API эндпоинтов. 180+ endpoints, 29 моделей, интеграции, service mesh.

---

## 📚 Источники (1 документ)

**API Technical Documentation Summary:**
- `05-technical/API-TECHNICAL-DOCUMENTATION-SUMMARY.md` - Техническая сводка (605 строк)
  - 180+ API endpoints по категориям
  - 29 core data models
  - Integration map (service mesh)
  - Event-driven architecture
  - WebSocket channels
  - Rate limiting & security

**Split parts (для справки):**
- `05-technical/api-tech-docs/api-tech-summary-part1.md`
- `05-technical/api-tech-docs/api-tech-summary-part2.md`

---

## 🎯 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/technical/api-documentation.yaml`
**API версия:** v1
**Тип файла:** OpenAPI 3.0 Specification (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── technical/
            ├── api-documentation.yaml  ← Создать этот файл
            ├── backend-audit.yaml
            └── global-state.yaml
```

---

## ✅ Что нужно сделать

### Шаг 1: Создание базовой структуры файла

**Действия:**
1. Создать файл `api/v1/technical/api-documentation.yaml`.
2. Добавить базовую информацию OpenAPI (openapi, info, servers, tags).
3. Определить теги: `API Documentation`, `Technical Reference`, `Service Mesh`.

**Ожидаемый результат:**
- Файл `api-documentation.yaml` с корректной базовой структурой OpenAPI.

### Шаг 2: Реализация Endpoints для документации

**Действия:**
1. Добавить endpoint `GET /technical/api/endpoints` для получения списка всех endpoints.
   - Query params: `category`, `service`, `version`
   - Responses: `200 OK` (EndpointsListResponse), `400 BadRequest` (Error)
2. Добавить endpoint `GET /technical/api/models` для получения списка моделей данных.
   - Query params: `category`, `include_schemas`
   - Responses: `200 OK` (DataModelsResponse), `400 BadRequest` (Error)
3. Добавить endpoint `GET /technical/api/integration-map` для карты интеграций.
   - Responses: `200 OK` (IntegrationMapResponse), `404 NotFound` (Error)
4. Добавить endpoint `GET /technical/api/health` для статуса всех сервисов.
   - Responses: `200 OK` (HealthStatusResponse), `503 ServiceUnavailable` (Error)

**Ожидаемый результат:**
- Endpoints для получения технической документации API.

### Шаг 3: Определение моделей данных

**Действия:**
1. Создать схемы для моделей:
   - `EndpointsListResponse` (total_count, endpoints[], categories[])
   - `EndpointInfo` (path, method, category, service, description, params[], responses[])
   - `DataModelsResponse` (models[], total_count)
   - `DataModelInfo` (model_name, category, fields[], relationships[])
   - `IntegrationMapResponse` (services[], connections[], event_channels[])
   - `ServiceInfo` (service_id, name, port, endpoints_count, dependencies[])
   - `HealthStatusResponse` (status, services_status[], timestamp)
2. Использовать `PascalCase` для имен моделей.
3. Добавить примеры для каждой модели.

**Ожидаемый результат:**
- Все модели данных определены в секции `components/schemas`.

### Шаг 4: Определение схем безопасности

**Действия:**
1. Использовать `BearerAuth` из `shared/security/security.yaml` для административных эндпоинтов.
2. Определить `security` для каждого защищенного эндпоинта.

**Ожидаемый результат:**
- Корректное применение схем безопасности.

### Шаг 5: Валидация и правила

**Действия:**
1. Добавить валидацию для category (enum).
2. Указать ограничения для запросов.
3. Определить правила rate limiting.

**Ожидаемый результат:**
- Валидация и правила отражены в схемах.

---

## 📚 Дополнительная информация

См. дополнительный файл: **[api-generation-task-template-details.md](../../templates/api-generation-task-template-details.md)**

---

**ВНИМАНИЕ:** Это задание для АПИТАСК агента. Выполняй пошагово.


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

