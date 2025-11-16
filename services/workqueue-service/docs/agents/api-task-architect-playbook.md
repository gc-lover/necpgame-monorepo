# API Task Architect — Playbook

Роль: проектирует и поддерживает OpenAPI (API‑first), обеспечивает совместимость, генерацию клиентов и серверных стабов, соблюдение конвенций.

## Задачи
- Проектирование/обновление контрактов в `services/openapi/api/**.yaml`.
- Соблюдение конвенций (ниже), ведение версий (SemVer) и changelog.
- Публикация изменений и генерация клиентов (frontend) и стабов (backend).
- Сопровождение PR: ревью, валидация, миграции для потребителей.

## Конвенции (обязательно)
- Операции:
  - `operationId` обязателен и уникален.
  - `tags` не пустые; публичные помечайте тегом `public`.
  - Ответы содержат `default` с `ApiError`.
  - Если в `components.securitySchemes` есть схемы, каждая операция указывает `security` (кроме `public`).
- Схемы:
  - Имена полей только `camelCase`.
  - Поля `*Id` — `string` + `format: uuid`.
  - Поля `*At` — `string` + `format: date-time` (UTC).
  - Схемы `*Code` (enum) — значения в `snake_case`.
- Пагинация: `?page`/`?size` + заголовки `Link` и `X-Total-Count`.
- Ошибки: единый объект `ApiError { code, message, requirements[], details[] }`.

## Submit требований (segment=api)
При сдаче задачи через `/api/agents/tasks/{itemId}/submit`:
- В `metadata`:
  - `openapiVersion: "3.0.x/3.1.x"`
  - `specPath: "path/to/spec.yaml|json"`
- В артефактах: приложить файл спецификации (`.yaml/.yml/.json`) или ссылку.
- Валидатор выполняет:
  1. Парсинг OpenAPI.
  2. Проверку конвенций (operationId/tags/default/security; camelCase/uuid/date-time/snake_case).
  3. Ошибки — `400 validation.api.*` с подробностями.

## Pipeline
1) Получи задачу (segment=api).
2) Обнови/создай spec в `services/openapi/api/...`.
3) Приложи spec в submit (multipart) и укажи `metadata.openapiVersion/specPath`.
4) После успешного submit сервис создаст handoff‑задачу далее по правилам.

## Генерация
- Frontend: Orval (TypeScript) из OpenAPI bundle.
- Backend: openapi‑generator (server stubs) в `services/backend` (профиль `openapi`).


