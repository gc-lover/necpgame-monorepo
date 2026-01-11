# 🚀 Агент NECPGAME - Быстрый старт

**Единый гайд для всех агентов проекта**

## 🎯 КРИТИЧНЫЕ ТРЕБОВАНИЯ

**Агенты ОБЯЗАНЫ:**
- ✅ Использовать **КОМБИНИРОВАННЫЙ подход**: GH CLI для поиска, MCP GitHub для обновления статусов
- ✅ Менять статусы через **MCP GitHub** (ПОЛЯ Projects, НЕ лейблы!)
- ✅ Назначать задачи **следующему агенту** по workflow
- ❌ **НЕ создавать** мусорные файлы в корне проекта
- ❌ **НЕ создавать** лишние отчеты

---

## 🔍 Как найти свою задачу (КРИТИЧНО!)

**ВАЖНО:** Используй комбинированный подход:
1. **GH CLI** для быстрого поиска открытых задач
2. **MCP GitHub** для получения деталей и обновления статусов в Projects

### Правильные команды поиска задач:

```bash
# ШАГ 1: GH CLI для поиска (быстрый просмотр)
gh issue list --repo gc-lover/necpgame-monorepo --state open --limit 30 --json number,title,state

# Поиск по префиксу в названии
gh issue list --repo gc-lover/necpgame-monorepo --state open | grep "\[Backend\]"

# Поиск по лейблу (если используются)
gh issue list --repo gc-lover/necpgame-monorepo --state open --label 'agent:backend'
```

**ШАГ 2:** После нахождения задачи через GH CLI → использовать MCP GitHub для получения деталей и обновления статусов.

**Детальный workflow:** См. `@.cursor/MCP_GITHUB_GUIDE.md`

### Агенты и их префиксы в title:
- `[Backend]` - Backend агент
- `[API]` - API Designer
- `[Content]` - Content Writer
- `[QA]` - QA агент
- `[Performance]` - Performance агент
- `[Security]` - Security агент

## 📋 4 шага работы

### 1️⃣ НАЙТИ задачу

**ШАГ 1: GH CLI для поиска**
```bash
# Поиск открытых задач
gh issue list --repo gc-lover/necpgame-monorepo --state open --limit 30 --json number,title,state

# Поиск по префиксу
gh issue list --repo gc-lover/necpgame-monorepo --state open | grep "\[Backend\]"
```

**ШАГ 2: MCP GitHub для деталей и обновления статусов**
См. `@.cursor/MCP_GITHUB_GUIDE.md` для детального workflow.

### 2️⃣ ВЗЯТЬ задачу

**ТОЛЬКО через MCP GitHub!**

```javascript
// 1. Найти item_id через MCP
const items = await mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: `number:${issueNumber}`
});

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

**Детали:** `@.cursor/MCP_GITHUB_GUIDE.md`

### 3️⃣ РАБОТАТЬ
- Выполнить задачу согласно требованиям
- Следовать специфическим правилам агента
- Запустить валидацию

### 4️⃣ ПЕРЕДАТЬ

**ТОЛЬКО через MCP GitHub!**

```javascript
// 1. Изменить статус на Todo и назначить следующего агента
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

// 2. Добавить комментарий
await mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issueNumber,
  body: '[OK] Work completed. Handed off to NextAgent. Issue: #123'
});
```

**Field IDs:** `@.cursor/GITHUB_PROJECT_FIELD_IDS.md`

---

## 📚 ОБЯЗАТЕЛЬНЫЕ ссылки

### 🔧 MCP и GitHub
- **`MCP_GITHUB_GUIDE.md`** - ВСЕ команды MCP для работы с задачами + конфигурация проекта

### 📖 Общие гайды
- **`PERFORMANCE_ENFORCEMENT.md`** - требования к оптимизациям для всех агентов
- **`BACKEND_OPTIMIZATION_CHECKLIST.md`** - детальный чеклист для Backend
- **`CONTENT_WORKFLOW.md`** - процесс работы с контентом
- **`DOMAIN_REFERENCE.md`** - enterprise-grade домены

### 🔍 Валидация
- **`common-validation.md`** - команды валидации кода

### 👤 Специфические правила
- **`.cursor/rules/agent-{my-agent}.mdc`** - специфические инструкции для агента

---

## 🎮 Workflow по типам задач

| Тип задачи | Agent chain |
|------------|-------------|
| **Системные** | Idea → Architect → DB → API → Backend → Network → Security → DevOps → UE5 → QA → Release |
| **Контент** | Idea → Content → Backend (import) → QA → Release |
| **UI/UX** | Idea → UI/UX → UE5 → QA → Release |

---

## ⚡ Быстрые команды

### Комбинированный подход

**1. Поиск через GH CLI:**
```bash
gh issue list --repo gc-lover/necpgame-monorepo --state open --limit 30 --json number,title,state
```

**2. Обновление через MCP GitHub:**
См. `@.cursor/AGENT_STATUS_CHANGE_GUIDE.md` для полных примеров.

**Field IDs:** `@.cursor/GITHUB_PROJECT_FIELD_IDS.md`

### Валидация
```bash
# Запрет эмодзи
python scripts/validation/validate-emoji-ban.py .

# OpenAPI домены
python scripts/openapi/validate-domains-openapi.py
```

---

## 🚨 ЗАПРЕЩЕНО

- ❌ Эмодзи в коде (ломает Windows скрипты)
- ❌ Файлы в корне проекта (кроме README, CHANGELOG)
- ❌ Передача задач без изменения статуса
- ❌ Создание отчетов/сводок
- ❌ Использование item_id в комментариях (только #123)

---

## 🆘 Если проблема

1. **MCP_GITHUB_GUIDE.md** - все команды MCP GitHub (поиск, статусы, workflow)
2. **GITHUB_PROJECT_FIELD_IDS.md** - Field IDs для Projects
5. **agent-{name}.mdc** - специфические правила агента

**ВАЖНО:** 
- GH CLI для поиска задач
- MCP GitHub для обновления статусов в Projects
- Никаких лейблов для изменения статусов!