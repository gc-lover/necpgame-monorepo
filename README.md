# NECPGAME Monorepo

## Структура

- `pipeline/` — процессы, роли, чеклисты и скрипты мультиагентного пайплайна.
  - `GLOBAL-RULES.yaml` — глобальные принципы и ограничения.
  - `agents/` — YAML-инструкции для всех агентов.
  - `checklists/` — обязательные проверки при переходах между стадиями.
  - `templates/` — шаблоны задач, отчётов и коммуникаций.
  - `scripts/` — PowerShell-утилиты и автоматизация (валидаторы, генерация/закрытие задач, pre-commit).
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
# Генерация задач из очереди (shared/trackers/queues → pipeline/tasks) с автогенерацией ID, обновлением очереди и Activity Log
pwsh -File pipeline/scripts/generate-tasks-from-queue.ps1 \
     -QueueFile shared/trackers/queues/backend/not-started.yaml \
     -TargetDirectory pipeline/tasks/06_backend_implementer \
     -Prefix BE -Actor "Backend Implementer" \
     [-Id BE-2025-029] [-TemplateFile pipeline/templates/task-from-queue-template.yaml] \
     [-Force] [-NoQueueUpdate] [-DisableActivityLog] [-SkipIndexUpdate] [-ValidateSync] [-ValidateSyncOnly]
# Завершение задачи (перенос в новую очередь, архивация и Activity Log)
pwsh -File pipeline/scripts/complete-task.ps1 \
     -TaskFile pipeline/tasks/06_backend_implementer/BE-042-new-api-layer.yaml \
     -TargetQueueFile shared/trackers/queues/backend/completed.yaml \
     -Actor "Backend Implementer" \
     [-SourceQueueFile shared/trackers/queues/backend/in-progress.yaml] [-ArchiveDirectory pipeline/tasks/archive]
# Создание новой записи знания
pwsh -File pipeline/scripts/new-knowledge.ps1 \
     -Title "Рабочее название" \
     -DocumentType canon \
     -Category vision \
     -OutputDirectory shared/docs/knowledge/canon/vision \
     -Tags vision,lore \
     [-Owners concept_director,vision_manager] [-Actor "Vision Manager"]
