# Синтаксис и формат для Cursor Background Agents

## Обзор

Cursor Background Agents могут работать с существующими YAML файлами задач, контента и механик в проекте. Этот документ описывает, как загружать информацию и создавать задачи для агентов.

## Структура существующих YAML файлов

### Roadmap задачи (knowledge/analysis/tasks/roadmap/)

```yaml
metadata:
  id: roadmap-v0.1-tech-demo
  title: "v0.1 Tech Demo"
  version: "1.16.0"
  
tasks:
  critical:
    - id: infra-001
      title: Настройка базовой инфраструктуры
      description: |
        Подробное описание задачи
      priority: critical
      status: pending|in-progress|completed|blocked
      dependencies: [task-id-1, task-id-2]
      estimated_effort: 3d
      actual_effort: 2d
      notes: |
        Заметки о выполнении
      related_documents:
        - knowledge/implementation/infrastructure/infrastructure-index.yaml
      implementation_paths:
        - docker-compose.yml
        - infrastructure/docker/
```

### Активные задачи (knowledge/analysis/tasks/active/)

```yaml
metadata:
  id: analysis-combat-systems-wave1-brief
  title: "Combat Systems — Wave 1 Brief"
  status: draft|approved|pending
  version: "1.0.0"
  
summary:
  problem: Описание проблемы
  goal: Цель задачи
  essence: Суть задачи
  
content:
  sections:
    - id: package_readiness
      title: Состав пакета
      body: |
        Детальное описание
        
review:
  chain:
    - role: brain_manager
      status: pending|approved
      
implementation:
  needs_task: true|false
  queue_reference:
    - shared/trackers/queues/concept/queued.yaml
  blockers: []
```

### Контент квестов (knowledge/content/quests/)

```yaml
metadata:
  id: content-quests-mq-001
  title: "Первый квест"
  status: approved
  
content:
  sections:
    - id: stages
      title: Стадии квеста
      body: |
        Описание стадий
    - id: dialogues
      title: Диалоги
      body: |
        Описание диалогов
    - id: encounters
      title: Боевые встречи
      body: |
        Описание встреч
```

### Механики (knowledge/mechanics/)

```yaml
metadata:
  id: combat-inventory
  title: "Система инвентаря"
  status: draft
  
summary:
  problem: Нужна система хранения предметов
  goal: Реализовать инвентарь
  
content:
  sections:
    - id: inventory_mechanics
      title: Механика инвентаря
      body: |
        Детальное описание
```

## Как агенты могут загружать задачи

### 1. Из roadmap файлов

Агенты могут сканировать `knowledge/analysis/tasks/roadmap/*.yaml` и извлекать задачи:

```yaml
# Агент находит задачу:
task:
  source_file: knowledge/analysis/tasks/roadmap/v0.1-tech-demo.yaml
  source_id: infra-001
  title: "Настройка базовой инфраструктуры"
  priority: critical
  status: pending
  description: |
    Настроить Docker Compose...
  
# Агент загружает связанные документы:
related_documents:
  - knowledge/implementation/infrastructure/infrastructure-index.yaml
  
# Агент работает с файлами:
implementation_paths:
  - docker-compose.yml
  - infrastructure/docker/
```

### 2. Из активных задач

Агенты могут работать с задачами из `knowledge/analysis/tasks/active/`:

```yaml
# Агент читает задачу:
task:
  source_file: knowledge/analysis/tasks/active/CURRENT-WORK/active/2025-11-09-combat-systems-wave1-brief.yaml
  source_id: analysis-combat-systems-wave1-brief
  status: draft
  needs_task: false
  github_issue: 70
  
# Агент проверяет review:
review:
  status: pending
  
# Агент создает задачи для реализации
```

### 3. Из контента квестов

Агенты могут создавать задачи для реализации квестов:

```yaml
# Агент находит квест:
content_task:
  source_file: knowledge/content/quests/main/mq-001-first-steps.yaml
  source_id: content-quests-mq-001
  title: "Реализация квеста: Первый квест"
  
# Агент использует структуру квеста:
quest_structure:
  stages: [...]
  dialogues: [...]
  encounters: [...]
  
# Агент создает задачи для реализации
```

### 4. Из механик

Агенты могут создавать задачи для реализации механик:

