# Common Agent Rules

## 🚀 НАЧНИ ЗДЕСЬ

**Новичок?** Читай `.cursor/AGENT_SIMPLE_GUIDE.md` - там всё просто и понятно!

**Опытный?** Эти правила - для деталей и edge cases.

---

## WARNING КРИТИЧЕСКИ ВАЖНО: Краткость и фокус на коде

**НИКОГДА НЕ ДЕЛАЙ:**
- ❌ Отчеты, summary, анализы, обзоры
- ❌ Длинные объяснения (максимум 1-2 предложения)
- ❌ Markdown файлы с отчетами/анализом
- ❌ Verbose комментарии в коде
- ❌ Таблицы статистики без запроса

**ВСЕГДА ДЕЛАЙ:**
- OK Работай с кодом напрямую
- OK Краткие ответы (1-2 предложения)
- OK Показывай только код и изменения
- OK Минимум текста, максимум действий
- OK Фокус на реализации

**Комментарии:**
- При передаче: `OK Ready. Handed off to {NextAgent}. Issue: #{number}`
- При возврате: `WARNING Returned: {reason}. Issue: #{number}`
- НЕ пиши длинные списки изменений

---

## WARNING КРИТИЧЕСКИ ВАЖНО: Git команды

### OK РАЗРЕШЕННЫЕ git операции:

```bash
git add <file>              # Добавить файлы
git commit -m "message"     # Создать коммит
git push                    # Отправить изменения
git status                  # Проверить статус
git diff                    # Посмотреть изменения
git log                     # История коммитов
git branch                  # Список веток
git checkout <branch>       # Переключить ветку
git pull                    # Получить изменения
git show                    # Показать коммит
```

### ❌ ЗАПРЕЩЕННЫЕ git операции (НИКОГДА НЕ ИСПОЛЬЗУЙ):

```bash
git reset --hard            # ❌ Уничтожает изменения
git reset HEAD~             # ❌ Переписывает историю
git reset --soft            # ❌ Переписывает историю
git push --force            # ❌ Перезаписывает удаленную историю
git push -f                 # ❌ Перезаписывает удаленную историю
git rebase                  # ❌ Переписывает историю
git rebase -i               # ❌ Переписывает историю
git commit --amend          # ❌ Переписывает последний коммит
git filter-branch           # ❌ Массовая перезапись истории
git reflog delete           # ❌ Удаляет записи reflog
git clean -fd               # ❌ Удаляет неотслеживаемые файлы
git clean -fdx              # ❌ Удаляет все неотслеживаемые файлы
```

### 🛡️ ПРАВИЛО:

**AI агенты ДОЛЖНЫ сохранять git историю неизменной!**

Если сделал ошибку:
- OK Создай новый коммит с исправлением
- OK Используй `git revert <commit>` для отмены коммита
- ❌ НЕ используй `git reset` или `git commit --amend`
- ❌ НЕ переписывай историю

**Причина:** Деструктивные команды могут:
- Потерять важные изменения
- Сломать историю проекта
- Создать конфликты для других агентов/разработчиков
- Нарушить CI/CD pipeline

---

## GitHub Project Configuration

**Project parameters:** See `.cursor/GITHUB_PROJECT_CONFIG.md`

Все агенты используют одинаковые параметры:
- `owner_type: 'user'`
- `owner: 'gc-lover'`
- `project_number: 1`
- `project_node_id: 'PVT_kwHODCWAw84BIyie'`
- `status_field_id: '239690516'`

## Backend Code Generation

**ogen - стандарт для всех сервисов**

