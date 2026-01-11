# 🔄 КРАТКИЙ ГАЙД: Изменение статусов задач

**Для агентов NECPGAME - как правильно менять статусы через MCP GitHub**

## 🎯 ОСНОВНЫЕ ПРАВИЛА

- ✅ **ВСЕГДА** использовать MCP GitHub для изменения статусов
- ❌ **НИКОГДА** не использовать лейблы (status:in-progress, status:done)
- ❌ **НИКОГДА** не использовать GitHub CLI для изменения статусов

## 📋 ШАБЛОНЫ КОДА

### Взятие задачи (In Progress)
```javascript
// 1. Найти item_id
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
    id: '239690516', // Status field
    value: '83d488e7' // In Progress
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
// Изменить статус на Todo и назначить агента
await mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: item.id,
  updated_field: [
    {
      id: '239690516', // Status field
      value: 'f75ad846' // Todo
    },
    {
      id: '243899542', // Agent field
      value: nextAgentId // ID следующего агента
    }
  ]
});

// Добавить комментарий
await mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issueNumber,
  body: `[OK] Work completed. Handed off to ${nextAgentName}. Issue: #${issueNumber}`
});
```

### Завершение задачи (Done)
```javascript
// Изменить статус на Done
await mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: item.id,
  updated_field: {
    id: '239690516', // Status field
    value: '98236657' // Done
  }
});

// Добавить комментарий
await mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issueNumber,
  body: 'Task completed successfully'
});
```

## 🔢 ID значений полей

### Status Field (239690516)
- **Todo**: `f75ad846`
- **In Progress**: `83d488e7`
- **Review**: `55060662`
- **Blocked**: `af634d5b`
- **Returned**: `c01c12e9`
- **Done**: `98236657`

### Agent Field (243899542)
- **Backend**: `1fc13998`
- **Network**: `c60ebab1`
- **Security**: `12586c50`
- **DevOps**: `7e67a39b`
- **QA**: `3352c488`
- **Idea**: `8c3f5f11`
- **Content**: `d3cae8d8`
- **Architect**: `d109c7f9`
- **API**: `6aa5d9af`
- **DB**: `1e745162`
- **Performance**: `d16ede50`
- **UI/UX**: `98c65039`
- **UE5**: `56920475`
- **GameBalance**: `12e8fb71`
- **Release**: `f5878f68`

## ⚡ Универсальная функция

```javascript
async function changeTaskStatus(issueNumber, statusId, nextAgentId = null) {
  // Найти задачу
  const items = await mcp_github_list_project_items({
    owner_type: 'user',
    owner: 'gc-lover',
    project_number: 1,
    query: `number:${issueNumber}`
  });

  const item = items.items.find(item => item.content.number === issueNumber);
  if (!item) throw new Error(`Task #${issueNumber} not found`);

  // Обновить
  const updates = [{ id: '239690516', value: statusId }];
  if (nextAgentId) updates.push({ id: '243899542', value: nextAgentId });

  await mcp_github_update_project_item({
    owner_type: 'user',
    owner: 'gc-lover',
    project_number: 1,
    item_id: item.id,
    updated_field: updates
  });

  return item.id;
}

// Использование:
await changeTaskStatus(123, '83d488e7');                    // In Progress
await changeTaskStatus(123, 'f75ad846', 'c60ebab1');        // Todo + Network
await changeTaskStatus(123, '98236657');                    // Done
```

## 🚨 ВАЖНЫЕ ЗАМЕЧАНИЯ

1. **Всегда проверяйте item_id** - задача должна быть добавлена в проект
2. **Используйте правильные ID** - копируйте из таблицы выше
3. **Добавляйте комментарии** - обязательно после изменения статуса
4. **Не используйте лейблы** - они не влияют на GitHub Projects

## 📚 Ссылки

- `MCP_GITHUB_GUIDE.md` - полный гайд с примерами
- `AGENT_QUICK_START.md` - общий workflow агентов