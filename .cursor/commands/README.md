# Agent Commands Reference

Команды для агентов NECPGAME проекта.

## 📂 Структура

### Общие команды
- `agent-main-prompt.md` - **ОСНОВНОЙ ПРОМПТ** для работы всех агентов
- `common-validation.md` - валидация кода и спецификаций

### GitHub интеграция (в корне .cursor/)
- `MCP_GITHUB_GUIDE.md` - работа с MCP GitHub API (поиск, статусы, workflow)
- `GITHUB_PROJECT_FIELD_IDS.md` - Field IDs для Projects

**УСТАРЕЛО:**
- `github-integration.md` - устарело, используй комбинированный подход

## 🔧 Основные команды

### Работа с GitHub Projects (MCP)
```javascript
// Поиск задач
await mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Agent:"Backend" Status:"Todo"'
});

// Взятие задачи (In Progress)
await mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: itemId,
  updated_field: {
    id: '239690516', // Status field
    value: '83d488e7' // In Progress
  }
});

// Передача задачи
await mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: itemId,
  updated_field: [
    {
      id: '239690516', // Status field
      value: 'f75ad846' // Todo
    },
    {
      id: '243899542', // Agent field
      value: 'c60ebab1' // Network agent
    }
  ]
});
```

**Детали:** `@.cursor/MCP_GITHUB_GUIDE.md`

### Валидация
```bash
# Запрет эмодзи
python scripts/validation/validate-emoji-ban.py .

# OpenAPI домены
python scripts/openapi/validate-domains-openapi.py

# Миграции БД
python scripts/migrations/validate-all-migrations.py
```

**Детали:** `common-validation.md`

## 📋 Статус команд

| Категория | Статус | Комментарий |
|-----------|--------|-------------|
| **MCP GitHub** | ✅ Активно | Обновление статусов в Projects (ОБЯЗАТЕЛЬНО!) |
| **GH CLI** | ✅ Активно | Поиск задач (только для поиска!) |
| **Валидация** | ✅ Активно | Обязательно для всех агентов |
| **Комбинированный** | ✅ Активно | GH CLI для поиска + MCP для статусов |

## 🚀 Быстрый старт

1. **agent-main-prompt.md** - **ОСНОВНОЙ ПРОМПТ** для автономной работы агентов
2. **AGENT_QUICK_START.md** - главный гайд для агентов
3. **MCP_GITHUB_GUIDE.md** - работа с MCP GitHub API (поиск, статусы, workflow)
4. **common-validation.md** - валидация кода

**ВАЖНО:** Комбинированный подход - GH CLI для поиска, MCP GitHub для статусов!