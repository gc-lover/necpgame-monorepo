# NECPGAME Monorepo

## Структура

- `pipeline/` — процессы, роли, чеклисты и скрипты мультиагентного пайплайна.
  - `GLOBAL-RULES.yaml` — глобальные принципы и ограничения.
  - `agents/` — YAML-инструкции для всех агентов.
  - `checklists/` — обязательные проверки при переходе между стадиями.
  - `templates/` — шаблоны задач, отчётов и коммуникаций.
  - `scripts/` — PowerShell утилиты для валидаций и автоматизации.
- `shared/`
  - `docs/knowledge/` — канон, механики, контент, исследования (в формате YAML).
  - `docs/communications/` — пакеты коммуникаций (готовятся Community агентом).
  - `trackers/` — очереди, логи и статусные таблицы (Activity, Readiness, Release и т.д.).
- `services/`
  - `openapi/` — OpenAPI спецификации и задачи на их создание.
  - `backend/` — Java 21 backend, генераторы контрактов, микросервисные модули.
  - `frontend/` — React + TypeScript frontend, Orval конфиги, скрипты генерации.

## Основные скрипты

```powershell
# Проверка структуры репозитория
pwsh -File pipeline/scripts/check-architecture-health.ps1 -RootPath C:\NECPGAME

# Валидация OpenAPI и генерация backend слоёв
pwsh -File services/backend/scripts/generate-openapi-layers.ps1 -ApiSpec services/openapi/api/v1/<domain>/<spec>.yaml

# Генерация фронтенд клиента
pwsh -File services/frontend/scripts/generate-api-orval.ps1
```

## Git-поток

- Каждую логическую доработку фиксируйте отдельным коммитом.
- Перед коммитом запускайте релевантные проверки (`check-queue-yaml.ps1`, `check-knowledge-schema.ps1`, `validate-swagger.ps1` и т.д.).
- Обновление трекеров и логов (Activity, Decision) обязательно при переходе задач между стадиями.

## Требования к документации

- Документы знаний создаются из шаблона `shared/docs/knowledge/templates/knowledge-entry-template.yaml`.
- Лимит — 400 строк; при превышении создаются последующие файлы с суффиксами `_0001`, `_0002`, ….
- Перед передачей материала на следующую роль запускайте `pwsh -File pipeline/scripts/check-file-limits.ps1` и прикладывайте чеклист.

## Связанные правила Cursor

Правила для каждой роли находятся в `.cursor/rules/*.mdc` и автоматически подсказывают агентам релевантные инструкции при работе с соответствующими файлами.


