# Как агентам находить свои задачи

## Механизм поиска задач

Агенты в Cursor не имеют автоматического доступа к списку задач. Они работают по запросу пользователя или могут искать задачи через MCP GitHub.

## Способ 1: Поиск через MCP GitHub (рекомендуется)

### Для Idea Writer агента

```javascript
// Используй MCP GitHub для поиска Issues с меткой agent:idea-writer
// Фильтр: метка agent:idea-writer И статус open И Development Stage = idea-writer
```

**Команда для агента:**
```
@agent-idea-writer Найди все открытые задачи для тебя через MCP GitHub и покажи список
```

**Что делать агенту:**
1. Используй `mcp_github_list_issues` с фильтром:
   - `labels: ['agent:idea-writer']`
   - `state: 'OPEN'`
2. Проверь Project статус через `mcp_github_list_project_items`:
   - Фильтр: `Development Stage = idea-writer`
   - Статус: `open`
3. Покажи пользователю список найденных задач
4. Спроси, с какой задачей работать

### Для Backend Developer агента

```javascript
// Фильтр: метка agent:backend И статус open И Development Stage = backend-dev
```

**Команда для агента:**
```
@backend Найди все задачи для бекенд разработки и покажи список
```

**Что делать агенту:**
1. Используй `mcp_github_list_issues` с фильтром:
   - `labels: ['agent:backend']`
   - `state: 'OPEN'`
2. Проверь, что есть OpenAPI спецификация (от API Designer)
3. Покажи список задач с приоритетами

### Для всех агентов

**Общий алгоритм поиска:**

1. **Определи свои метки:**
   - Idea Writer: `agent:idea-writer`
   - Architect: `agent:architect`
   - API Designer: `agent:api-designer`
   - Backend: `agent:backend`
   - Network: `agent:network`
   - DevOps: `agent:devops`
   - Performance: `agent:performance`
   - UE5: `agent:ue5`
   - QA: `agent:qa`
   - Release: `agent:release`

2. **Используй MCP GitHub для поиска:**
   ```javascript
   // Пример для Idea Writer
   const issues = await mcp_github_list_issues({
     owner: 'gc-lover',
     repo: 'necpgame-monorepo',
     labels: ['agent:idea-writer'],
     state: 'OPEN'
   });
   ```

3. **Проверь Project статус:**
   ```javascript
   // Получи Project items с нужным Development Stage
   const projectItems = await mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: 'Development Stage:idea-writer state:open',
     fields: ['title', 'status', 'Development Stage']
   });
   ```

4. **Отфильтруй готовые задачи:**
   - Исключи задачи со статусом `closed`
   - Исключи задачи на других этапах
   - Приоритизируй задачи без метки `in-progress`

## Способ 2: Работа по запросу пользователя

Пользователь может явно указать задачу:

```
@agent-idea-writer Работай с Issue #9
```

В этом случае:
1. Прочитай Issue #9 через MCP GitHub
2. Проверь, что задача для тебя (метка `agent:idea-writer`)
3. Проверь статус задачи (не обработана ли уже)
4. Начни работу

## Способ 3: Автоматическое уведомление (через GitHub Actions)

GitHub Actions автоматически:
- Добавляет метки при создании Issue
- Обновляет Project статус
- Комментирует в Issue, когда задача готова для агента

**Агент должен:**
1. Проверить комментарии в Issue
2. Если есть комментарий "Ready for {agent-name}" → задача готова
3. Начать работу

## Структура задачи для агента

Когда агент находит задачу, он должен проверить:

1. **Метки:**
   - Есть ли метка `agent:{agent-name}`?
   - Есть ли метка `stage:{stage-name}`?

2. **Project статус:**
   - Правильный ли `Development Stage`?
   - Открыта ли задача?

3. **Содержимое Issue:**
   - Есть ли описание задачи?
   - Есть ли критерии приемки?
   - Есть ли связанные документы?

4. **Готовность:**
   - Завершена ли предыдущая стадия?
   - Есть ли все необходимые входные данные?

## Примеры команд для поиска задач

### Idea Writer
```
@agent-idea-writer Покажи все открытые задачи для Idea Writer из GitHub Project
```

### Backend Developer
```
@backend Найди все задачи для бекенд разработки, где есть OpenAPI спецификация
```

### Architect
```
@architect Покажи все задачи на этапе архитектуры, которые ждут обработки
```

## Автоматизация через GitHub Actions

Workflow `project-status-automation.yml` автоматически:
- Добавляет Issues в Project
- Устанавливает `Development Stage` на основе меток
- Комментирует в Issue, когда задача готова

**Агент должен проверять комментарии:**
- Если есть комментарий от `github-actions[bot]` с текстом "Ready for {agent}" → задача готова

## Рекомендации

1. **Всегда проверяй статус перед началом работы:**
   - Не обрабатывай уже обработанные задачи
   - Проверяй, что задача на правильном этапе

2. **Используй MCP GitHub для поиска:**
   - Не полагайся только на упоминание пользователя
   - Активно ищи свои задачи

3. **Сообщай пользователю о найденных задачах:**
   - Покажи список задач
   - Предложи приоритеты
   - Спроси, с какой начать

4. **Помечай обработанные задачи:**
   - Обновляй статус в Project
   - Комментируй в Issue
   - Переходи к следующему этапу