# Запуск сценария агента (workflow → последовательные команды)
pwsh -File pipeline/scripts/run-scenario.ps1 -Role openapi-executor -Step claim-task [-Variables @{ "spec" = "services/openapi/api/v1/foo.yaml" }] [-DryRun]
# Генерация чеклистов для Cursor (`.cursor/tasks/*.json`)
pwsh -File pipeline/scripts/generate-checklist-tasks.ps1 -All [-Force]
# Мониторинг и авто-карточки по знаниям
pwsh -File pipeline/scripts/watch-knowledge.ps1 [-ProcessGlossary] [-DryRun]
# Сравнение версий документа знаний
pwsh -File pipeline/scripts/knowledge-diff.ps1 -File shared/docs/knowledge/.../document.yaml [-Since HEAD~1]
# Проверка обязательного обновления Activity Log
pwsh -File pipeline/scripts/check-activity-log.ps1 [-BaseRef origin/develop]
# Формирование статус-дэшборда
pwsh -File pipeline/scripts/status-dashboard.ps1 [-IncludeTasks]
# Итоговая сводка для handoff
pwsh -File pipeline/scripts/handoff-summary.ps1 -QueueFile shared/trackers/queues/backend/in-progress.yaml -Role "Backend Implementer" [-TasksDirectory pipeline/tasks/06_backend_implementer] [-Format markdown]
```

- Если в карточке очереди нет `id`, укажите `-Prefix` — скрипт присвоит номер (`<PREFIX>-000`, `<PREFIX>-001`, …), допишет slug из `title` и обновит очередь.
- Имя файла формируется как `<ID>-<slug>.yaml`; если title пустой или после нормализации slug отсутствует, используется только идентификатор.
- Параметр `-Actor` фиксирует запись в `shared/trackers/activity-log.yaml`; без него используется значение `automation`. `-DisableActivityLog` отключает автоматическую запись.
- После генерации/закрытия задач отдельное редактирование `shared/trackers/activity-log.yaml` не требуется: запись добавляется автоматически. Ручные правки допустимы только в исключительных ситуациях (например, ретроактивное восстановление истории) и фиксируются через отдельный MR.
- Параметры `-ValidateSync` и `-ValidateSyncOnly` проверяют соответствие между очередями и task-файлами. Скрипт автоматически обновляет `pipeline/tasks/index.yaml`, в котором собрана сводка по всем очередям.

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
- Перед открытием PR выполняйте `pipeline/scripts/run-precommit.ps1` (включает `check-activity-log.ps1` в режиме проверки индексированных файлов).
- Установите hook: `pwsh -File pipeline/scripts/install-precommit.ps1` (на Linux/macOS не забудьте `chmod +x .git/hooks/pre-commit`) — он автоматически вызывает run-precommit при каждом коммите.
- Обновление трекеров и логов (Activity, Decision) обязательно при переходе задач между стадиями.
- Тяжёлые артефакты (рендеры, media, UE5) храните в отдельном хранилище или через git LFS.
- Настройте защиту ветки `main` и, при необходимости, `develop` в настройках GitHub (Branch protection rules).

## CI

`.github/workflows/ci.yml` использует path-фильтры:

- `structure` — выполняет проверки архитектуры, Markdown и review-меток.
- `openapi` — запускает `validate-swagger` при изменениях в `services/openapi/**`.
- `backend` / `frontend` — запускают структурные проверки сервисов только при изменениях соответствующих директорий.
- `pr-main-validation` — включается только для Pull Request в `main`, прогоняет `run-precommit`, `check-knowledge-schema`, а также гарантирует успешный запуск maven/npm тестов.
- Отдельный workflow `task-link` проверяет, что в описании PR указаны ID задач и путь к соответствующей очереди (`shared/trackers/queues/...`).

Следите за успешным прохождением workflow перед merge.

Дополнительно:

- Для Pull Request в `develop` работают первые четыре job; для `main` дополнительно требуется зелёный статус `pr-main-validation`.
- Настройте branch protection так, чтобы перечисленные статусы были обязательными перед merge.

## Требования к документации

- Знания оформляются через `shared/docs/knowledge/templates/knowledge-entry-template.yaml`.
- Лимит файла — 400 строк; превышение → новые файлы с суффиксами `_0001`, `_0002`, ….
- Запускайте `pipeline/scripts/check-file-limits.ps1`, `check-knowledge-schema.ps1`, `check-knowledge-review.ps1` перед передачей.
- Markdown-файлы в knowledge недопустимы, проверяется `check-knowledge-markdown.ps1`.
- Очереди обновляйте через автоматические скрипты (`generate-tasks-from-queue.ps1` для создания карточек, `complete-task.ps1` для перевода статусов); ручное редактирование YAML запрещено.
- Общая сводка по состоянию очередей и задач — `pipeline/tasks/index.yaml` (обновляется `generate-tasks-from-queue.ps1` или вручную через `-ValidateSyncOnly`).
- Генерируйте новые знания через `pipeline/scripts/new-knowledge.ps1` (быстрый запуск: `.cursor/tasks/new-knowledge.json`, автоматическое обновление глоссария и Activity Log).
- Мониторинг статусов: `pipeline/scripts/status-dashboard.ps1` (результат — `shared/trackers/status-dashboard.yaml`) и точечные handoff-сводки через `pipeline/scripts/handoff-summary.ps1` (`shared/trackers/handovers/`).

## Связанные правила Cursor

Правила для каждой роли находятся в `.cursor/rules/*.mdc` и автоматически подсказывают агентам релевантные инструкции при работе с соответствующими файлами.
