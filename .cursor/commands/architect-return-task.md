# Architect: Return Task

Возврат задачи предыдущему агенту если что-то не готово.

## 🚫 Причины возврата

### 1. Идея не детализирована

**Проверка:**
- Нет четкого описания функциональности
- Непонятно что именно нужно реализовать
- Противоречия в требованиях
- Отсутствует лор/контекст (для game-design задач)

**Если проблема:**
- Update: Status `Returned`, Agent `Idea`

### 2. Это не системная задача

**Проверка labels:**
- `ui`, `ux`, `client` → это UI/UX задача напрямую
- `canon`, `lore`, `quest` → это контент-квест напрямую

**Если проблема:**
- Update: Status `Todo`, Agent `UI/UX` или `Content`

### 3. Задача уже реализована

**Проверка:**
- Архитектура уже существует
- Функциональность уже в коде
- Дублирует другую задачу

**Если проблема:**
- Закрыть Issue как дубликат
- Или Update Status to `Done`

## WARNING Как вернуть задачу

### Шаблон возврата к Idea Writer

```javascript
// 1. Обновить статус
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'c01c12e9' }, // Status: Returned
    { id: 243899542, value: '8c3f5f11' }, // Agent: Idea
  ]
});

// 2. Добавить комментарий с причиной
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: 'WARNING **Task returned: Idea not detailed enough**\n\n' +
        '**Problems found:**\n' +
        '- Unclear what exact functionality is needed\n' +
        '- Missing use cases and user stories\n' +
        '- Contradictory requirements in description\n\n' +
        '**Expected:**\n' +
        '- Detailed description of functionality\n' +
        '- Use cases (how users will interact)\n' +
        '- Clear requirements and acceptance criteria\n' +
        '- Game mechanics description (for game features)\n\n' +
        '**Correct agent:** Idea Writer\n\n' +
        '**Status updated:** Returned (Agent: Idea)\n\n' +
        'Issue: #' + issue_number
});
```

### Переадресация к UI/UX

```javascript
// 1. Обновить статус
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'f75ad846' }, // Status: Todo
    { id: 243899542, value: '98c65039' }, // Agent: UI/UX
  ]
});

// 2. Добавить комментарий
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: '➡️ **Task redirected: UI/UX task detected**\n\n' +
        '**Reason:**\n' +
        '- This is UI/UX task (labels: ui, ux, client)\n' +
        '- No architecture needed, only UI design\n\n' +
        '**Correct agent:** UI/UX Designer\n\n' +
        '**Status updated:** Todo (Agent: UI/UX)\n\n' +
        'Issue: #' + issue_number
});
```

### Переадресация к Content Writer

```javascript
// 1. Обновить статус
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'f75ad846' }, // Status: Todo
    { id: 243899542, value: 'd3cae8d8' }, // Agent: Content
  ]
});

// 2. Добавить комментарий
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: '➡️ **Task redirected: Content quest detected**\n\n' +
        '**Reason:**\n' +
        '- This is content quest (labels: canon, lore, quest)\n' +
        '- Quest system architecture already exists\n' +
        '- Content Writer will create YAML quest file\n\n' +
        '**Correct agent:** Content Writer\n\n' +
        '**Status updated:** Todo (Agent: Content)\n\n' +
        'Issue: #' + issue_number
});
```

## 📊 ID статусов для возврата/переадресации

```javascript
const STATUS_IDS = {
  Status: {
    Todo: 'f75ad846',
    Returned: 'c01c12e9',
  },
  Agent: {
    Idea: '8c3f5f11',
    'UI/UX': '98c65039',
    Content: 'd3cae8d8',
  }
};
```

## OK После возврата

1. **НЕ продолжай работу** над задачей
2. Дождись когда Idea Writer доработает идею
3. Задача вернется к тебе с обновленным описанием
4. Переключись на другую задачу из `Architect - Todo`

## 🔄 Лимит возвратов

**ВАЖНО:** Максимум **2 возврата** между Architect и Idea Writer.

Если задача возвращается 3-й раз:
1. Update Status to `Architect - Blocked`
2. Создать встречу/обсуждение для выяснения требований
3. Привлечь product owner или tech lead

