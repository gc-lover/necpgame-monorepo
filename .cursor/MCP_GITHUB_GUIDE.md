# MCP GitHub Guide

**Единый гайд по работе с GitHub Projects через MCP GitHub API**

## КРИТИЧНЫЕ ТРЕБОВАНИЯ

**Агенты ОБЯЗАНЫ:**
- Использовать **MCP GitHub** для изменения полей в GitHub Projects
- Менять статусы задач через поле Status (Todo → In Progress → Done)
- Назначать задачи следующему агенту через поле Agent
- НЕ использовать лейблы для изменения статуса (работает только через Projects API)
- НЕ создавать мусорные файлы в корне проекта

---

## КОНФИГУРАЦИЯ ПРОЕКТА

**Repository Parameters:**
- **Owner:** `gc-lover`
- **Repository:** `necpgame-monorepo`
- **GitHub CLI:** Должен быть установлен и настроен (`gh auth status`)

**Field IDs для GitHub Projects:**

См. `@.cursor/GITHUB_PROJECT_FIELD_IDS.md` - единый источник всех Field IDs.

---

## Поиск задач (Комбинированный подход)

**ВАЖНО:** MCP GitHub Projects API может не корректно фильтровать задачи.
Используй **комбинированный подход:**
1. **GH CLI** для быстрого поиска открытых задач
2. **MCP GitHub** для получения деталей и обновления статусов

### Шаг 1: Поиск через GH CLI (быстрый просмотр)

```bash
# Поиск открытых задач
gh issue list --repo gc-lover/necpgame-monorepo --state open --limit 30 --json number,title,state

# Поиск по префиксу
gh issue list --repo gc-lover/necpgame-monorepo --state open | grep "\[Backend\]"

# Поиск по лейблу (если используются)
gh issue list --repo gc-lover/necpgame-monorepo --state open --label "agent:backend"
```

**Преимущества GH CLI:**
- Быстрый поиск по номеру, заголовку, лейблам
- Получение актуального состояния Issue (open/closed)
- Простая фильтрация по префиксам в названии

**Ограничения GH CLI:**
- Не показывает статус в Projects (Todo/In Progress/Done)
- Не показывает поле Agent из Projects
- Не позволяет обновлять статусы в Projects

### Шаг 2: Получение деталей через MCP GitHub

После нахождения задачи через GH CLI, получаем детали через MCP:

```javascript
// 1. Получить Issue детали
const issue = await mcp_github_issue_read({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issueNumber,
  method: 'get'
});

// 2. Найти задачу в проекте по номеру Issue
const projectItems = await mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: `number:${issueNumber}`,
  fields: []
});

const item = projectItems.items.find(item => item.content.number === issueNumber);

// 3. Если задача найдена - получить её поля (Status, Agent)
if (item) {
  const itemDetails = await mcp_github_get_project_item({
    owner_type: 'user',
    owner: 'gc-lover',
    project_number: 1,
    item_id: item.id,
    fields: []
  });
  
  // Извлечь статус и агента из полей
  const statusField = itemDetails.fields.find(f => f.id === '239690516');
  const currentStatus = statusField?.value;
}
```

### Шаг 3: ОБЯЗАТЕЛЬНАЯ проверка актуальности задачи

**КРИТИЧНО: Статусы задач в Projects могут не совпадать с реальностью!**
**ПЕРЕД взятием задачи в работу агент ОБЯЗАН:**

1. **Проверить состояние Issue:**
   ```javascript
   const issueClosed = (issue.state === 'closed');
   ```

2. **Проверить реальное состояние кода в репозитории:**
   - Найти файлы, которые должны быть изменены
   - Проверить что функционал ещё не реализован
   - Убедиться что изменения действительно нужны

3. **Синхронизировать статусы:**
   - Если Issue закрыта → статус в Projects должен быть Done
   - Если код реализован → статус в Projects должен быть Done, Issue должна быть закрыта
   - Если и Issue закрыта, и код реализован → статус в Projects должен быть Done

**ТОЛЬКО ЕСЛИ задача актуальна, Issue открыта и код не реализован** - брать её в работу.

---

## Изменение статусов (MCP GitHub)

**ТОЛЬКО MCP GitHub для обновления статусов в Projects!**

### Взятие задачи (In Progress)

```javascript
// 1. Найти item_id задачи в проекте
const items = await mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: `number:${issueNumber}`
});

const item = items.items.find(item => item.content.number === issueNumber);

// 2. Изменить статус на In Progress
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

### Передача задачи (Todo + следующий агент)

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

### Завершение задачи (Done)

```javascript
// 1. Изменить статус на Done
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

// 2. Закрыть Issue
await mcp_github_issue_write({
  method: 'update',
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issueNumber,
  state: 'closed',
  state_reason: 'completed'
});

// 3. Добавить финальный комментарий
await mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issueNumber,
  body: 'Task completed successfully'
});
```

---

## Шаблоны кода для агентов

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

  await mcp_github_issue_write({
    method: 'update',
    owner: 'gc-lover',
    repo: 'necpgame-monorepo',
    issue_number: issueNumber,
    state: 'closed',
    state_reason: 'completed'
  });

  await mcp_github_add_issue_comment({
    owner: 'gc-lover',
    repo: 'necpgame-monorepo',
    issue_number: issueNumber,
    body: 'Task completed successfully'
  });
}
```

---

## Полный workflow агента

**ВАЖНО:** Используй комбинированный подход!

Краткий алгоритм:
1. **Найти задачу** через GH CLI
2. **Получить детали** через MCP GitHub
3. **Проверить актуальность** задачи (Issue состояние, код в репозитории)
4. **Взять задачу** через MCP (статус In Progress)
5. **Работать** над задачей
6. **Передать** через MCP (статус Todo + следующий агент)
7. **Завершить** через MCP (статус Done + закрыть Issue)

---

## Field IDs Reference

**ВАЖНО:** Все Field IDs определены в `@.cursor/GITHUB_PROJECT_FIELD_IDS.md`

Используй эту ссылку вместо дублирования Field IDs.

**См. `@.cursor/GITHUB_PROJECT_FIELD_IDS.md` для полного списка:**
- Status Field (239690516)
- Agent Field (243899542)
- Type Field (246469155)
- Check Field (246468990)

---

## Troubleshooting

### Проблема: Задача не найдена в проекте через `query: "number:X"`

**Решение:**
1. Проверь что задача действительно в проекте (через веб-интерфейс GitHub)
2. Попробуй поиск без фильтра: `query: ''`
3. Используй GH CLI для поиска, затем найди `item_id` через `list_project_items` без фильтра

### Проблема: Поля Status/Agent не возвращаются

**Решение:**
1. Используй `get_project_item` с конкретным `item_id`
2. Проверь что поля существуют через `list_project_fields`
3. Используй GH CLI для получения базовой информации о задаче

### Проблема: Фильтры в query не работают

**Решение:**
1. Используй GH CLI для предварительной фильтрации
2. Затем используй MCP для получения деталей и обновления статусов
3. Комбинируй оба подхода для надёжного workflow

---

## Ссылки

- `AGENT_QUICK_START.md` - общий workflow агентов
- `GITHUB_PROJECT_FIELD_IDS.md` - Field IDs для Projects
- `CONTENT_WORKFLOW.md` - для контентных задач
- `common-validation.md` - валидация кода