- OK НОВЫЕ сервисы → `ogen` (90% faster!)
- 🔄 СУЩЕСТВУЮЩИЕ → мигрируй на `ogen` (#1590)

**Гайд:** `.cursor/ogen/README.md` и `.cursor/ogen/02-MIGRATION-STEPS.md`

## Performance Optimizations (для Backend)

**WARNING КРИТИЧНО: Backend ОБЯЗАН применять оптимизации для MMOFPS RPG**

**BLOCKER (задачу НЕЛЬЗЯ передавать без этого):**
- ❌ НЕТ context timeouts → FIX before handoff
- ❌ НЕТ DB pool config → FIX before handoff
- ❌ Goroutine leaks → FIX before handoff
- ❌ НЕТ struct alignment → FIX before handoff
- ❌ НЕТ structured logging → FIX before handoff

**Базовые (ВСЕГДА для ВСЕХ сервисов):**
- Context timeouts для внешних вызовов
- DB connection pool (25-50 connections)
- Struct field alignment (fieldalignment)
- Goroutine leak detection (goleak)
- Structured logging (zap)
- Health/Metrics endpoints

**Hot Path (>100 RPS):**
- Memory pooling (`sync.Pool`)
- Batch DB operations
- Lock-free structures (`atomic`)
- Preallocation
- Zero allocations в benchmarks

**Game Servers (real-time):**
- Worker pool для горутин
- Spatial partitioning (>100 объектов)
- Adaptive tick rate
- GC tuning (`GOGC=50`)
- Profiling endpoints (pprof)

**🆕 Database Advanced (2025):**
- Time-series partitioning → query ↓90%, auto retention
- Materialized views → 100x speedup (leaderboards)
- Covering indexes → query ↓50-70%
- Partial indexes → index size ↓60-80%
- pgBouncer → 10k connections to 25 pool
- LISTEN/NOTIFY → real-time events
- WAL tuning → write ↑50%
- JSONB + GIN indexes

**🆕 Redis Advanced (2025):**
- Session store (stateless servers)
- Pipelining → round-trips ↓99%
- Lua scripts (atomic ops)
- Redis Cluster (millions ops/sec)
- Pub/Sub invalidation (distributed cache)
- Sorted sets (leaderboards)

**🆕 Resilience (2025):**
- Circuit breakers (DB resilience)
- Feature flags (graceful degradation)
- Load shedding (backpressure)
- Fallback strategies (multi-level)
- Connection retry (exponential backoff)

**Валидация ОБЯЗАТЕЛЬНА:**
- Запускай `/backend-validate-optimizations #123` перед передачей
- Если BLOCKER → исправь и повтори
- Передавай ТОЛЬКО после OK validation passed

**Детали:**
- `.cursor/GO_BACKEND_PERFORMANCE_BIBLE.md` - **150+ оптимизаций** (13 parts, обновлено 2025)
- `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md` - чек-лист с новыми техниками
- `.cursor/templates/backend-*.md` - шаблоны кода
- `.cursor/performance/*.md` - 13 частей Performance Bible
- `/backend-validate-optimizations #123` - команда для проверки

**Рефакторинг существующих сервисов:**
- Backend ОБЯЗАН рефакторить неоптимизированный код
- При работе с существующим сервисом - применяй оптимизации
- Создавай отдельные Issues для рефакторинга если нашел проблемы
- Используй `/backend-refactor-service {service-name}` для планирования

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

## Status & Agent Management

**ВАЖНО:** 
- Конфигурация проекта и ID опций — `.cursor/GITHUB_PROJECT_CONFIG.md`
- Простое руководство — `.cursor/AGENT_SIMPLE_GUIDE.md`

**Status (стадия):** `Todo`, `In Progress`, `Review`, `Blocked`, `Returned`, `Done`  
**Agent (ответственный):** `Idea`, `Content`, `Backend`, `Architect`, `API`, `DB`, `QA`, `Performance`, `Security`, `Network`, `DevOps`, `UI/UX`, `UE5`, `GameBalance`, `Release`

**Как читать связку:**  
`Status: Todo + Agent: Backend` → Backend должен взять.  
`Status: In Progress + Agent: Backend` → Backend работает.  
`Status: Todo + Agent: QA` → передано QA.

**Первичный трекинг:** всегда через поля Status и Agent (не labels).

**Обновление полей (API требует ID!):**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id, // из list_project_items
  updated_field: [
    { id: 239690516, value: '83d488e7' }, // Status: In Progress
    { id: 243899542, value: '{AGENT_ID}' } // Agent: из GITHUB_PROJECT_CONFIG.md
  ]
});
```

**Обязательные точки обновления:**
1. Старт работы: Status `Todo` → `In Progress`, Agent = {MyAgent}
2. Передача дальше: Status `In Progress`/`Review` → `Todo`, Agent = {NextAgent}
3. Возврат: Status → `Returned`, Agent = {CorrectAgent}
4. Блокер: Status → `Blocked`, Agent = {MyAgent}
5. Финал: Status → `Done`, Agent = {CurrentAgent} (если последний)

**Поиск задач:**
```javascript
mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Agent:"{MyAgent}" Status:"Todo"'
});
```

**Получить опции если не знаешь ID:**
```javascript
const fields = await mcp_github_list_project_fields({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1
});
const status = fields.fields.find(f => f.id === 239690516);
const agent = fields.fields.find(f => f.id === 243899542);
```

**Комментарий при передаче задачи:**
```markdown
OK Ready. Handed off to {NextAgent}
Issue: #{number}
```

**Комментарий при возврате задачи:**
```markdown
WARNING Returned: {reason}
Correct agent: {Agent Name}
Issue: #{number}
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
- `agent:*` labels (агент хранится в поле Agent)
- `stage:*` labels (стадия хранится в поле Status)

**On Start:**
- Set `Status: In Progress`, `Agent: {MyAgent}`
- Add functional labels if needed (optional)

**On Finish:**
- Set `Status: Todo`, `Agent: {NextAgent}` (или `Status: Done`, если финал)
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
1. Update fields: `Status: Returned`, `Agent: {CorrectAgent}`
2. Add comment with reason

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
- Transfer: Status `Todo`, Agent `Content`

## UI Tasks

**Labels: `ui`, `ux`, `client`:**
- Determine task type by labels or content
- Transfer: Status `Todo`, Agent `UI/UX`
