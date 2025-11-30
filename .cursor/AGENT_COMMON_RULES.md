# Common Agent Rules

## GitHub Project Configuration

**Project parameters:** See `.cursor/GITHUB_PROJECT_CONFIG.md`

Все агенты используют одинаковые параметры:
- `owner_type: 'user'`
- `owner: 'gc-lover'`
- `project_number: 1`
- `project_node_id: 'PVT_kwHODCWAw84BIyie'`
- `status_field_id: '239690516'`

## GitHub API

**ALWAYS use `mcp_github_search_issues` instead of multiple `mcp_github_issue_read`**
- Sequential requests: 300-500ms delay
- Batch operations: 5-10 Issues
- For >=10 Issues use GitHub Actions Batch Processor
- Cache results (TTL: 2-3 minutes)

## Task Identification

**ВАЖНО: Различие между ID задачи и номером Issue**

### Внутренний ID проекта (`item_id` / `project_item_id`)
- Это внутренний идентификатор элемента в GitHub Project
- Используется **ТОЛЬКО** для API вызовов (`mcp_github_update_project_item`, `list_project_items`)
- Не упоминается в комментариях, сообщениях или документации
- Получается из результата `list_project_items` (поле `id`)

### Номер Issue (`#123`)
- Это публичный номер Issue в GitHub (например, `#123`)
- Используется **ВСЕГДА** в:
  - Комментариях к Issue
  - Сообщениях пользователю
  - Коммитах (например, `Related Issue: #123`)
  - Файлах кода/документации (например, `// Issue: #123`)
  - PR описаниях
  - Сообщениях об ошибках
- Получается из результата `list_project_items` (поле `content.number`) или из Issue напрямую

### Правило
- **Для API вызовов:** используй `item_id` (project_item_id)
- **Для всего остального:** используй номер Issue в формате `#123`
- **Никогда не показывай пользователю `item_id`** - всегда используй номер Issue

## Status Management

**ВАЖНО:** 
- Полное руководство по статусам и передаче задач - см. `.cursor/STATUS_HANDOFF_GUIDE.md`
- Формат обновления статуса - см. `.cursor/UPDATE_STATUS_FORMAT.md`
- Конфигурация проекта и ID статусов - см. `.cursor/GITHUB_PROJECT_CONFIG.md`

**Status field shows current task state:**

1. **New task:** `Todo` (universal)
2. **On handoff:** Set `{NextAgent} - Todo`
3. **On start:** Change to `{MyAgent} - In Progress`
4. **During work:** `{MyAgent} - Blocked`, `{MyAgent} - Review`, `{MyAgent} - Returned`
5. **On finish:** Set `{NextAgent} - Todo` or `Done`

**Format:** `{Agent Name} - {State}`
- States: Todo, In Progress, Blocked, Review, Returned
- Examples: `Architect - Todo`, `Backend - In Progress`, `QA - Blocked`

**Primary tracking:** Use Project Status, not labels. Status determines agent and stage.

**Обновление статуса:**
```javascript
// GitHub API требует ID, не названия. Используй константы из GITHUB_PROJECT_CONFIG.md
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,  // из list_project_items (внутренний ID для API)
  updated_field: {
    id: 239690516,  // STATUS_FIELD_ID (число, не строка!)
    value: '02b1119e'  // STATUS_OPTIONS['Architect - In Progress'] из GITHUB_PROJECT_CONFIG.md
  }
});
```

**Важно:** 
- GitHub API требует ID, не названия (это ограничение API)
- `id` поля - число (239690516), не строка
- `value` - id опции статуса из констант (см. GITHUB_PROJECT_CONFIG.md)
- Если нужного статуса нет в константах → получить через `mcp_github_list_project_fields`
- `item_id` получай из результата `list_project_items` (это внутренний ID для API, не номер Issue)
- Всегда добавляй комментарий при передаче задачи другому агенту, используя номер Issue (например, `Issue: #123`), а не `item_id`

**ОБЯЗАТЕЛЬНЫЕ моменты обновления статуса:**
1. **При старте работы:** Todo → {MyAgent} - In Progress
2. **При передаче задачи:** {MyAgent} - In Progress → {NextAgent} - Todo
3. **При возврате задачи:** {MyAgent} - In Progress → {CorrectAgent} - Returned
4. **При блокировке:** {MyAgent} - In Progress → {MyAgent} - Blocked
5. **При завершении:** {MyAgent} - In Progress → Done (если это финальный этап)

**Когда использовать статусы:**
- **Blocked** - задача заблокирована внешними факторами (ожидание ответа, зависимость от другой задачи, технические проблемы)
- **Review** - задача на внутренней проверке/ревизии перед передачей следующему агенту
- **Returned** - задача возвращена предыдущему агенту из-за проблем или неготовности
- **In Progress** - задача в активной работе
- **Todo** - задача готова к началу работы

**Обновление статуса при старте работы:**
После выбора задачи из списка (через `find-tasks`), ОБЯЗАТЕЛЬНО обнови статус на `{MyAgent} - In Progress`:

**ВАЖНО: Используй константы из `.cursor/GITHUB_PROJECT_CONFIG.md`!**

