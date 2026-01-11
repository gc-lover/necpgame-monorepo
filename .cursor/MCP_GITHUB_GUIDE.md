# GitHub CLI Integration Guide

**Единый гайд по работе с GitHub Issues через GitHub CLI для Cursor IDE**

## 🎯 КРИТИЧНЫЕ ТРЕБОВАНИЯ

**Агенты ОБЯЗАНЫ:**
- Использовать **MCP GitHub** для изменения полей в GitHub Projects
- Менять статусы задач через поле Status (Todo → In Progress → Done)
- Назначать задачи следующему агенту через поле Agent
- НЕ использовать лейблы для изменения статуса (работает только через Projects API)
- НЕ создавать мусорные файлы в корне проекта

---

## ⚙️ КОНФИГУРАЦИЯ ПРОЕКТА

**Repository Parameters:**
- **Owner:** `gc-lover`
- **Repository:** `necpgame-monorepo`
- **GitHub CLI:** Должен быть установлен и настроен (`gh auth status`)

**Field IDs для GitHub Projects (ОБЯЗАТЕЛЬНО знать!):**

**Status Field (239690516):**
- Todo: `f75ad846`
- In Progress: `83d488e7`
- Review: `55060662`
- Blocked: `af634d5b`
- Returned: `c01c12e9`
- Done: `98236657`

**Agent Field (243899542):**
- Backend: `1fc13998`
- Network: `c60ebab1`
- Security: `12586c50`
- DevOps: `7e67a39b`
- QA: `3352c488`
- Idea: `8c3f5f11`
- Content: `d3cae8d8`
- Architect: `d109c7f9`
- API: `6aa5d9af`
- DB: `1e745162`
- Performance: `d16ede50`
- UI/UX: `98c65039`
- UE5: `56920475`
- GameBalance: `12e8fb71`
- Release: `f5878f68`

### Field IDs (КРИТИЧНО знать!)

**Status Field (239690516)**
```javascript
const STATUS_OPTIONS = {
  'Todo': 'f75ad846',
  'In Progress': '83d488e7',
  'Review': '55060662',
  'Blocked': 'af634d5b',
  'Returned': 'c01c12e9',
  'Done': '98236657'
};
```

**Agent Field (243899542)**
```javascript
const AGENT_OPTIONS = {
  'Backend': '1fc13998',
  'Network': 'c60ebab1',
  'Security': '12586c50',
  'DevOps': '7e67a39b',
  'QA': '3352c488',
  'Idea': '8c3f5f11',
  'Content': 'd3cae8d8',
  'Architect': 'd109c7f9',
  'API': '6aa5d9af',
  'DB': '1e745162',
  'Performance': 'd16ede50',
  'UI/UX': '98c65039',
  'UE5': '56920475',
  'GameBalance': '12e8fb71',
  'Release': 'f5878f68'
};
```

**Type Field (246469155)**
```javascript
const TYPE_OPTIONS = {
  'API': '66f88b2c',         // OpenAPI спецификации
  'MIGRATION': 'd3702826',   // БД миграции
  'DATA': 'b06014a2',         // Импорт данных
  'BACKEND': '08174330',      // Go код
  'UE5': 'd4d523a0'           // Unreal Engine
};
```

**Check Field (246468990)**
```javascript
const CHECK_OPTIONS = {
  '0': '22932cc7', // NOT_CHECKED
  '1': '4e8cf8f5'  // CHECKED
};
```

---

## 📋 Основные GitHub CLI команды

### Поиск задач агента
```bash
# В терминале: GitHub CLI

# МЕТОД 1: Поиск по лейблу агента (рекомендуемый)
gh issue list --repo gc-lover/necpgame-monorepo --state open --label 'agent:backend'

# МЕТОД 2: Поиск по названию (альтернативный)
gh issue list --repo gc-lover/necpgame-monorepo --state open | grep "\[Backend\]"
```

**Output:**
```
2296	OPEN	[Backend] Implement crafting-network-service-go with enterprise-grade real-time combat	agent:qa, status:ready-for-qa	2026-01-11T13:53:30Z
2294	OPEN	[Backend] Implement combat-system-service-go with enterprise-grade real-time combat	agent:qa, status:ready-for-qa	2026-01-11T13:43:00Z
```

