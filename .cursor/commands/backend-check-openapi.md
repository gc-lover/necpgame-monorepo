# Check OpenAPI

Check if OpenAPI spec exists and is ready for code generation before starting.

## Check

1. Verify Agent = `Backend`, Status = `Todo` или `In Progress`
2. Check file: `proto/openapi/{service-name}.yaml`
3. Validate: `npx -y @redocly/cli lint proto/openapi/{service-name}.yaml`
4. **НОВАЯ ПРОВЕРКА:** Проверь размер спецификации

### Проверка размера спецификации:

```bash
# Подсчитай строки в спецификации
wc -l proto/openapi/{service-name}.yaml

# Если >500 строк - проверь модульную структуру
ls proto/openapi/{service-name}/
```

**Если спецификация >500 строк:**
- OK Разбита на модули (`{service-name}/schemas/`, `{service-name}/paths/`) → OK, продолжай
- ❌ НЕ разбита (монолитный файл) → верни API Designer

**Result:**
- OK Found, valid, <500 lines (or split) → can start
- WARNING Found, valid, >500 lines (not split) → return to API (Status `Returned`, Agent `API`)
- ❌ Not found or invalid → return to API (Status `Returned`, Agent `API`)

**Update fields (if returning - spec >500 lines and not split):**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'c01c12e9' }, // Status: Returned
    { id: 243899542, value: '6aa5d9af' }, // Agent: API
  ]
});

// Добавить комментарий
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: 'WARNING **Task returned: OpenAPI spec too large**\n\n' +
        '**Problem:**\n' +
        '- OpenAPI spec exceeds 500 lines (currently: XXX lines)\n' +
        '- Not split into modules (violates file size limit)\n\n' +
        '**Expected:**\n' +
        '- Split spec into modules: `{service-name}/schemas/`, `{service-name}/paths/`\n' +
        '- Each module <500 lines\n' +
        '- Use `$ref` to link modules\n' +
        '- See: `.cursor/rules/agent-api-designer.mdc` (Splitting Large Specs)\n\n' +
        '**Correct agent:** API Designer\n\n' +
        '**Status updated:** `API Designer - Returned`\n\n' +
        'Issue: #' + issue_number
});
```

**Update fields (if returning - not found or invalid):**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'c01c12e9' }, // Status: Returned
    { id: 243899542, value: '6aa5d9af' }, // Agent: API
  ]
});
```