```javascript
// 1. Получить item_id из результата list_project_items
const items = await mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Status:"{Agent} - Todo"'
});
const project_item_id = items.items[0].id;  // внутренний ID для API

// 2. Использовать константы из GITHUB_PROJECT_CONFIG.md
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,  // STATUS_FIELD_ID (число, не строка!)
    value: 'cf5cf6bb'  // STATUS_OPTIONS['Content Writer - In Progress'] из GITHUB_PROJECT_CONFIG.md
  }
});
```

**Если нужного статуса нет в константах:**
```javascript
// Получить через list_project_fields
const fields = await mcp_github_list_project_fields({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1
});
const statusField = fields.fields.find(f => f.id === 239690516);
const option = statusField.options.find(o => o.name === '{Agent} - In Progress');
const optionId = option.id;  // использовать в value
```

**КРИТИЧЕСКИ ВАЖНО:**
- ОБЯЗАТЕЛЬНО обновляй статус при старте работы (Todo → In Progress)
- ОБЯЗАТЕЛЬНО обновляй статус при передаче задачи (In Progress → NextAgent - Todo)
- ОБЯЗАТЕЛЬНО обновляй статус при возврате задачи (In Progress → CorrectAgent - Returned)
- Используй константы из GITHUB_PROJECT_CONFIG.md, не плейсхолдеры!

**Шаблон комментария при передаче задачи:**

**См. `.cursor/HANDOFF_COMMENT_TEMPLATES.md` для полного списка шаблонов**

```markdown
OK {Work type} ready. Handed off to {NextAgent}

{Optional details about what was completed}

PR: #{number} (if applicable)
Issue: #{number}
```

**Примечание:** В комментариях всегда указывай номер Issue в формате `#{number}`, а не `item_id` (project_item_id).

**Шаблон комментария при возврате задачи:**
```markdown
WARNING **Task returned: {reason}**

**Missing:**
- {what_is_missing}

**Correct agent:** {Agent Name}

**Status updated:** `{CorrectAgent} - Returned`
```

## Label Management

**Functional labels only (optional):**
- Type: `backend`, `client`, `protocol`, `infrastructure`, `security`, `database`, `game-balance`
- Content: `content`, `canon`, `lore`, `quest`, `game-design`
- UI: `ui`, `ux`
- Priority: `priority-high`, `priority-medium`, `priority-low`
- State: `needs-review`, `ready-for-dev`, `branch-created`
- Standard: `bug`, `enhancement`, `documentation`

**DO NOT use:**
- `agent:*` labels (agent determined by Status)
- `stage:*` labels (stage determined by Status)

**On Start:**
- Update Project `Status` to `{MyAgent} - In Progress`
- Add functional labels if needed (optional)

**On Finish:**
- Update Project `Status` to `{NextAgent} - Todo` (or `Done`)
- Functional labels remain (optional)


## Git Commits

```bash
git commit -m "[{agent}] {type}: {description}

{details}

Related Issue: #{number}"
```

Format: `[{agent}] {type}: {description}`
- Types: `feat:`, `fix:`, `docs:`, `test:`

## Task Return

**If task not ready:**
1. Update Status to `{CorrectAgent} - Returned`
2. Add comment with reason

**Details:** `.cursor/AGENT_TASK_RETURN.md`

## Issue Tracking in Files

**CRITICAL: Все файлы кода и документов ОБЯЗАТЕЛЬНО должны содержать номер Issue в начале файла!**

### Формат комментария

**Go код:**
```go
// Issue: #123
package server
```

**C++ код:**
```cpp
// Issue: #123
#include "Header.h"
```

**YAML документы:**
```yaml
# Issue: #123
metadata:
  id: quest-001
```

**Markdown документы:**
```markdown
<!-- Issue: #123 -->
# Документация
```

**SQL файлы:**
```sql
-- Issue: #123
CREATE TABLE users (...);
```

**Dockerfile:**
```dockerfile
# Issue: #123
FROM golang:1.24-alpine
```

**Shell скрипты:**
```bash
#!/bin/bash
# Issue: #123
```

### Правило

- **Все новые файлы:** Обязательно добавь `Issue: #{number}` в первой строке
- **При редактировании:** Если Issue не указан, добавь его
- **Цель:** Быстро найти задачу и проверить требования при ошибках в коде

## Task Requirements Check

**Перед началом работы с кодом/документами:**

1. **Прочитай Issue полностью:**
   - Требования из Issue
   - Критерии приемки
   - Связанные документы
   - Комментарии

2. **Проверь соответствие:**
   - Код/документ соответствует требованиям Issue
   - Все критерии приемки учтены
   - Нет противоречий с существующим кодом

3. **При ошибках:**
   - Вернись к Issue
   - Проверь требования
   - Исправь код/документ согласно требованиям

**Если требования неясны → верни задачу с комментарием**

## File Size Limit

**CRITICAL: Do NOT create files >500 lines!**
- If exceeds 500 lines → split into multiple files
- Each file: 300-400 lines max

## Content Quests

**Labels: `canon`, `lore`, `quest`:**
- Determine task type by labels or content
- Transfer to Content Writer via Status: `Content Writer - Todo`

## UI Tasks

**Labels: `ui`, `ux`, `client`:**
- Determine task type by labels or content
- Transfer to UI/UX Designer via Status: `UI/UX - Todo`
