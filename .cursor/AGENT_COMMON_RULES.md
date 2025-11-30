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


## Status Management

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
