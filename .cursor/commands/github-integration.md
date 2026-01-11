# GitHub Integration (УСТАРЕЛО)

**⚠️ ВНИМАНИЕ: Этот документ УСТАРЕЛ!**

Используй комбинированный подход:
- **GH CLI** для поиска задач
- **MCP GitHub** для обновления статусов в Projects

**Актуальная документация:**
- `@.cursor/MCP_GITHUB_GUIDE.md` - работа с MCP GitHub (поиск, статусы, workflow)

---

## Overview (УСТАРЕЛО)
Commands for managing GitHub Issues and task lifecycle using GitHub CLI.

## Core Commands

### Issue Management with GitHub CLI
```bash
# Поиск задач агента
gh issue list --repo gc-lover/necpgame-monorepo --state open --label 'agent:backend'

# Взятие задачи в работу
gh issue comment 123 --body '[OK] Начинаю работу над задачей'

# Передача следующему агенту
gh issue comment 123 --body '[OK] Work completed. Handed off to Network. Issue: #123'

# Закрытие завершенной задачи
gh issue close 123 --comment 'Task completed successfully'

# Просмотр деталей задачи
gh issue view 123 --repo gc-lover/necpgame-monorepo
```

**Purpose:** Управление задачами через GitHub CLI вместо Projects

**Labels для агентов:**
- `agent:backend`, `agent:api`, `agent:database`, `agent:network`, etc.

### Status Transitions (через комментарии + лейблы)

| From Status | To Status | Trigger | GitHub CLI Commands |
|-------------|-----------|---------|-------------------|
| Todo | In Progress | Agent takes task | `gh issue comment` + `gh issue edit --add-label 'status:in-progress'` |
| In Progress | Todo | Handoff to next agent | `gh issue comment` + `gh issue edit --remove-label 'status:in-progress' --add-label 'agent:{next}'` |
| In Progress | Review | Self-review | `gh issue comment` + `gh issue edit --remove-label 'status:in-progress' --add-label 'status:review'` |
| Review | Todo | Ready for next agent | `gh issue comment` + `gh issue edit --remove-label 'status:review' --add-label 'agent:{next}'` |
| Todo | Done | Final completion | `gh issue close` + comment |

## GitHub CLI Commands

### List Issues (замена List Project Items)
```bash
# Поиск задач конкретного агента (Todo/In Progress статусы)
gh issue list --repo gc-lover/necpgame-monorepo --state open --label 'agent:backend'

# Поиск всех открытых задач
gh issue list --repo gc-lover/necpgame-monorepo --state open

# Фильтр по автору или другим лейблам
gh issue list --repo gc-lover/necpgame-monorepo --state open --author gc-lover
```

### Comment Issues (замена Update Project Item + Add Issue Comment)
```bash
# Взятие задачи
gh issue comment 123 --body '[OK] Начинаю работу над задачей'

# Передача с указанием следующего агента
gh issue comment 123 --body '[OK] Backend implementation complete. Handed off to Network. Issue: #123'

# Запрос ревью
gh issue comment 123 --body '[REVIEW] Implementation ready for review'

# Возврат на доработку
gh issue comment 123 --body '[WARNING] Returned to Backend for fixes. Issue: #123'
```

### Close Issues (замена Done status)
```bash
# Закрытие завершенной задачи
gh issue close 123 --comment 'Task completed successfully'

# Переоткрытие если нужно
gh issue reopen 123
```

## Labels Reference

**Agent Labels:**
- `agent:backend` - Backend агент
- `agent:api` - API Designer
- `agent:database` - Database агент
- `agent:network` - Network агент
- `agent:security` - Security агент
- `agent:devops` - DevOps агент
- `agent:qa` - QA агент
- `agent:performance` - Performance агент
- `agent:ue5` - UE5 агент
- `agent:content` - Content Writer
- `agent:architect` - Architect
- `agent:idea` - Idea Writer
- `agent:ui-ux` - UI/UX Designer
- `agent:game-balance` - Game Balance агент
- `agent:release` - Release агент

**Status Labels (опционально):**
- `status:todo` - новые задачи (по умолчанию открытые без этого лейбла)
- `status:in-progress` - задача в работе
- `status:review` - задача на ревью
- `status:blocked` - заблокирована (нуждается в разблокировке)
- `status:returned` - возвращена на доработку
- `status:done` - завершена (issue закрыта)

**Логика статусов:**
- GitHub Issues имеют только `open`/`closed` состояния
- Наши статусы реализуются через лейблы `status:*`
- Поиск задач: `--state open` + `--label 'agent:{agent}'` (без статуса = Todo)
- Для поиска конкретного статуса: добавить `--label 'status:{status}'`

## Workflow Integration

### Taking a Task (GitHub CLI)
1. Find task: `gh issue list --repo gc-lover/necpgame-monorepo --state open --label 'agent:{agent}'`
2. Take task: `gh issue comment {number} --body '[OK] Начинаю работу над задачей'`
3. Add working label: `gh issue edit {number} --add-label 'status:in-progress'`

### Handoff Process (GitHub CLI)
1. Validate work with agent command (`/{agent}-validate-result`)
2. Update status via comment: `gh issue comment {number} --body '[OK] Work completed. Handed off to {NextAgent}. Issue: #{number}'`
3. Update labels: `gh issue edit {number} --remove-label 'status:in-progress' --add-label 'agent:{next-agent}'`

### Closing Tasks
1. For final completion: `gh issue close {number} --comment 'Task completed successfully'`
2. For release completion: `gh issue close {number} --comment 'Released in v{version}'`

## Error Handling

### Common Issues
- **Issue not found**: Check issue number is correct
- **Permission denied**: Verify GitHub token has repo access
- **Label not found**: Create label first or use existing ones

### Recovery
- Check issue directly: `gh issue view {number}`
- List all agent issues: `gh issue list --label 'agent:{agent}'`
- Re-run failed commands after fixing authentication

### Authentication Issues
```bash
# Проверить аутентификацию
gh auth status

# Переавторизоваться если нужно
gh auth login
```