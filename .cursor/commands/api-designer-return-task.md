# Return Task

Return task if not ready.

## Return Reasons

- No architecture → return to Architect
- Content quest → return to Content Writer

## Steps

1. Update Status to `{CorrectAgent} - Returned`
2. Add comment with reason

**Update Status:**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,  // внутренний ID для API (не номер Issue)
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции '{CorrectAgent} - Returned' из list_project_fields
  }
});
```

**Важно:** 
- `item_id` используется только для API вызова (внутренний ID проекта)
- В комментариях всегда указывай номер Issue в формате `Issue: #123`, а не `item_id`
- Номер Issue берется из `content.number` результата `list_project_items`
