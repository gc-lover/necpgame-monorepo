# Очереди статусов (YAML)

## Назначение
Каждая очередь хранится в YAML-файле и содержит массив задач, находящихся в одном статусе. Формат предназначен для автоматической обработки скриптами и упрощает контроль структуры.

## Пример структуры
```yaml
status: queued
last_updated: 2025-11-09 14:10
items:
  - id: API-2025-041
    title: Неоновый рынок — обновление
    source: .BRAIN/02-gameplay/neon-market.md
    owner: API Task Architect
    updated: 2025-11-02 11:30
```

## Каталоги и файлы
- `concept/{queued,in-progress,review,completed}.yaml`
- `api/{queued,in-progress,review,completed}.yaml`
- `backend/{not-started,in-progress,completed}.yaml`
- `frontend/{not-started,in-progress,completed}.yaml`
- `qa/{planned,executing,completed}.yaml`
- `release/{ready,released}.yaml`
- `refactor/{queued,in-progress,completed}.yaml`

## Правила
- При смене статуса задача удаляется из старого файла и добавляется в новый.
- Все timestamps в формате `YYYY-MM-DD HH:MM`.
- Скрипт `check-queue-yaml.ps1` проверяет структуру и уникальность идентификаторов. Выполняй его перед коммитом.
- При превышении 500 строк разбивай файл на `_0001.yaml`, `_0002.yaml` и т.п., добавляй ссылки в README.
- Для агрегированной сводки запускай `pipeline/scripts/status-dashboard.ps1` (файл `shared/trackers/status-dashboard.yaml`).

