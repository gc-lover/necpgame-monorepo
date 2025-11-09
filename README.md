# NECPGAME Monorepo

## Структура

- `pipeline/` — процессы, роли, чеклисты и скрипты мультиагентного пайплайна.
  - `GLOBAL-RULES.yaml` — глобальные принципы и ограничения.
  - `agents/` — YAML-инструкции для всех агентов.
  - `checklists/` — обязательные проверки при переходах между стадиями.
  - `templates/` — шаблоны задач, отчётов и коммуникаций.
  - `scripts/` — PowerShell-утилиты и автоматизация (валидаторы, queue-manager, pre-commit).
- `shared/`
  - `docs/knowledge/` — канон, механики, контент, исследования (YAML).
  - `docs/communications/` — коммуникационные пакеты (Community Agent).
  - `trackers/` — очереди, статусные таблицы, логи.
- `services/`
  - `openapi/` — OpenAPI спецификации и задачи на их подготовку.
  - `backend/` — Java 21 backend, генераторы контрактов.
  - `frontend/` — React + TypeScript frontend, Orval конфиги и клиенты.

## Основные скрипты

```powershell
# Проверка структуры репозитория
pwsh -File pipeline/scripts/check-architecture-health.ps1 -RootPath C:\NECPGAME

# Управление очередями без ручного редактирования YAML
pwsh -File pipeline/scripts/queue-manager.ps1 -Command add `
     -SourceFile shared/trackers/queues/backend/not-started.yaml `
     -Id BE-2025-999 -Title "Новый сценарий" -Owner "Backend Implementer"

# Валидация OpenAPI
pwsh -File pipeline/scripts/validate-swagger.ps1 -ApiDirectory services/openapi/api/v1

# Генерация backend слоёв
pwsh -File services/backend/scripts/generate-openapi-layers.ps1 -ApiSpec services/openapi/api/v1/<domain>/<spec>.yaml

# Генерация фронтенд клиента
pwsh -File services/frontend/scripts/generate-api-orval.ps1

# Pre-commit проверки (структура, очереди, OpenAPI)
pwsh -File pipeline/scripts/run-precommit.ps1
```

## Git-поток

- Каждую логическую доработку фиксируйте отдельным коммитом.
- Используйте `git worktree` либо короткоживущие ветки: `git worktree add ../feature-x feature/x`.
- Перед push выполняйте `pipeline/scripts/run-precommit.ps1` (архитектура, очереди, OpenAPI).
- Обновление трекеров и логов (Activity, Decision) обязательно при переходе задач между стадиями.
- Тяжёлые артефакты (рендеры, media, UE5) храните в отдельном хранилище или через git LFS.

## CI

`.github/workflows/ci.yml` использует path-фильтры:

- `structure` — выполняет проверки архитектуры, Markdown и review-меток.
- `openapi` — запускает `validate-swagger` при изменениях в `services/openapi/**`.
- `backend` / `frontend` — запускают структурные проверки сервисов только при изменениях соответствующих директорий.

Следите за успешным прохождением workflow перед merge.

## Требования к документации

- Знания оформляются через `shared/docs/knowledge/templates/knowledge-entry-template.yaml`.
- Лимит файла — 400 строк; превышение → новые файлы с суффиксами `_0001`, `_0002`, ….
- Запускайте `pipeline/scripts/check-file-limits.ps1`, `check-knowledge-schema.ps1`, `check-knowledge-review.ps1` перед передачей.
- Markdown-файлы в knowledge недопустимы, проверяется `check-knowledge-markdown.ps1`.
- Очереди обновляйте через `pipeline/scripts/queue-manager.ps1`.

## Связанные правила Cursor

Правила для каждой роли находятся в `.cursor/rules/*.mdc` и автоматически подсказывают агентам релевантные инструкции при работе с соответствующими файлами.
