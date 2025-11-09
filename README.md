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
# Требуется модуль powershell-yaml (однократно): pwsh -Command "Install-Module -Name powershell-yaml -Scope CurrentUser -Force"
# Альтернатива без PowerShell (требуется PyYAML)
python pipeline/scripts/queue_manager.py add shared/trackers/queues/backend/not-started.yaml \
       --id BE-2025-999 --title "Новый сценарий" --owner "Backend Implementer"

# Валидация OpenAPI
pwsh -File pipeline/scripts/validate-swagger.ps1 -ApiDirectory services/openapi/api/v1

# Генерация backend слоёв
pwsh -File services/backend/scripts/generate-openapi-layers.ps1 -ApiSpec services/openapi/api/v1/<domain>/<spec>.yaml

# Генерация фронтенд клиента
pwsh -File services/frontend/scripts/generate-api-orval.ps1

# Пре-коммит проверки (структура, очереди, OpenAPI)
pwsh -File pipeline/scripts/run-precommit.ps1
# Установка pre-commit hook
pwsh -File pipeline/scripts/install-precommit.ps1
# Генерация задач из очереди (shared/trackers/queues → pipeline/tasks) с автогенерацией ID
pwsh -File pipeline/scripts/generate-tasks-from-queue.ps1 \
     -QueueFile shared/trackers/queues/backend/not-started.yaml \
     -TargetDirectory pipeline/tasks/06_backend_implementer \
     -Prefix BE -Actor "Backend Implementer" \
     [-Id BE-2025-029] [-TemplateFile pipeline/templates/task-from-queue-template.yaml] [-Force] [-NoQueueUpdate] [-DisableActivityLog]
```

- Если в карточке очереди нет `id`, укажите `-Prefix` — скрипт присвоит номер (`<PREFIX>-000`, `<PREFIX>-001`, …), допишет slug из `title` и обновит очередь.
- Имя файла формируется как `<ID>-<slug>.yaml`; если title пустой или после нормализации slug отсутствует, используется только идентификатор.
- Параметр `-Actor` фиксирует запись в `shared/trackers/activity-log.yaml`; без него используется значение `automation`. `-DisableActivityLog` отключает автоматическую запись.
- После генерации задач отдельное редактирование `shared/trackers/activity-log.yaml` не требуется: запись добавляется автоматически. Ручные правки допустимы только в исключительных ситуациях (например, ретроактивное восстановление истории) и фиксируются через отдельный MR.

## Git-поток (lightweight GitFlow)

- Основные ветки:
  - `main` — продакшн: только Merge Requests из `develop` (release) или `hotfix/*`. Прямые push запрещены (`.github/workflows/enforce-pr-merges.yml`).
  - `develop` — интеграционная ветка для согласованных задач.
- Рабочие ветки:
  - `feature/<task-id>-<slug>` — разработка новых задач.
  - `hotfix/<issue>` — срочные исправления из `main`.
  - `release/<version>` — подготовка релиза перед merge в `main`.
- Каждая ветка создаётся от `develop`, для hotfix — от `main`.
- Мержим только через Pull Request с обязательной проверкой CI и checklist агента.
- После merge feature → develop ветку удаляем (локально и на origin).
- Используйте `git worktree add ../feature-x feature/<task>` для параллельных потоков.
- Перед открытием PR выполняйте `pipeline/scripts/run-precommit.ps1`.
- Установите hook: `pwsh -File pipeline/scripts/install-precommit.ps1` (на Linux/macOS не забудьте `chmod +x .git/hooks/pre-commit`).
- Обновление трекеров и логов (Activity, Decision) обязательно при переходе задач между стадиями.
- Тяжёлые артефакты (рендеры, media, UE5) храните в отдельном хранилище или через git LFS.
- Для окружений без PowerShell применяйте Python CLI `pipeline/scripts/queue_manager.py` и аналогичные обёртки.
- Настройте защиту ветки `main` и, при необходимости, `develop` в настройках GitHub (Branch protection rules).

## CI

`.github/workflows/ci.yml` использует path-фильтры:

- `structure` — выполняет проверки архитектуры, Markdown и review-меток.
- `openapi` — запускает `validate-swagger` при изменениях в `services/openapi/**`.
- `backend` / `frontend` — запускают структурные проверки сервисов только при изменениях соответствующих директорий.
- `pr-main-validation` — включается только для Pull Request в `main`, прогоняет `run-precommit`, `check-knowledge-schema`, доступность queue-manager и зарезервированные шаги для `mvn test` / `npm test`.

Следите за успешным прохождением workflow перед merge.

Дополнительно:

- Для Pull Request в `develop` работают первые четыре job; для `main` дополнительно требуется зелёный статус `pr-main-validation`.
- Настройте branch protection так, чтобы перечисленные статусы были обязательными перед merge.

## Требования к документации

- Знания оформляются через `shared/docs/knowledge/templates/knowledge-entry-template.yaml`.
- Лимит файла — 400 строк; превышение → новые файлы с суффиксами `_0001`, `_0002`, ….
- Запускайте `pipeline/scripts/check-file-limits.ps1`, `check-knowledge-schema.ps1`, `check-knowledge-review.ps1` перед передачей.
- Markdown-файлы в knowledge недопустимы, проверяется `check-knowledge-markdown.ps1`.
- Очереди обновляйте через `pipeline/scripts/queue-manager.ps1` или `python pipeline/scripts/queue_manager.py`.

## Связанные правила Cursor

Правила для каждой роли находятся в `.cursor/rules/*.mdc` и автоматически подсказывают агентам релевантные инструкции при работе с соответствующими файлами.
