# Agent Commands Reference

Команды для агентов NECPGAME проекта.

## 📂 Структура

### Общие команды
- `agent-main-prompt.md` - **ОСНОВНОЙ ПРОМПТ** для работы всех агентов
- `github-integration.md` - **ОБЯЗАТЕЛЬНО** для работы с GitHub Issues через CLI
- `common-validation.md` - валидация кода и спецификаций

### Специфические команды агентов
Большинство команд устарели. Используйте GitHub CLI для всех операций.

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

**Детали:** `github-integration.md`

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
| **MCP GitHub** | ✅ Активно | Основной способ работы с задачами через Projects API |
| **Валидация** | ✅ Активно | Обязательно для всех агентов |
| **GitHub CLI** | ❌ Устарело | НЕ использовать для изменения статусов (только лейблы) |
| **Специфические** | ⚠️ Устарело | Использовать MCP вместо скриптов |

## 🚀 Быстрый старт

1. **agent-main-prompt.md** - **ОСНОВНОЙ ПРОМПТ** для автономной работы агентов
2. **AGENT_QUICK_START.md** - главный гайд для агентов
3. **github-integration.md** - команды MCP для работы с GitHub Projects
4. **common-validation.md** - валидация кода

Все операции через MCP GitHub - никаких GitHub CLI команд для статусов.