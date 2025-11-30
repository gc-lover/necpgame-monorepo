# Формат обновления статуса

**ВАЖНО:** Все команды используют одинаковый формат обновления статуса.

## Правильный формат

```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,  // число, не строка!
    value: '02b1119e'  // id опции статуса, не название!
  }
});
```

## Как получить id опции статуса

```javascript
// 1. Получить список полей проекта
const fields = await mcp_github_list_project_fields({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1
});

// 2. Найти поле Status (id: 239690516)
const statusField = fields.fields.find(f => f.id === 239690516);

// 3. Найти нужную опцию в options
const option = statusField.options.find(o => o.name === 'Architect - In Progress');

// 4. Использовать option.id в value
```

## Основные id опций статусов

- "Architect - In Progress": `02b1119e`
- "API Designer - In Progress": `ff20e8f2`
- "Backend - In Progress": `7bc9d20f`
- "Database - In Progress": `91d49623`
- "Idea Writer - In Progress": `d9960d37`
- "Content Writer - In Progress": `cf5cf6bb`
- "QA - In Progress": `251c89a6`
- "UE5 - In Progress": `9396f45a`
- "UI/UX - In Progress": `dae97d56`

Полный список: `mcp_github_list_project_fields`

## Обновление всех команд

Все команды в `.cursor/commands/` должны использовать этот формат.
Если в команде используется `id: '239690516'` или `value: '{Status Name}'` - обновить на правильный формат.

