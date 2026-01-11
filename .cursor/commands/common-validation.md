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

### GitHub Issue Management

**ВАЖНО:** Используй комбинированный подход!
- **GH CLI** для поиска задач
- **MCP GitHub** для обновления статусов в Projects

**Детали:**
- `@.cursor/MCP_GITHUB_GUIDE.md` - работа с MCP GitHub (поиск, статусы, workflow)

## Usage Examples

**ВАЖНО:** Все операции с задачами через комбинированный подход!
См. `@.cursor/MCP_GITHUB_GUIDE.md` для полного workflow.

## Error Handling

### Validation Failures
- Commands return non-zero exit codes on failure
- Detailed error messages with suggestions
- Automatic rollback where possible

### Recovery Actions
- Fix identified issues
- Re-run validation
- Update GitHub status if needed

## Integration with GitHub

Валидация выполняется через терминал.
Обновление статусов задач через MCP GitHub (см. `@.cursor/MCP_GITHUB_GUIDE.md`).