### Взятие задачи в работу
```javascript
// 1. Найти item_id задачи в проекте
const items = await mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: `number:${issueNumber}`
});

const item = items.items.find(item => item.content.number === issueNumber);

// 2. Изменить статус на In Progress (83d488e7)
await mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: item.id,
  updated_field: {
    id: '239690516', // Status field ID
    value: '83d488e7' // In Progress option ID
  }
});

// 3. Добавить комментарий
await mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issueNumber,
  body: '[OK] Начинаю работу над задачей'
});
```

### Передача задачи следующему агенту
```javascript
// 1. Изменить статус на Todo и назначить следующего агента
await mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: item.id,
  updated_field: [
    {
      id: '239690516', // Status field ID
      value: 'f75ad846' // Todo option ID
    },
    {
      id: '243899542', // Agent field ID
      value: agentOptionId // ID следующего агента (например, 'c60ebab1' для Network)
    }
  ]
});

// 2. Добавить комментарий о передаче
await mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issueNumber,
  body: `[OK] Work completed. Handed off to NextAgent. Issue: #${issueNumber}`
});
```

### Завершение задачи
```javascript
// 1. Изменить статус на Done (98236657)
await mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: item.id,
  updated_field: {
    id: '239690516', // Status field ID
    value: '98236657' // Done option ID
  }
});

// 2. Добавить финальный комментарий
await mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issueNumber,
  body: 'Task completed successfully'
});
```

---

## 🛠️ ШАБЛОНЫ КОДА ДЛЯ АГЕНТОВ

### Универсальный шаблон для изменения статуса
```javascript
// Копировать и использовать в любом агенте
async function changeTaskStatus(issueNumber, newStatus, nextAgentId = null) {
  // Найти задачу в проекте
  const items = await mcp_github_list_project_items({
    owner_type: 'user',
    owner: 'gc-lover',
    project_number: 1,
    query: `number:${issueNumber}`
  });

  const item = items.items.find(item => item.content.number === issueNumber);
  if (!item) {
    throw new Error(`Task #${issueNumber} not found in project`);
  }

  // Подготовить поля для обновления
  const updates = [{
    id: '239690516', // Status field
    value: newStatus
  }];

  // Добавить следующего агента если указан
  if (nextAgentId) {
    updates.push({
      id: '243899542', // Agent field
      value: nextAgentId
    });
  }

  // Обновить задачу
  await mcp_github_update_project_item({
    owner_type: 'user',
    owner: 'gc-lover',
    project_number: 1,
    item_id: item.id,
    updated_field: updates
  });

  return item.id;
}

// Примеры использования:
await changeTaskStatus(123, '83d488e7'); // In Progress
await changeTaskStatus(123, 'f75ad846', 'c60ebab1'); // Todo + Network agent
await changeTaskStatus(123, '98236657'); // Done
```

### Взятие задачи (шаблон)
```javascript
async function takeTask(issueNumber) {
  const itemId = await changeTaskStatus(issueNumber, '83d488e7'); // In Progress

  await mcp_github_add_issue_comment({
    owner: 'gc-lover',
    repo: 'necpgame-monorepo',
    issue_number: issueNumber,
    body: '[OK] Начинаю работу над задачей'
  });

  return itemId;
}
```

### Передача задачи (шаблон)
```javascript
async function handoffTask(issueNumber, nextAgentId, nextAgentName) {
  await changeTaskStatus(issueNumber, 'f75ad846', nextAgentId); // Todo + agent

  await mcp_github_add_issue_comment({
    owner: 'gc-lover',
    repo: 'necpgame-monorepo',
    issue_number: issueNumber,
    body: `[OK] Work completed. Handed off to ${nextAgentName}. Issue: #${issueNumber}`
  });
}
```

### Завершение задачи (шаблон)
```javascript
async function completeTask(issueNumber) {
  await changeTaskStatus(issueNumber, '98236657'); // Done

  await mcp_github_add_issue_comment({
    owner: 'gc-lover',
    repo: 'necpgame-monorepo',
    issue_number: issueNumber,
    body: 'Task completed successfully'
  });
}
```

---

## 🏷️ Labels Reference (КРИТИЧНО знать!)

### Agent Labels
```bash
# Назначение агентов через лейблы
agent:backend      # Backend агент
agent:api          # API Designer
agent:database     # Database агент
agent:network      # Network агент
agent:security     # Security агент
agent:devops       # DevOps агент
agent:qa           # QA агент
agent:performance  # Performance агент
agent:ue5          # UE5 агент
agent:content      # Content Writer
agent:architect    # Architect
agent:idea         # Idea Writer
agent:ui-ux        # UI/UX Designer
agent:game-balance # Game Balance агент
agent:release      # Release агент
```

### Status Labels
```bash
# Статусы задач через лейблы
status:todo        # Новые задачи (опционально)
status:in-progress # В работе
status:review      # На ревью
status:blocked     # Заблокированы
status:returned    # Возвращены на доработку
# status:done - не нужен, issue закрывается
```

### Type Labels (опционально)
```bash
# Типы задач (для дополнительной классификации)
type:api           # OpenAPI спецификации
type:migration     # БД миграции
type:data          # Импорт данных
type:backend       # Go код
type:ue5           # Unreal Engine
```

---

## 🔄 Полный workflow агента

### 1. Найти задачу
```bash
# Найти задачи своего агента
gh issue list --repo gc-lover/necpgame-monorepo --state open --label 'agent:backend'
```

### 2. Взять задачу
```bash
# Добавить комментарий о начале работы
gh issue comment 123 --body '[OK] Начинаю работу над задачей'