```yaml
# Агент находит механику:
mechanic_task:
  source_file: knowledge/mechanics/combat/combat-inventory.yaml
  source_id: combat-inventory
  title: "Реализация системы инвентаря"
  
# Агент использует описание:
summary:
  problem: "Нужна система хранения предметов"
  goal: "Реализовать инвентарь"
  
# Агент создает код на основе описания
```

## Формат для создания новых задач агентов

Для новых задач используйте формат из `template.yaml`:

```yaml
metadata:
  id: agent-task-<unique-id>
  title: "Название задачи"
  priority: critical|high|medium|low
  status: pending|in-progress|review|completed
  assignee: cursor-agent
  created_at: 2025-11-22T00:00:00Z
  
task:
  description: |
    Подробное описание
  context: |
    Контекст задачи
    
implementation:
  steps:
    - step: 1
      action: "Что нужно сделать"
      files:
        - path/to/file.h
        - path/to/file.cpp
      validation: |
        Как проверить выполнение
        
dependencies:
  tasks: []
  documents: []
  code_files: []
  
acceptance_criteria:
  - Критерий 1
  - Критерий 2
  
review:
  automated_checks:
    - SOLID принципы
    - Стиль кода
    - Чеклист ревью
  human_review_required: false
```

## Интеграция с существующими системами

### REST API (workqueue-service)

Задачи могут быть синхронизированы с REST API:

```bash
# Получить задачу
GET /api/agents/<uuid>/next-task

# Принять задачу
POST /api/agents/<uuid>/next-task/accept
{
  "itemId": "<queue_item_uuid>",
  "expectedVersion": 1,
  "note": "Задача в работе"
}

# Сдать задачу
POST /api/agents/<uuid>/next-task/release
{
  "itemId": "<queue_item_uuid>",
  "expectedVersion": 2,
  "statusCode": "completed",
  "note": "Задача выполнена"
}
```

### См. также:
- `knowledge/implementation/agent-operating-manual.yaml` - руководство по REST API
- `knowledge/implementation/agent-scenarios.yaml` - сценарии работы агентов
- `knowledge/analysis/tasks/agent-tasks/INTEGRATION-GUIDE.yaml` - детальная интеграция

## Примеры использования

### Пример 1: Загрузка задачи из roadmap

```yaml
# Агент находит в v0.1-tech-demo.yaml:
task_id: combat-001
title: "UE5 Dedicated Server для игровой логики боя"
priority: high
status: pending
description: |
  Использовать UE5 Dedicated Server для авторитетной симуляции боя...

# Агент создает задачу для реализации:
agent_task:
  source: roadmap
  source_id: combat-001
  files_to_create:
    - client/UE5/NECPGAME/Source/NECPGAME/Server/ULyraServerGatewayConnection.h
    - client/UE5/NECPGAME/Source/NECPGAME/Server/ULyraServerGatewayConnection.cpp
```

### Пример 2: Загрузка контента квеста

```yaml
# Агент находит в content/quests/main/mq-001-first-steps.yaml:
quest_id: content-quests-mq-001
title: "Первый квест"

# Агент создает задачу для реализации:
agent_task:
  source: content
  source_id: content-quests-mq-001
  quest_data:
    stages: [...]
    dialogues: [...]
    encounters: [...]
  
  files_to_create:
    - knowledge/content/quests/main/mq-001-first-steps-data.yaml
    - services/quest-service/src/main/java/.../MQ001QuestHandler.java
```

### Пример 3: Загрузка механики

```yaml
# Агент находит в mechanics/combat/combat-inventory.yaml:
mechanic_id: combat-inventory
title: "Система инвентаря"
problem: "Нужна система хранения предметов"

# Агент создает задачу для реализации:
agent_task:
  source: mechanics
  source_id: combat-inventory
  implementation:
    - step: 1
      action: "Создать интерфейс IInventory"
      files:
        - client/UE5/NECPGAME/Source/NECPGAME/Inventory/IInventory.h
    - step: 2
      action: "Реализовать класс UInventoryComponent"
      files:
        - client/UE5/NECPGAME/Source/NECPGAME/Inventory/UInventoryComponent.h
        - client/UE5/NECPGAME/Source/NECPGAME/Inventory/UInventoryComponent.cpp
```
