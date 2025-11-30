# Validate Result

Check backend readiness and determine next agent.

## Criteria

- [ ] Backend implemented, API works
- [ ] Tests passed, code meets standards
- [ ] Metrics and health checks configured

## Determine Next Agent

**Content quest (labels `canon`, `lore`, `quest`):**
- OK Ready → handoff to QA, Update Status to `QA - Todo`
- ❌ Not ready → fix issues, don't handoff

**System task:**
- OK Ready → handoff to Network, Update Status to `Network - Todo`
- ❌ Not ready → fix issues, don't handoff

**Result:** OK Ready → handoff to determined agent / ❌ Not ready → fix issues

**Update Status:**
**ВАЖНО: Используй константы из `.cursor/GITHUB_PROJECT_CONFIG.md`!**
```javascript
// Для контент-квестов: передать QA
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,  // из результата list_project_items
  updated_field: {
    id: 239690516,  // STATUS_FIELD_ID (число, не строка!)
    value: '86ca422e'  // STATUS_OPTIONS['QA - Todo'] из GITHUB_PROJECT_CONFIG.md
  }
});

// Для системных задач: передать Network
// value: '{Network - Todo option_id}' - получить через list_project_fields если нет в константах
```

**ОБЯЗАТЕЛЬНО:** После обновления статуса добавь комментарий к Issue с номером Issue (например, `Issue: #123`), а не `item_id`.