# Добавить лейбл статуса
gh issue edit 123 --add-label 'status:in-progress'
```

### 3. Выполнить работу
- Реализовать функционал
- Запустить валидацию
- Сделать коммит

### 4. Передать следующему агенту
```bash
# Добавить комментарий о передаче
gh issue comment 123 --body '[OK] Work completed. Handed off to Network. Issue: #123'

# Обновить лейблы
gh issue edit 123 --remove-label 'status:in-progress' --add-label 'agent:network'
```

### 5. Закрыть задачу (для финальных агентов)
```bash
# Для завершенных задач
gh issue close 123 --comment 'Task completed successfully'
```

---

## 🚨 Обработка ошибок

### Задача не найдена
- Проверить корректность лейбла агента (`agent:{name}`)
- Проверить состояние issue (`--state open`)
- Проверить права доступа к репозиторию

### Лейбл не добавляется
- Проверить что лейбл существует в репозитории
- Проверить права на редактирование issues
- Создать лейбл если его нет: `gh label create {name}`

### Комментарий не добавляется
- Проверить номер issue
- Проверить права на комментирование
- Проверить аутентификацию: `gh auth status`

---

## 💡 Лучшие практики

### Всегда проверять перед обновлением
```javascript
const currentTask = await mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: `number:${task.number}`
});
```

### Использовать транзакции
```javascript
const updates = [
  {id: STATUS_FIELD_ID, value: STATUS_OPTIONS['In Progress']},
  {id: AGENT_FIELD_ID, value: AGENT_OPTIONS.Backend}
];

await mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: task.id,
  updated_field: updates
});
```

### Правильные комментарии
```javascript
const comment = `[OK] ${description}. Handed off to ${nextAgent}.\\n\\nIssue: #${issueNumber}`;
```

---

## 🎯 Поддержка сред

### Cursor IDE
- Использует GitHub CLI в терминале
- Все команды работают через командную строку
- Интеграция с IDE через терминал для seamless workflow

### Другие среды
- Любая среда с установленным GitHub CLI
- Работает в bash, zsh, PowerShell, cmd
- Compatible API для enterprise использования

---

## ⚡ Быстрые команды

### Поиск задач
```bash
# Прямой поиск через GitHub CLI
gh issue list --repo gc-lover/necpgame-monorepo --state open --label 'agent:backend'
```

### Взятие задачи
```bash
# Две команды для взятия задачи
gh issue comment 123 --body '[OK] Начинаю работу над задачей' && gh issue edit 123 --add-label 'status:in-progress'
```

### Передача задачи
```bash
# Две команды для передачи
gh issue comment 123 --body '[OK] Work completed. Handed off to Network. Issue: #123' && gh issue edit 123 --remove-label 'status:in-progress' --add-label 'agent:network'
```

---

## 📚 Ссылки

- `github-integration.md` - команды GitHub CLI для работы с issues
- `AGENT_QUICK_START.md` - общий workflow агентов
- `CONTENT_WORKFLOW.md` - для контентных задач
- `common-validation.md` - валидация и общие команды