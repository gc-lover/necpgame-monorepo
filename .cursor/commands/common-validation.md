# Common Validation Commands

## Overview
Shared validation commands used by multiple agents for quality assurance and compliance checking.

## Available Commands

### Emoji Ban Validation
```
/validate-emoji-ban
```

**Purpose:** Ensures no emoji characters in code (Windows compatibility)

**Implementation:**
```bash
python scripts/validation/validate-emoji-ban.py $(find . -name "*.py" -o -name "*.go" -o -name "*.yaml")
```

### Script Types Validation
```
/validate-script-types
```

**Purpose:** Ensures only allowed script types are used (no .sh, .ps1 in root)

**Implementation:**
```bash
python scripts/validation/validate-script-types.py
```

### Domain OpenAPI Validation
```
/validate-domains-openapi
```

**Purpose:** Validates enterprise-grade domain OpenAPI specifications

**Implementation:**
```bash
python scripts/openapi/validate-domains-openapi.py --domain {domain}
```

### GitHub Issue Management (GitHub CLI)
```bash
# Поиск задач агента (Todo статус)
gh issue list --repo gc-lover/necpgame-monorepo --state open --label 'agent:backend' -L 10

# Взятие задачи в работу
gh issue comment 123 --body '[OK] Начинаю работу над задачей'

# Передача следующему агенту
gh issue comment 123 --body '[OK] Work completed. Handed off to Network. Issue: #123'

# Закрытие завершенной задачи
gh issue close 123 --comment 'Task completed successfully'
```

**Purpose:** Управление задачами через GitHub CLI вместо Projects

**Labels для агентов:**
- `agent:backend`, `agent:api`, `agent:database`, `agent:network`, etc.

**Статусы в комментариях:**
- `[OK] Начинаю работу` - взятие задачи
- `[OK] Ready. Handed off to {NextAgent}` - передача
- `Task completed successfully` - закрытие

## Usage Examples

### Backend Agent Workflow (GitHub CLI)
```bash
# 1. Find tasks
gh issue list --repo gc-lover/necpgame-monorepo --state open --label 'agent:backend'

# 2. Take task
gh issue comment 123 --body '[OK] Начинаю работу над задачей'

# 3. Work on implementation...

# 4. Validate before handoff
/backend-validate-optimizations #123
/backend-validate-result #123

# 5. Handoff to next agent
gh issue comment 123 --body '[OK] Backend implementation complete. Handed off to Network. Issue: #123'
```

### Database Agent Workflow (GitHub CLI)
```bash
# 1. Find tasks
gh issue list --repo gc-lover/necpgame-monorepo --state open --label 'agent:database'

# 2. Take task
gh issue comment 456 --body '[OK] Начинаю работу над задачей'

# 3. Create migrations...

# 4. Validate
/database-validate-result #456

# 5. Apply migrations and handoff
gh issue comment 456 --body '[OK] Database schema created. Handed off to API Designer. Issue: #456'
```

## Error Handling

### Validation Failures
- Commands return non-zero exit codes on failure
- Detailed error messages with suggestions
- Automatic rollback where possible

### Recovery Actions
- Fix identified issues
- Re-run validation
- Update GitHub status if needed

## Integration with GitHub CLI

All validation commands now work with GitHub CLI for issue management:

- Commands executed via terminal/GitHub CLI
- Results displayed in structured format
- Integration with GitHub Issues and labels
- Real-time feedback via comments