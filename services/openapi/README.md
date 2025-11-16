# OpenAPI (API-first)

Единый источник истины для контрактов. Backend и Frontend генерируются из спецификаций. Любые изменения API — через PR к OpenAPI.

## Структура каталогов
```
services/openapi/
  api/
    workqueue/
      workqueue.yaml
    game/
      core.yaml
      items.yaml
      economy.yaml
      world.yaml
      social.yaml
      loot.yaml
    shared/
      components.yaml        # общие схемы/типажи/ошибки
    _template.yaml           # стартовый каркас спецификации
  tasks/
    validate.sh              # локальная валидация
    bundle.sh                # сборка монолитного spec (если нужно)
```

Правила:
- Одна область — один файл. Переиспользование через `$ref` в `shared/components.yaml`.
- Версионирование — через `info.version` и Git‑теги. Запрещены breaking‑changes без мажорного bump.
- Обязательные `tags`, `operationId`, `summary`, `description`, `security` (если применимо).
- Ответы: всегда описывайте `default` ошибку (`application/problem+json` или стандартный `ApiError`).

## Процесс (API-first)
1) Меняем/добавляем спецификацию в `services/openapi/api/**.yaml`.
2) `tasks/validate.sh` — локальная проверка (lint + resolve refs).
3) Мердж → CI валидирует и публикует артефакт спецификации.
4) Генерация:
   - Backend: Maven OpenAPI generator (server stubs) + ручные имплементации.
   - Frontend: Orval/TypeScript клиенты из монолитного бандла.

## Стандарты
- Идентификаторы: `snake_case` для кодов (enum/code), `camelCase` для полей JSON.
- Дата/время: `string` + `format: date-time` (UTC, ISO 8601).
- UUID: `string` + `format: uuid`.
- Пагинация: `?page`/`?size` + `Link` заголовки и `X-Total-Count`.
- Ошибки: единый объект `ApiError { code, message, requirements[], details[] }`.
- Конвенции операций:
  - `operationId` обязателен и уникален.
  - `tags` не пустые; для публичных — добавляйте `public`.
  - Ответы: определяйте `default` с `ApiError`.
  - Security: если в `components.securitySchemes` объявлены схемы, каждая операция обязана указать `security` (кроме `public`).
- Конвенции схем:
  - Имена полей строго `camelCase`.
  - Поля, оканчивающиеся на `Id` — `format: uuid`.
  - Поля, оканчивающиеся на `At` — `format: date-time`.
  - Схемы `*Code` (enum) — значения в `snake_case`.

## Сопоставление с кодом
- Backend (`services/backend`): генерация интерфейсов/DTO по профилю `openapi` и последующая реализация сервисов.
- Frontend (`services/frontend`): Orval генерирует типы/клиенты. Запросы — строго через сгенерированные клиенты.

## Обязанности агента API (API Task Architect)
- Проектировать и обновлять OpenAPI, соблюдая конвенции и совместимость.
- Поддерживать `shared/components.yaml` и переиспользование схем.
- Обновлять версии спецификаций (SemVer) и вести changelog.
- Создавать PR c изменениями, запускать валидацию, инициировать генерацию клиентов/стабов.
- Согласовывать breaking‑changes, готовить миграционные инструкции для потребителей.


