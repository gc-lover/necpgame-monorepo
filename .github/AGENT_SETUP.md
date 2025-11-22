# Настройка системы агентов для NECPGAME

## Шаг 1: Создание GitHub Project

### Через MCP (рекомендуется):

```bash
# В Cursor AI можно использовать команду:
"Создай GitHub Project 'NECPGAME Development' для управления задачами"
```

Или через MCP функции:
- `mcp_github_list_projects` - проверить существующие проекты
- Создать проект вручную через GitHub UI (Projects V2 пока не поддерживает создание через API)

### Через GitHub UI:

1. Перейдите в репозиторий → вкладка **Projects**
2. Нажмите **New project**
3. Выберите шаблон **Board** (канбан)
4. Назовите: **"NECPGAME Development"**

## Шаг 2: Настройка Custom Fields

### Field 1: "Development Stage"

1. В Project нажмите **⚙️ Configure** (шестеренка)
2. Нажмите **+ Add field** → **Single select**
3. Название: `Development Stage`
4. Добавьте опции:
   - `idea-writer` (Idea Writer)
   - `architect` (Architect)
   - `api-designer` (API Designer)
   - `backend-dev` (Backend Developer)
   - `network-dev` (Network Engineer)
   - `devops` (DevOps/Infrastructure)
   - `performance` (Performance Engineer)
   - `ue5-dev` (UE5 Developer)
   - `testing` (QA/Testing)
   - `release` (Release)

### Field 2: "Branch" (рекомендуется)

1. **+ Add field** → **Text**
2. Название: `Branch`
3. Автоматически заполняется workflow при создании ветки

### Field 3: "Priority" (опционально)

1. **+ Add field** → **Single select**
2. Опции: `P0`, `P1`, `P2`, `P3`

## Шаг 3: Создание Views для каждого агента

### View для Idea Writer:

1. В Project нажмите **+ Add view**
2. Название: **"Idea Writer Queue"**
3. Фильтр: `Development Stage` = `idea-writer`
4. Сортировка: по дате создания (старые сначала)

### View для Architect:

1. **+ Add view** → **"Architect Queue"**
2. Фильтр: `Development Stage` = `architect`

### И так далее для каждого агента...

## Шаг 4: Настройка Labels

Убедитесь, что в репозитории есть метки:

- `agent:idea-writer`
- `agent:architect`
- `agent:api-designer`
- `agent:backend`
- `agent:network`
- `agent:devops`
- `agent:performance`
- `agent:ue5`
- `agent:qa`
- `agent:release`
- `stage:idea`
- `stage:design`
- `stage:api-design`
- `stage:backend-dev`
- `stage:network`
- `stage:infrastructure`
- `stage:performance`
- `stage:client-dev`
- `stage:testing`
- `stage:release`

## Шаг 5: Использование через Cursor AI

### Создание задачи для Idea Writer:

```
"Создай Issue для новой идеи квеста и добавь в Project со статусом idea-writer"
```

Cursor AI выполнит:
1. Создаст Issue через `mcp_github_issue_write`
2. Добавит метки: `agent:idea-writer`, `stage:idea`
3. Добавит Issue в Project через `mcp_github_add_project_item`
4. Установит поле `Development Stage` = `idea-writer`

### Переход задачи к следующему агенту:

Когда Idea Writer завершил работу:

```
"Обнови статус Issue #X на architect"
```

Cursor AI:
1. Обновит поле `Development Stage` через `mcp_github_update_project_item`
2. Изменит метки на `agent:architect`, `stage:design`
3. Задача автоматически появится в view "Architect Queue"

### Автоматические переходы:

Workflow автоматически переводит задачи при:
- Создании PR с меткой `backend` → статус меняется на `backend-dev`
- Мерже PR с меткой `backend` → статус меняется на `ue5-dev`
- Мерже PR с меткой `ue5` → статус меняется на `testing`

## Шаг 6: Примеры команд для агентов

### Idea Writer:

```
"Покажи все задачи со статусом idea-writer"
"Создай идею для системы крафта и добавь в Project"
"Разработай лор для нового персонажа"
```

### Architect:

```
"Покажи задачи со статусом architect"
"Структурируй идею из Issue #5"
"Спроектируй архитектуру для системы инвентаря"
```

### API Designer:

```
"Покажи задачи со статусом api-designer"
"Создай OpenAPI спецификацию для Issue #10"
```

### Backend Developer:

```
"Покажи задачи со статусом backend-dev"
"Реализуй бекенд для Issue #15"
```

### UE5 Developer:

```
"Покажи задачи со статусом ue5-dev"
"Реализуй клиент для Issue #20"
```

## Шаг 7: Мониторинг прогресса

### Через MCP:

```
"Покажи статистику по Project: сколько задач на каждом этапе"
"Какие задачи заблокированы?"
"Покажи задачи с приоритетом P1"
```

### Через GitHub UI:

- Используйте диаграммы в Project для визуализации
- Создайте view "All In Progress" для отслеживания активных задач
- Используйте фильтры для поиска узких мест

## Автоматизация через GitHub Actions

Workflow `.github/workflows/project-status-automation.yml` автоматически:

1. **Определяет стадию** по меткам Issue/PR
2. **Обновляет поле** `Development Stage` в Project
3. **Добавляет метки** агента
4. **Переводит задачи** при мерже PR

### Настройка PROJECT_NUMBER:

В файле `.github/workflows/project-status-automation.yml` укажите номер вашего Project:

```yaml
env:
  PROJECT_NUMBER: 1  # Замените на номер вашего Project
```

Номер Project можно найти в URL: `https://github.com/users/gc-lover/projects/1` (1 - это номер)

## Troubleshooting

### Проблема: Workflow не обновляет статус

**Решение:**
1. Проверьте, что Project создан (Projects V2)
2. Убедитесь, что поле "Development Stage" существует
3. Проверьте права токена (нужен `project` scope)
4. Проверьте номер Project в `PROJECT_NUMBER`

### Проблема: MCP не может обновить Project

**Решение:**
1. Убедитесь, что токен имеет права `project`
2. Проверьте, что используется правильный Project ID
3. Убедитесь, что Issue/PR добавлены в Project

### Проблема: Автоматические переходы не работают

**Решение:**
1. Проверьте, что метки правильно названы
2. Убедитесь, что PR связан с Issue (через `Closes #X` или `Fixes #X`)
3. Проверьте логи GitHub Actions

