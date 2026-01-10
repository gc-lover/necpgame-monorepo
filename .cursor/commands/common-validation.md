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

### GitHub Fields Update
```
/update-github-fields --item-id {id} --type {TYPE} --check {0|1}
```

**Purpose:** Updates GitHub Project fields for task management

**Types:**
- API, MIGRATION, DATA, BACKEND, UE5

**Check values:**
- 0: Task not checked
- 1: Task checked

**Implementation:**
```bash
python scripts/update-github-fields.py --item-id 123 --type API --check 1
```

## Usage Examples

### Backend Agent Workflow
```bash
# 1. Find tasks
/backend-find-tasks

# 2. Take task and update fields
python scripts/update-github-fields.py --item-id 123 --type BACKEND --check 0

# 3. Work on implementation...

# 4. Validate before handoff
/backend-validate-optimizations #123
/backend-validate-result #123

# 5. Update fields for handoff
python scripts/update-github-fields.py --item-id 123 --type BACKEND --check 1
```

### Database Agent Workflow
```bash
# 1. Find tasks
/database-find-tasks

# 2. Take task
python scripts/update-github-fields.py --item-id 456 --type MIGRATION --check 0

# 3. Create migrations...

# 4. Validate
/database-validate-result #456

# 5. Apply migrations if content
/database-apply-content-migration
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

## Integration with MCP

All validation commands are designed to work with MCP (Model Context Protocol) in Cursor IDE:

- Commands can be executed via MCP interface
- Results displayed in structured format
- Integration with GitHub Project updates
- Real-time feedback and suggestions