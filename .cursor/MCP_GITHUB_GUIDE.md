# MCP GitHub Integration Guide

**Единый гайд по работе с GitHub Projects через MCP для Cursor IDE и Antigravity**

## 🎯 КРИТИЧНЫЕ ТРЕБОВАНИЯ

**Агенты ОБЯЗАНЫ:**
- Использовать MCP для всех операций с GitHub Projects
- Менять статусы задач ПОСЛЕ исполнения
- Назначать задачи следующему агенту по workflow
- НЕ создавать мусорные файлы в корне проекта
- НЕ создавать лишние отчеты

---

## ⚙️ КОНФИГУРАЦИЯ ПРОЕКТА

**Project Parameters:**
- **Owner Type:** `user`
- **Owner:** `gc-lover`
- **Project Number:** `1`
- **Project Node ID:** `PVT_kwHODCWAw84BIyie`
- **Repository:** `gc-lover/necpgame-monorepo`

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

## 📋 Основные MCP команды

### Поиск задач агента
```javascript
// В Cursor IDE: MCP сервер cursor-github
// В Antigravity: аналогичный MCP сервер
mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Agent:"Backend" Status:"Todo"'
});
```

**Response:**
```json
{
  "items": [
    {
      "id": "PVTI_lAHODCWAw84BIyiezg8JzKw",
      "number": 123,
      "title": "Implement combat service API",
      "status": "Todo",
      "agent": "Backend"
    }
  ]
}
```

### Взятие задачи в работу
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: 'PVTI_lAHODCWAw84BIyiezg8JzKw',
  updated_field: [
    {id: '239690516', value: '83d488e7'}, // Status: In Progress
    {id: '243899542', value: '1fc13998'}, // Agent: Backend
    {id: '246469155', value: '08174330'}, // Type: BACKEND
    {id: '246468990', value: '22932cc7'}  // Check: 0 (unchecked)
  ]
});
```

### Передача задачи следующему агенту
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: 'PVTI_lAHODCWAw84BIyiezg8JzKw',
  updated_field: [
    {id: '239690516', value: 'f75ad846'}, // Status: Todo
    {id: '243899542', value: 'c60ebab1'}, // Agent: Network
    {id: '246469155', value: '08174330'}, // Type: BACKEND (сохранить)
    {id: '246468990', value: '4e8cf8f5'}  // Check: 1 (validated)
  ]
});

// ОБЯЗАТЕЛЬНО добавить комментарий
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: 123,
  body: '[OK] Backend implementation complete. Handed off to Network.\\n\\nIssue: #123'
});
```

---

## 🔑 Field IDs (КРИТИЧНО знать!)

### Status Field (239690516)
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

### Agent Field (243899542)
```javascript
const AGENT_OPTIONS = {
  'Backend': '1fc13998',
  'Network': 'c60ebab1',
  'Security': '12586c50',
  'DevOps': '7e67a39b',
  'QA': '3352c488'
  // Полный список в GITHUB_PROJECT_CONFIG.md
};
```

### Type Field (246469155)
```javascript
const TYPE_OPTIONS = {
  'API': '66f88b2c',         // OpenAPI спецификации
  'MIGRATION': 'd3702826',   // БД миграции
  'DATA': 'b06014a2',         // Импорт данных
  'BACKEND': '08174330',      // Go код
  'UE5': 'd4d523a0'           // Unreal Engine
};
```

### Check Field (246468990)
```javascript
const CHECK_OPTIONS = {
  '0': '22932cc7', // NOT_CHECKED
  '1': '4e8cf8f5'  // CHECKED
};
```

---

## 🔄 Полный workflow агента

### 1. Найти задачу
```javascript
const tasks = await mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Agent:"Backend" Status:"Todo"'
});
```

### 2. Взять задачу
```javascript
const task = tasks.items[0];
await mcp_github_update_project_item({
  // ... обновить статус на In Progress, назначить агента
});
```

### 3. Выполнить работу
- Реализовать функционал
- Запустить валидацию
- Сделать коммит

### 4. Передать следующему агенту
```javascript
await mcp_github_update_project_item({
  // ... обновить статус на Todo, назначить следующего агента
});

await mcp_github_add_issue_comment({
  // ... добавить комментарий с результатами
});
```

---

## 🚨 Обработка ошибок

### Задача не найдена
- Проверить корректность query
- Проверить права доступа
- Проверить что проект существует

### Поля не обновляются
- Проверить Field IDs в `GITHUB_PROJECT_CONFIG.md`
- Проверить что item_id корректный
- Проверить права на запись

### Комментарий не добавляется
- Проверить issue_number (не item_id!)
- Проверить права на issues

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
- Использует MCP сервер `cursor-github`
- Все команды работают через MCP интерфейс
- Интеграция с IDE для seamless workflow

### Antigravity
- Аналогичные MCP команды
- Поддержка всех GitHub Project операций
- Compatible API для enterprise использования

---

## ⚡ Быстрые команды

### Поиск задач
```bash
# Через скрипт (альтернатива MCP)
python scripts/update-github-fields.py --find --agent Backend
```

### Обновление статуса
```bash
# Через скрипт
python scripts/update-github-fields.py --item-id 123 --status in_progress --agent Backend
```

### Передача задачи
```bash
# Через скрипт
python scripts/update-github-fields.py --item-id 123 --status todo --agent Network
```

---

## 📚 Ссылки

- `GITHUB_PROJECT_CONFIG.md` - все Field IDs и опции
- `AGENT_SIMPLE_GUIDE.md` - общий workflow
- `CONTENT_WORKFLOW.md` - для контентных задач