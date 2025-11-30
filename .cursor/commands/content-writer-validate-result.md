# Validate Result

Check quest YAML readiness before handoff to Backend.

## Criteria

- [ ] YAML created, syntax valid
- [ ] Structure matches architecture
- [ ] File <=500 lines

**Result:**
- OK Ready → handoff to Backend
- ❌ Not ready → fix issues

**On handoff:** Update Status to `Backend - Todo`

**Update Status:**
**ВАЖНО: Используй константы из `.cursor/GITHUB_PROJECT_CONFIG.md`!**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,  // из результата list_project_items
  updated_field: {
    id: 239690516,  // STATUS_FIELD_ID (число, не строка!)
    value: '72d37d44'  // STATUS_OPTIONS['Backend - Todo'] из GITHUB_PROJECT_CONFIG.md
  }
});
```

**ОБЯЗАТЕЛЬНО:** После обновления статуса добавь комментарий к Issue с номером Issue (например, `Issue: #123`), а не `item_id`.
